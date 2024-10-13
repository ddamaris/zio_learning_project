import dao.{AccountDAO, ClientDAO, Migrations, TransactionDAO}
import service.{AccountService, ClientService, TransactionService}
import zio.ZIO
import zio.test._

object AccountServiceSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment, Throwable] = {
    suite("AccountService")(
      suite("added accounts exist in db")(
        test("returns true confirming existence of added accounts") {
          for {
            clientService <- ZIO.environment[ClientService.Service].map(_.get)
            accService    <- ZIO.environment[AccountService.Service].map(_.get)
            cln <- clientService.create(
              "Cln_1_f_n",
              "Cln_1_s_n",
              "Cln_1_address"
            )
            acc    <- accService.create("deposit", cln.id)
            getAcc <- accService.get(acc.id)
          } yield assertTrue(getAcc.get == acc)
        },
        test("returns true confirming existence of many added accounts") {
          for {
            clientService <- ZIO.environment[ClientService.Service].map(_.get)
            accService    <- ZIO.environment[AccountService.Service].map(_.get)
            cl1 <- clientService.create(
              "Cln_2_f_n",
              "Cln_2_s_n",
              "Cln_2_address"
            )
            cl2 <- clientService.create(
              "Cln_3_f_n",
              "Cln_3_s_n",
              "Cln_3_address"
            )
            acc1 <- accService.create("deposit", cl1.id)
            acc2 <- accService.create("special", cl2.id)
            accs <- accService.getAll
          } yield assertTrue(accs.contains(acc1) && accs.contains(acc2))
        }
      ),
      suite("deleted accounts do not exist in db")(
        test("returns true confirming non-existence of deleted accounts") {
          for {
            clientService  <- ZIO.environment[ClientService.Service].map(_.get)
            accountService <- ZIO.environment[AccountService.Service].map(_.get)
            cln <- clientService.create(
              "Cln_4_f_n",
              "Cln_4_s_n",
              "Cln_4_address"
            )
            acc    <- accountService.create("special", cln.id)
            _      <- accountService.delete(acc.id)
            getAcc <- accountService.get(acc.id)
          } yield assertTrue(getAcc.isEmpty)
        }
      ),
      suite("updated accounts contain accurate information")(
        test("returns true confirming updated account information") {
          for {
            clientService  <- ZIO.environment[ClientService.Service].map(_.get)
            accountService <- ZIO.environment[AccountService.Service].map(_.get)
            cln <- clientService.create(
              "Cln_5_f_n",
              "Cln_5_s_n",
              "Cln_5_address"
            )
            acc    <- accountService.create("deposit", cln.id)
            _      <- accountService.update(acc.id, Some("special"), None)
            getAcc <- accountService.get(acc.id)
          } yield assertTrue(getAcc.get.kind == "special")
        }
      )
    )
  }.provideShared(
    AccountService.live,
    AccountDAO.live,
    ClientService.live,
    ClientDAO.live,
    TransactionService.live,
    TransactionDAO.live,
    TestContainerLayers.dataSourceLayer,
    Migrations.layer,
    Live.default
  )

}
