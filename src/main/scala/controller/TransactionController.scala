package controller

import model.{TransactionCreated, TransactionId}
import service.TransactionService
import utils._
import zhttp.http._
import zio._
import zio.json._

final case class TransactionController(service: TransactionService.Service) {

  val routes: Http[Any, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> !! / "transaction" / "all" =>
        service.getAll.map(trns => Response.json(trns.toJson))

      case Method.GET -> !! / "transaction" / id =>
        for {
          id  <- TransactionId.get(id)
          trn <- service.get(id)
        } yield Response.json(trn.toJson)

      case req @ Method.POST -> !! / "transaction" =>
        for {
          trnCreated <- parseBody[TransactionCreated](req)
          trn <-
            service.create(
              trnCreated.from,
              trnCreated.to,
              trnCreated.amount
            )
        } yield Response.json(trn.toJson)
    }
}

object TransactionController {
  def live: URLayer[TransactionService.Service, TransactionController] =
    ZLayer.fromFunction(TransactionController.apply _)
}
