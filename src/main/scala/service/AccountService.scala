package service

import dao.AccountDAO
import model.{Account, AccountId, ClientId}
import zio.macros.accessible
import zio.{Task, URLayer, ZLayer}

@accessible
object AccountService {
  trait Service {
    def create(kind: String, clientId: ClientId): Task[Account]
    def delete(id: AccountId): Task[Unit]
    def get(id: AccountId): Task[Option[Account]]
    def getForClient(clientId: ClientId): Task[List[Account]]
    def getAll: Task[List[Account]]
    def update(
        id: AccountId,
        kind: Option[String] = None,
        balance: Option[Long] = None,
        clientId: Option[ClientId] = None
    ): Task[Unit]
  }

  case class Impl(accountDAO: AccountDAO.Service) extends Service {
    override def create(kind: String, clientId: ClientId): Task[Account] =
      accountDAO.create(kind, clientId)
    override def delete(id: AccountId): Task[Unit] = accountDAO.delete(id)
    override def get(id: AccountId): Task[Option[Account]] = accountDAO.get(id)
    def getForClient(clientId: ClientId): Task[List[Account]] =
      accountDAO.getForClient(clientId)
    def getAll: Task[List[Account]] = accountDAO.getAll
    def update(
        id: AccountId,
        kind: Option[String] = None,
        balance: Option[Long] = None,
        clientId: Option[ClientId] = None
    ): Task[Unit] = accountDAO.update(id, kind, balance, clientId)
  }

  def live: URLayer[AccountDAO.Service, Service] =
    ZLayer.fromFunction(Impl.apply _)
}
