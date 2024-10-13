package service

import dao.ClientDAO
import model.{Client, ClientId}
import zio.macros.accessible
import zio.{Task, URLayer, ZLayer}

@accessible
object ClientService {
  trait Service {
    def create(
        firstName: String,
        lastName: String,
        address: String
    ): Task[Client]
    def delete(id: ClientId): Task[Unit]
    def get(id: ClientId): Task[Option[Client]]
    def getAll: Task[List[Client]]
    def update(
        id: ClientId,
        firstName: Option[String] = None,
        lastName: Option[String] = None,
        address: Option[String] = None
    ): Task[Unit]
  }

  case class Impl(clientDAO: ClientDAO.Service) extends Service {
    override def create(
        firstName: String,
        lastName: String,
        address: String
    ): Task[Client] = clientDAO.create(firstName, lastName, address)
    override def delete(id: ClientId): Task[Unit]        = clientDAO.delete(id)
    override def get(id: ClientId): Task[Option[Client]] = clientDAO.get(id)
    override def getAll: Task[List[Client]]              = clientDAO.getAll
    override def update(
        id: ClientId,
        firstName: Option[String] = None,
        lastName: Option[String] = None,
        address: Option[String] = None
    ): Task[Unit] = clientDAO.update(id, firstName, lastName, address)
  }

  def live: URLayer[ClientDAO.Service, Service] =
    ZLayer.fromFunction(Impl.apply _)
}
