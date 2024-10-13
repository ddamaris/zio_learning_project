package dao

import model.{AccountId, Transaction, TransactionId}
import zio._
import zio.macros.accessible

import javax.sql.DataSource

@accessible
object TransactionDAO {

  trait Service {
    def create(from: AccountId, to: AccountId, amount: Long): Task[Transaction]
    def get(id: TransactionId): Task[Option[Transaction]]
    def getAll: Task[List[Transaction]]
  }

  case class Impl(dataSource: DataSource) extends Service {

    import utils.QuillContext._

    override def create(
        from: AccountId,
        to: AccountId,
        amount: Long
    ): Task[Transaction] =
      for {
        trn <- Transaction(from, to, amount)
        _ <- run(query[Transaction].insertValue(lift(trn)))
          .provideEnvironment(ZEnvironment(dataSource))
      } yield trn

    override def get(id: TransactionId): Task[Option[Transaction]] =
      run(query[Transaction].filter(_.id == lift(id)))
        .provideEnvironment(ZEnvironment(dataSource))
        .map(_.headOption)

    override def getAll: Task[List[Transaction]] =
      run(query[Transaction])
        .provideEnvironment(ZEnvironment(dataSource))
  }

  def live: URLayer[DataSource, Service] =
    ZLayer.fromFunction(Impl.apply _)
}
