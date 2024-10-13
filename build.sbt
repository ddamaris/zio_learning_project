scalaVersion := "2.13.13"
version := "0.1.0-SNAPSHOT"
organization := "com.my.project"
organizationName := "my.project"

name := "accounts"
scalacOptions += "-Ymacro-annotations"
libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.1.4",
  "dev.zio" %% "zio-macros" % "2.1.6",
  "dev.zio" %% "zio-config" % "4.0.2",
  "dev.zio" %% "zio-config-magnolia" % "4.0.2",
  "dev.zio" %% "zio-config-typesafe" % "4.0.2",
  "dev.zio" %% "zio-json" % "0.6.2",
  "dev.zio" %% "zio-sql-postgres" % "0.1.2",
  "dev.zio" %% "zio-test" % "2.1.5" % Test,
  "dev.zio" %% "zio-test-sbt" % "2.1.6",
  "io.d11" %% "zhttp" % "2.0.0-RC10",
  "io.getquill" %% "quill-jdbc-zio" % "4.8.3",
  "io.github.kitlangton" %% "zio-magic" % "0.3.12",

  "org.postgresql" % "postgresql" % "42.7.3",
  "org.flywaydb" % "flyway-core" % "10.15.2",
  "org.flywaydb" % "flyway-database-postgresql" % "10.14.0",
  "io.github.scottweaver" %% "zio-2-0-testcontainers-postgresql" % "0.10.0",
  "io.github.scottweaver" %% "zio-2-0-db-migration-aspect" % "0.10.0"
)
