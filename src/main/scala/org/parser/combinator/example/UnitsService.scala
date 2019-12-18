package org.parser.combinator.example

import java.math.MathContext

import cats.effect.IO
import org.http4s.HttpService
import io.circe.Json
import io.circe.parser._
import io.circe.syntax._
import org.http4s._
import io.circe.generic.auto._
import org.http4s.circe._
import org.http4s.dsl.impl.Root
import org.http4s.dsl.io._

import scala.math.BigDecimal
import scala.util.Try

object UnitsParam extends OptionalQueryParamDecoderMatcher[String]("units")
object UnitsService {
  def getUnitsService(): HttpService[IO] = {
    val convertFunc: String => Try[UnitsParser.Unit] = UnitsParser.convertUnits

      HttpService[IO] {
      case req @ GET -> Root / "units" / "si" :? UnitsParam(unitString) =>
        unitString match {
          case None =>
            BadRequest(Json.obj("message" -> " 'units' REST parameter is missing.".asJson))
          case Some(ustring) =>
            convertFunc(ustring) match {
              case scala.util.Failure(exception) =>
                BadRequest(Json.obj("message" -> s"Parsing failure for unit string ${ustring}".asJson,
                "exception" -> s"${exception.getMessage}".asJson))
              case scala.util.Success(value) =>
                Ok(Json.obj(
                  "unit_name" -> s"${value.unitName()}".asJson,
                  "multiplication_factor" -> Json.fromBigDecimal(value.multFactor()(new MathContext(14)))))//TODO: there may be a better way to ensure that only 14 digits are used in the JSON output.  I ran out of time debugging this.
            }
        }
    }
  }

}
