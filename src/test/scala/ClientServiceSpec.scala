import dao.{AccountDAO, ClientDAO, Migrations}
import zio.{Scope, ZIO}
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault}
import service.{AccountService, ClientService}
import zio.test._

object ClientServiceSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = {
    suite("ClientService")(
      suite("added client exist in db")(
        test("returns true confirming existence of many added persons") {
          for {
            clientService <- ZIO.environment[ClientService.Service].map(_.get)
            cl1 <- clientService.create(
              "Cln_7_f_n",
              "Cln_7_s_n",
              "Cln_7_address"
            )
            cl2 <- clientService.create(
              "Cln_8_f_n",
              "Cln_8_s_n",
              "Cln_8_address"
            )
            clns <- clientService.getAll
          } yield assertTrue(clns.contains(cl1) && clns.contains(cl2))
        }
      ),
      suite("deleted persons do not exist in db")(
        test("returns false confirming non-existence of deleted person") {
          for {
            clientService <- ZIO.environment[ClientService.Service].map(_.get)
            clnt <- clientService.create(
              "Cln_10_f_n",
              "Cln_10_s_n",
              "Cln_10_address"
            )
            _         <- clientService.delete(clnt.id)
            getClient <- clientService.get(clnt.id)
          } yield assertTrue(getClient.isEmpty)
        }
      ),
      suite("updated persons contain accurate information")(
        test("returns true confirming updated person information") {
          for {
            clientService <- ZIO.environment[ClientService.Service].map(_.get)
            cl <- clientService.create(
              "Cln_11_f_n",
              "Cln_11_s_n",
              "Cln_11_address"
            )
            _     <- clientService.update(cl.id, None, None, Some("Blablabla"))
            getCl <- clientService.get(cl.id)
          } yield assertTrue(
            getCl.get.firstName == "Cln_11_f_n" && getCl.get.address == "Blablabla"
          )
        }
      )
    )
  }.provideShared(
    ClientService.live,
    ClientDAO.live,
    Migrations.layer,
    TestContainerLayers.dataSourceLayer
  )
}
