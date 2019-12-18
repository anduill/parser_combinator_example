package org.parser.combinator.example

import io.circe.Json

import scala.util.Try
import scala.util.matching.Regex
import scala.util.parsing.combinator._
import scala.math.BigDecimal._
object UnitsParser extends JavaTokenParsers with PackratParsers {

  sealed trait UnitOperator
  object Multiplication extends UnitOperator
  object Division extends UnitOperator

  sealed trait Unit {
    def multFactor(): BigDecimal
    def unitName(): String

  }
  case class CompoundUnit(u1: Unit, u2: Unit, operator: UnitOperator) extends Unit{
    override def multFactor(): BigDecimal = {
      operator match {
        case Multiplication => u1.multFactor() * u2.multFactor()
        case Division => u1.multFactor() / u2.multFactor()
      }
    }
    override def unitName(): String = {
      operator match {
        case Multiplication => s"${u1.unitName()}*${u2.unitName()}"
        case Division => s"${u1.unitName()}/${u2.unitName()}"
      }
    }
  }
  case class ParenUnit(u: Unit) extends Unit {
    override def multFactor(): BigDecimal = u.multFactor()

    override def unitName(): String = s"(${u.unitName()})"
  }
  sealed trait BaseUnit extends Unit{
    def name(): String
    def symbol(): String
    def regexStr(): String = s"""(${name()}|${symbol()})"""
    def regex(): Regex = regexStr().r
  }
  object Minute extends BaseUnit {
    override def name(): String = "minute"
    override def symbol(): String = "min"
    override def multFactor(): BigDecimal = 60.0
    override def unitName(): String = "s"
  }
  object Hour extends BaseUnit {
    override def name(): String = "hour"
    override def symbol(): String = "h"
    override def multFactor(): BigDecimal = 3600.0
    override def unitName(): String = "s"
  }
  object Day extends BaseUnit {
    override def name(): String = "day"
    override def symbol(): String = "d"
    override def multFactor(): BigDecimal = 86400.0
    override def unitName(): String = "s"
  }
  object Degree extends BaseUnit {
    override def name(): String = "degree"
    override def symbol(): String = "°"
    override def multFactor(): BigDecimal = Math.PI / 180.0
    override def unitName(): String = "rad"
  }
  object ArcMinute extends BaseUnit {
    override def name(): String = "arcminute"
    override def symbol(): String = "'"
    override def multFactor(): BigDecimal = Math.PI / 10800.0
    override def unitName(): String = "rad"
  }
  object ArcSecond extends BaseUnit {
    override def name(): String = "arcsecond"
    override def symbol(): String = "\""
    override def multFactor(): BigDecimal = Math.PI / 648000.0
    override def unitName(): String = "rad"
  }
  object Hectare extends BaseUnit {
    override def name(): String = "hectare"
    override def symbol(): String = "ha"
    override def multFactor(): BigDecimal = 10000.0
    override def unitName(): String = "m^2"
  }
  object Litre extends BaseUnit {
    override def name(): String = "litre"
    override def symbol(): String = "L"
    override def multFactor(): BigDecimal = 0.001
    override def unitName(): String = "m^3"
  }
  object Tonne extends BaseUnit {
    override def name(): String = "tonne"
    override def symbol(): String = "t"
    override def multFactor(): BigDecimal = 1000.0
    override def unitName(): String = "kg"
  }

  lazy val baseUnitParser: PackratParser[Unit] = ( Minute.regex() | Hectare.regex() | Hour.regex() | Degree.regex() | Day.regex() | ArcMinute.regex() | ArcSecond.regex() | Litre.regex() | Tonne.regex() ) ^^ {
    case x if x.matches(Minute.regexStr())=> Minute
    case x if x.matches(Hour.regexStr()) => Hour
    case x if x.matches(Day.regexStr()) => Day
    case x if x.matches(Degree.regexStr()) => Degree
    case x if x.matches(ArcMinute.regexStr()) => ArcMinute
    case x if x.matches(ArcSecond.regexStr()) => ArcSecond
    case x if x.matches(Hectare.regexStr()) => Hectare
    case x if x.matches(Litre.regexStr()) => Litre
    case x if x.matches(Tonne.regexStr()) => Tonne
  }

  lazy val operatorParser : PackratParser[UnitOperator] = ("*" | "/") ^^ {
    case "*" => Multiplication
    case "/" => Division
  }

  lazy val expr: PackratParser[Unit] = expr ~ operatorParser ~ term  ^^ {
    case un1 ~ op ~ un2 =>
      CompoundUnit(un1.asInstanceOf[Unit],un2.asInstanceOf[Unit], op.asInstanceOf[UnitOperator])
    case c =>
      c.asInstanceOf[CompoundUnit]
  } | term ~ operatorParser ~ term ^^ {
    case un1 ~ op ~ un2 =>
      CompoundUnit(un1.asInstanceOf[Unit],un2.asInstanceOf[Unit], op.asInstanceOf[UnitOperator])
    case c =>
      c.asInstanceOf[CompoundUnit]
  }
  lazy val term: PackratParser[Unit] = ("(" ~ expr ~ ")" | baseUnitParser) ^^ {
    case p1 ~ un ~ p2 => ParenUnit(un.asInstanceOf[Unit])
    case un =>
      un.asInstanceOf[Unit]
  }

  lazy val generalUnitParser: PackratParser[Unit] = (expr | term) ^^ {
    case u => u
  }

  def convertUnits(input: String): Try[Unit] = {
    Try{
      val res = parseAll(generalUnitParser, input)
      res.get
    }
  }
}
