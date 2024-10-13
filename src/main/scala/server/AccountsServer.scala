package server

import controller.{AccountController, ClientController, TransactionController}
import dao.Migrations
import zhttp.http._
import zhttp.service.Server
import zio._

final case class AccountsServer(
    clientController: ClientController,
    accountController: AccountController,
    transactionController: TransactionController,
    migrations: Migrations
) {
  val allRoutes: HttpApp[Any, Throwable] =
    clientController.routes ++ accountController.routes ++ transactionController.routes

  def start: ZIO[Any, Throwable, Unit] =
    for {
      _ <- migrations.reset
      _ <- Server.start(8080, allRoutes)
    } yield ()
}

object AccountsServer {
  val live: ZLayer[
    ClientController
      with AccountController
      with TransactionController
      with Migrations,
    Nothing,
    AccountsServer
  ] =
    ZLayer.fromFunction(AccountsServer.apply _)
}
