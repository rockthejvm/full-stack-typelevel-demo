package com.rockthejvm.livedemo

import cats.effect.*
import doobie.util.ExecutionContexts
import doobie.hikari.HikariTransactor
import com.comcast.ip4s.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.CORS

import com.rockthejvm.livedemo.core.*
import com.rockthejvm.livedemo.http.*

object Application extends IOApp.Simple {
  def makePostgres = for {
    ec <- ExecutionContexts.fixedThreadPool[IO](32)
    transactor <- HikariTransactor.newHikariTransactor[IO](
      "org.postgresql.Driver",
      "jdbc:postgresql://localhost:5444/",
      "docker",
      "docker",
      ec
    )
  } yield transactor

  def makeServer = for {
    postgres <- makePostgres
    jobs     <- JobsLive.resource[IO](postgres)
    jobApi   <- JobRoutes.resource[IO](jobs)
    server <- EmberServerBuilder
      .default[IO]
      .withHost(host"0.0.0.0")
      .withPort(port"4041")
      .withHttpApp(CORS(jobApi.routes.orNotFound))
      .build
  } yield server

  override def run: IO[Unit] =
    makeServer.use(_ => IO.println("Rock the JVM! Server ready.") *> IO.never)
}
