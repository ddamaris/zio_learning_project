package utils
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariDataSource
import io.getquill.context.ZioJdbc
import io.getquill.jdbczio.Quill
import io.getquill.util.LoadConfig
import io.getquill.{JdbcContextConfig, PostgresZioJdbcContext, SnakeCase}
import zio.{ULayer, ZLayer}

import javax.sql.DataSource
import scala.jdk.CollectionConverters.MapHasAsJava

object QuillContext extends PostgresZioJdbcContext(SnakeCase) {
  def ds: HikariDataSource = JdbcContextConfig(LoadConfig("db")).dataSource
  val dataSourceLayer: ZLayer[Any, Throwable, DataSource] = Quill.DataSource.fromDataSource(ds)


//  val dataSourceLayer: ZLayer[Any, Throwable, DataSource] = {
//    val localDBConfig = Map[String, String](
//      "dataSource.user"     -> "postgres",
//      "dataSource.password" -> "admin",
//      "dataSource.url" -> "jdbc:postgresql://localhost:5432/postgres?currentSchema=otus",
//      "dataSourceClassName" -> "org.postgresql.ds.PGSimpleDataSource"
//    ).asJava
//    val config = ConfigFactory.parseMap(localDBConfig)
//    Quill.DataSource.fromConfig(config)
//  }
}
