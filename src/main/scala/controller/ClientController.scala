package controller

import model.{ClientCreated, ClientId, ClientUpdated}
import service.ClientService
import utils._
import zhttp.http._
import zio._
import zio.json._

final case class ClientController(service: ClientService.Service) {

  val routes: Http[Any, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> !! / "healscheck" =>
        ZIO.succeed(Response.status(Status.Ok))

      case Method.GET -> !! / "client" / "all" =>
        service.getAll.map(clients => Response.json(clients.toJson))

      case Method.GET -> !! / "client" / id =>
        for {
          id     <- ClientId.get(id)
          client <- service.get(id)
        } yield Response.json(client.toJson)

      case req @ Method.POST -> !! / "client" =>
        for {
          clientCreated <- parseBody[ClientCreated](req)
          client <-
            service.create(
              clientCreated.firstName,
              clientCreated.lastName,
              clientCreated.address
            )
        } yield Response.json(client.toJson)

      case req @ Method.PATCH -> !! / "client" / id =>
        for {
          clientId      <- ClientId.get(id)
          clientUpdated <- parseBody[ClientUpdated](req)
          _ <- service.update(
            clientId,
            clientUpdated.firstName,
            clientUpdated.lastName,
            clientUpdated.address
          )
        } yield Response.ok

      case Method.DELETE -> !! / "client" / id =>
        for {
          id <- ClientId.get(id)
          _  <- service.delete(id)
        } yield Response.ok

    }

}

object ClientController {
  def live: URLayer[ClientService.Service, ClientController] =
    ZLayer.fromFunction(ClientController.apply _)
}
