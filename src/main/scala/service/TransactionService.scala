package service

import dao.{AccountDAO, TransactionDAO}
import errors.AppError.FailedTransaction
import model.{AccountId, Transaction, TransactionId}
import zio.macros.accessible
import zio.{Task, URLayer, ZIO, ZLayer}

@accessible
object TransactionService {

  trait Service {
    def create(from: AccountId, to: AccountId, amount: Long): Task[Transaction]
    def get(id: TransactionId): Task[Option[Transaction]]
    def getAll: Task[List[Transaction]]
  }

  case class Impl(transactionDao: TransactionDAO.Service, accountDao: AccountDAO.Service)
      extends Service {
    override def create(
        from: AccountId,
        to: AccountId,
        amount: Long
    ): Task[Transaction] =
      for {
        acc_from <- accountDao.get(from)
        acc_to   <- accountDao.get(to)
        trn <-
          if (acc_from.get.balance - amount >= 0)
            for {
              _ <- accountDao.update(
                acc_from.get.id,
                None,
                acc_from.map(a => a.balance - amount),
                None
              )
              _ <- accountDao.update(
                acc_to.get.id,
                None,
                acc_to.map(a => a.balance + amount),
                None
              )
              res <- transactionDao.create(from, to, amount)
//            transactionDao.create(from, to, amount)
            } yield res
          else ZIO.fail(FailedTransaction("Too big amount"))
      } yield trn

    override def get(id: TransactionId): Task[Option[Transaction]] =
      transactionDao.get(id)

    override def getAll: Task[List[Transaction]] = transactionDao.getAll
  }

  def live: URLayer[TransactionDAO.Service with AccountDAO.Service, Service] =
    ZLayer.fromFunction(Impl.apply _)

}
