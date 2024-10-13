import controller.{AccountController, ClientController, TransactionController}
import dao.{AccountDAO, ClientDAO, Migrations, TransactionDAO}
import server.AccountsServer
import service.{AccountService, ClientService, TransactionService}
import utils.QuillContext
import zio._

object Main extends ZIOAppDefault {
  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    ZIO
      .serviceWithZIO[AccountsServer](_.start)
      .provide(
        AccountsServer.live,
        Migrations.layer,
        QuillContext.dataSourceLayer,
        ClientController.live,
        ClientService.live,
        ClientDAO.live,
        AccountController.live,
        AccountService.live,
        AccountDAO.live,
        TransactionController.live,
        TransactionService.live,
        TransactionDAO.live
      )
}
