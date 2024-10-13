package dao

import model.{Client, ClientId}
import zio._
import zio.macros.accessible

import javax.sql.DataSource

@accessible
object ClientDAO {

  trait Service {
    def create(
        firstName: String,
        lastName: String,
        address: String
    ): Task[Client]
    def get(id: ClientId): Task[Option[Client]]
    def getAll: Task[List[Client]]
    def delete(id: ClientId): Task[Unit]
    def update(
        id: ClientId,
        firstName: Option[String],
        lastName: Option[String],
        address: Option[String]
    ): Task[Unit]
  }

  case class Impl(dataSource: DataSource) extends Service {

    import utils.QuillContext._

    override def create(
        firstName: String,
        lastName: String,
        address: String
    ): Task[Client] =
      for {
        client <- Client(firstName, lastName, address)
        _ <- run(query[Client].insertValue(lift(client)))
          .provideEnvironment(ZEnvironment(dataSource))
      } yield client

    override def delete(id: ClientId): Task[Unit] =
      run(query[Client].filter(_.id == lift(id)).delete)
        .provideEnvironment(ZEnvironment(dataSource))
        .unit

    def get(id: ClientId): Task[Option[Client]] =
      run(query[Client].filter(_.id == lift(id)))
        .provideEnvironment(ZEnvironment(dataSource))
        .map(_.headOption)

    def getAll: Task[List[Client]] = run(query[Client])
      .provideEnvironment(ZEnvironment(dataSource))

    def update(
        id: ClientId,
        firstName: Option[String],
        lastName: Option[String],
        address: Option[String]
    ): Task[Unit] = run(
      query[Client]
        .filter(_.id == lift(id))
        .update(
          cln => cln.firstName -> lift(firstName).getOrElse(cln.firstName),
          cln => cln.lastName -> lift(lastName).getOrElse(cln.lastName),
          cln => cln.address -> lift(address).getOrElse(cln.address)
        )
    )
      .provideEnvironment(ZEnvironment(dataSource))
      .unit
  }

  def live: URLayer[DataSource, Service] =
    ZLayer.fromFunction(Impl.apply _)
}
