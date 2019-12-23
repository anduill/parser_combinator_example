package org.parser.combinator.example

import java.math.MathContext

import cats.effect.IO
import org.http4s.{HttpService, Request, Response, Status, _}
import org.scalatest.{FlatSpec, Matchers}
import io.circe.Json
import io.circe.parser._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.io._
import io.circe.generic.auto._
import org.parser.combinator.example.UnitsParser.{Day, Degree, Hectare, Minute}

import scala.math.BigDecimal

class UnitsServiceSpec extends FlatSpec with Matchers {

  "simple service call 1" should "succeed" in {
    val service: HttpService[IO] = UnitsService.getUnitsService()
    val request = Request[IO](Method.GET, uri = Uri.uri("/units/si?units=(degree/min)"))
    val actualResponse = unpackJsonResponse(service(request).value.unsafeRunSync().get)
    val expectedResponse = Json.obj("unit_name" -> "(rad/s)".asJson,
    "multiplication_factor" -> Json.fromBigDecimal((Degree.multFactor() / Minute.multFactor())((new MathContext(14)))))
    actualResponse shouldBe expectedResponse
  }

  "simple service call 2" should "succeed" in {
    val service: HttpService[IO] = UnitsService.getUnitsService()
    val request = Request[IO](Method.GET, uri = Uri.uri("/units/si?units=(degree/(minute*hectare))"))
    val actualResponse = unpackJsonResponse(service(request).value.unsafeRunSync().get)
    val expectedResponse = Json.obj("unit_name" -> "(rad/(s*m^2))".asJson,
      "multiplication_factor" -> Json.fromBigDecimal((Degree.multFactor() / (Minute.multFactor() * Hectare.multFactor()))(new MathContext(14))))
    actualResponse shouldBe expectedResponse
  }

  "simple service call 3" should "succeed" in {
    val service: HttpService[IO] = UnitsService.getUnitsService()
    val request = Request[IO](Method.GET, uri = Uri.uri("/units/si?units=(day*day*day)"))
    val actualResponse = unpackJsonResponse(service(request).value.unsafeRunSync().get)
    val expectedResponse = Json.obj("unit_name" -> "(s*s*s)".asJson,
      "multiplication_factor" -> Json.fromBigDecimal((Day.multFactor() * Day.multFactor() * Day.multFactor())(new MathContext(14))))
    actualResponse shouldBe expectedResponse
  }

  // This can extract the JSON object out of the Response
  def unpackJsonResponse(response: Response[IO])(implicit decoder: EntityDecoder[IO, String]): Json = {
    import io.circe.parser._
    val result = decoder.decode(response, false)
    parse(result.value.unsafeRunSync().right.get).right.get
  }
}
