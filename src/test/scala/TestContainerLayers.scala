import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import io.getquill.jdbczio.Quill
import zio._

import java.util.Properties
import javax.sql.DataSource
import scala.jdk.CollectionConverters.MapHasAsJava

object TestContainerLayers {

//    val dataSourceLayer: ZLayer[Any, Throwable, DataSource] = {
//      val localDBConfig = Map[String, String](
//        "dataSource.user"     -> "postgres",
//        "dataSource.password" -> "admin",
//        "dataSource.url" -> "jdbc:postgresql://localhost:5432/postgres?currentSchema=otus_test",
//        "dataSourceClassName" -> "org.postgresql.ds.PGSimpleDataSource"
//      ).asJava
//      val config = ConfigFactory.parseMap(localDBConfig)
//      Quill.DataSource.fromConfig(config)
//    }

  val dataSourceLayer: ZLayer[Any, Nothing, DataSource] = ZLayer {
      ZIO.attemptBlocking(unsafeDataSourceFromJdbcInfo).orDie
  }

  private def unsafeDataSourceFromJdbcInfo: DataSource = {
    val props = new Properties()
    props.putAll(
      Map(
        "dataSource.user" -> "postgres",
        "dataSource.password" -> "admin",
        "dataSource.url" -> "jdbc:postgresql://localhost:5432/postgres?currentSchema=otus",
        "dataSourceClassName" -> "org.postgresql.ds.PGSimpleDataSource").asJava
    )
    new HikariDataSource(new HikariConfig(props))
  }
}
