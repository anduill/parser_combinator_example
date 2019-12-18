package org.parser.combinator.example

import org.scalatest.{Assertion, FlatSpec, Matchers}
import UnitsParser._

class UnitsParserSpec extends FlatSpec with Matchers  {

  "parsing test one" should "succeed" in {
    val test1Str = "degree/min"
    val actualParse = convertUnits(test1Str).get
    val expectedParse = CompoundUnit(Degree, Minute, Division)
    val expectedMultFact = Degree.multFactor() / Minute.multFactor()
    val expectedUnitName = "rad/s"
    testExpected(actualParse, expectedParse, expectedMultFact, expectedUnitName)
  }

  "parsing test two" should "succeed" in {
    val test1Str = "(degree/min)"
    val actualParse = convertUnits(test1Str).get
    val expectedParse = ParenUnit(CompoundUnit(Degree, Minute, Division))
    val expectedMultFact = Degree.multFactor() / Minute.multFactor()
    val expectedUnitName = "(rad/s)"
    testExpected(actualParse, expectedParse, expectedMultFact, expectedUnitName)
  }

  "parsing test three" should "succeed" in {
    val test1Str = "(degree/(minute*hectare))"
    val actualParse = convertUnits(test1Str).get
    val expectedParse = ParenUnit(CompoundUnit(Degree, ParenUnit(CompoundUnit(Minute, Hectare, Multiplication)), Division))
    val expectedMultFact = Degree.multFactor() / (Minute.multFactor() * Hectare.multFactor())
    val expectedUnitName = "(rad/(s*m^2))"
    testExpected(actualParse, expectedParse, expectedMultFact, expectedUnitName)
  }

  "parsing test four" should "succeed" in {
    val test1Str = "ha*°"
    val actualParse = convertUnits(test1Str).get
    val expectedParse = CompoundUnit(Hectare, Degree, Multiplication)
    val expectedMultFact = Hectare.multFactor() * Degree.multFactor()
    val expectedUnitName = "m^2*rad"
    testExpected(actualParse, expectedParse, expectedMultFact, expectedUnitName)
  }

  "parsing test five" should "succeed" in {
    val test1Str = "degree"
    val actualParse = convertUnits(test1Str).get
    val expectedParse = Degree
    val expectedMultFact = Degree.multFactor()
    val expectedUnitName = "rad"
    testExpected(actualParse, expectedParse, expectedMultFact, expectedUnitName)
  }

  "parsing test six" should "succeed" in {
    val test1Str = "day*day*day"
    val actualParse = convertUnits(test1Str).get
    val expectedParse = CompoundUnit(CompoundUnit(Day, Day, Multiplication), Day, Multiplication)
    val expectedMultFact = Day.multFactor() * Day.multFactor() * Day.multFactor()
    val expectedUnitName = "s*s*s"
    testExpected(actualParse, expectedParse, expectedMultFact, expectedUnitName)
  }
  "bad string" should "fail" in {
    val test1Str = "day*day*day)"
    val actualParse = convertUnits(test1Str)
    actualParse match {
      case util.Success(_) =>
        throw new Exception(s"parsing should fail for string $test1Str")
      case util.Failure(_) =>
        //
    }
  }

  def testExpected(actualParse: Unit, expectedParse: Unit, expectedMultFact: BigDecimal, expectedUnitName: String): Assertion = {
    actualParse shouldBe expectedParse
    expectedMultFact shouldBe actualParse.multFactor()
    expectedUnitName shouldBe actualParse.unitName()
  }
}
