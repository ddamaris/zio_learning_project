package controller

import model.{AccountCreated, AccountId, AccountUpdated, ClientId}
import service.AccountService
import utils._
import zhttp.http._
import zio._
import zio.json._

final case class AccountController(service: AccountService.Service) {

  val routes: Http[Any, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> !! / "account" / "all" =>
        service.getAll.map(accounts => Response.json(accounts.toJson))

      case Method.GET -> !! / "account" / id =>
        for {
          id  <- AccountId.get(id)
          acc <- service.get(id)
        } yield Response.json(acc.toJson)

      case Method.GET -> !! / "account/clientId" / id =>
        for {
          id   <- ClientId.get(id)
          accs <- service.getForClient(id)
        } yield Response.json(accs.toJson)

      case req @ Method.POST -> !! / "account" =>
        for {
          accCreated <- parseBody[AccountCreated](req)
          acc        <- service.create(accCreated.kind, accCreated.clientId)
        } yield Response.json(acc.toJson)

      case req @ Method.PATCH -> !! / "account" / id =>
        for {
          accId      <- AccountId.get(id)
          accUpdated <- parseBody[AccountUpdated](req)
          _ <- service.update(
            accId,
            accUpdated.kind,
            accUpdated.balance,
            accUpdated.clientId
          )
        } yield Response.ok

      case req @ Method.DELETE -> !! / "account" / id =>
        for {
          id          <- AccountId.get(id)
          optPleasure <- service.get(id)
          _           <- service.delete(id)
        } yield Response.ok
    }
}

object AccountController {
  def live: URLayer[AccountService.Service, AccountController] =
    ZLayer.fromFunction(AccountController.apply _)
}
