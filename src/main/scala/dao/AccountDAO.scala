package dao

import model.{Account, AccountId, ClientId}
import zio._
import zio.macros.accessible

import javax.sql.DataSource

@accessible
object AccountDAO {

  trait Service {
    def create(kind: String, clientId: ClientId): Task[Account]
    def getForClient(clientId: ClientId): Task[List[Account]]
    def get(id: AccountId): Task[Option[Account]]
    def getAll: Task[List[Account]]
    def delete(id: AccountId): Task[Unit]
    def update(
        id: AccountId,
        kind: Option[String],
        balance: Option[Long],
        clientId: Option[ClientId]
    ): Task[Unit]
  }

  case class Impl(dataSource: DataSource) extends Service {

    import utils.QuillContext._

    override def create(kind: String, clientId: ClientId): Task[Account] =
      for {
        acc <- Account(kind, clientId)
        _ <- run(query[Account].insertValue(lift(acc)))
          .provideEnvironment(ZEnvironment(dataSource))
      } yield acc

    override def delete(id: AccountId): Task[Unit] =
      run(query[Account].filter(_.id == lift(id)).delete)
        .provideEnvironment(ZEnvironment(dataSource))
        .unit

    override def get(id: AccountId): Task[Option[Account]] =
      run(query[Account].filter(_.id == lift(id)))
        .provideEnvironment(ZEnvironment(dataSource))
        .map(_.headOption)

    override def getForClient(clientId: ClientId): Task[List[Account]] =
      run(
        query[Account]
          .filter(_.clientId == lift(clientId))
      )
        .provideEnvironment(ZEnvironment(dataSource))

    override def getAll: Task[List[Account]] =
      run(query[Account]).provideEnvironment(ZEnvironment(dataSource))

    override def update(
        id: AccountId,
        kind: Option[String],
        balance: Option[Long],
        clientId: Option[ClientId]
    ): Task[Unit] =
      run(
        query[Account]
          .filter(_.id == lift(id))
          .update(
            acc => acc.kind -> lift(kind).getOrElse(acc.kind),
            acc => acc.balance -> lift(balance).getOrElse(acc.balance),
            acc => acc.clientId -> lift(clientId).getOrElse(acc.clientId)
          )
      ).provideEnvironment(ZEnvironment(dataSource)).unit
  }

  def live: URLayer[DataSource, Service] =
    ZLayer.fromFunction(Impl.apply _)
}
