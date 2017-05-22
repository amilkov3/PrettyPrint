package io.voklim.prettyprint.impl

import io.voklim.test._
import org.scalacheck._

class PrettyPrintTest extends UnitPropertySpec {

  property("should correctly encode case classes to String via the pprint typeclass"){
    forAll{ foo: Foo =>
      foo.pprint.replaceAll(" ", "") shouldEqual foo.toPrettyPrintString.replaceAll(" ", "")
    }
  }

  case class Foo(x: Option[Boolean], bar: Bar) {
    val toPrettyPrintString = {
      s"""
        |io.voklim.prettyprint.impl.PrettyPrintTest$$Foo: (
        |x = ${x.toString},
        |bar = ${bar.toPrettyPrintString}
        |)
      """.stripMargin.replaceAll("\n", "")
    }
  }
  case class Bar(y: Double, z: String) {
    val toPrettyPrintString = s"io.voklim.prettyprint.impl.PrettyPrintTest$$Bar: (y = $y, z = '$z')"
  }

  implicit lazy val arbitaryFoo: Arbitrary[Foo] = Arbitrary(for {
    boolOpt <- Arbitrary.arbitrary[Option[Boolean]]
    bar <- Arbitrary.arbitrary[Bar]
  } yield Foo(boolOpt, bar))

  implicit lazy val arbitaryBar: Arbitrary[Bar] = Arbitrary(for {
    d <- Arbitrary.arbitrary[Double]
    s <- Arbitrary.arbitrary[String]
  } yield Bar(d, s))
}
