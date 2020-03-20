package com.boredmage.onenput

import org.scalatest.flatspec._
import org.scalatest.matchers.should.Matchers

class EnumerableTest extends AnyFlatSpec with Matchers {

  "Enumerable" should "work for Booleans" in {
    Enumerable[Boolean].values should contain theSameElementsAs List(true, false)
  }

  it should "work for option" in {
    Enumerable[Option[Option[Option[Unit]]]].values should contain theSameElementsAs List(
      None, Some(None), Some(Some(None)), Some(Some(Some(())))
    )
  }

  it should "work for case classes of enumerables" in {
    case class TwoBooleans(a: Boolean, b: Boolean)
    Enumerable[TwoBooleans].values should contain theSameElementsAs List(
      TwoBooleans(true, true),
      TwoBooleans(false, true),
      TwoBooleans(true, false),
      TwoBooleans(false, false)
    )
  }

  it should "work for sealed traits of enumerables" in {
    sealed trait MyUnion
    case class Booleans(b1: Boolean, b2: Boolean, u: Unit) extends MyUnion
    case class OptionThing(o: Option[Option[Option[Unit]]]) extends MyUnion

    Enumerable[MyUnion].values should have length {
      Enumerable[Booleans].values.length + Enumerable[OptionThing].values.length
    }
  }

  it should "Work for objects" in {
    case object Object
    Enumerable[Object.type].values should have length 1
  }

  it should "work for sealed traits of objects" in {
    sealed trait Objects
    case object O1 extends Objects
    case object O2 extends Objects
    case object O3 extends Objects
    Enumerable[Objects].values should contain theSameElementsAs List(O1, O2, O3)
  }

  it should "work for value class" in {
    case class U(u: Unit)
    Enumerable[U].values should contain theSameElementsAs List(U(()))
  }

  it should "return empty for case class with Nothing arg" in {
    case class NoElements(n: Nothing)
//     Enumerable.gen[NoElements] should have length 0 // doesn't compile :(
  }
}
