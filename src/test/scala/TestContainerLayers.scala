import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import zio._

import java.util.Properties
import javax.sql.DataSource
import scala.jdk.CollectionConverters.MapHasAsJava

object TestContainerLayers {

  val dataSourceLayer: ZLayer[Any, Nothing, DataSource] = ZLayer {
      ZIO.attemptBlocking(unsafeDataSourceFromJdbcInfo).orDie
  }

  private def unsafeDataSourceFromJdbcInfo: DataSource = {
    val props = new Properties()
    props.putAll(
      Map(
        "dataSource.user" -> "postgres",
        "dataSource.password" -> "admin",
        "dataSource.url" -> "jdbc:postgresql://localhost:5432/postgres?currentSchema=otus_test",
        "dataSourceClassName" -> "org.postgresql.ds.PGSimpleDataSource").asJava
    )
    new HikariDataSource(new HikariConfig(props))
  }
}
