package org.parser.combinator.example

import java.util.concurrent.Executors

import cats.effect.IO
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.typesafe.scalalogging.LazyLogging
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext

object StartServer extends App with LazyLogging {
  val blazeExec = Executors.newFixedThreadPool(5, new ThreadFactoryBuilder().setNameFormat("Blaze-Server-Processes").build())
  val blazeExecutionContext: ExecutionContext = ExecutionContext.fromExecutor(blazeExec)
  val server = BlazeBuilder[IO]
    .bindHttp(8080, "0.0.0.0")
    .withExecutionContext(blazeExecutionContext)
    .mountService(UnitsService.getUnitsService())
    .start.unsafeRunSync()
  server.awaitShutdown()
}
