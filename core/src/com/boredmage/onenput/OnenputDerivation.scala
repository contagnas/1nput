package com.boredmage.onenput

import magnolia.{CaseClass, Magnolia, SealedTrait}
import cats.implicits._

import scala.io.StdIn
import scala.language.experimental.macros

trait OnenputDerivation {
  type Typeclass[T] = Onenput[T]
  def combine[T](caseClass: CaseClass[Typeclass, T]): Typeclass[T] = new Typeclass[T] {
    override def pathedPrompt(label: List[String] = Nil): Option[T] =
      if (caseClass.parameters.isEmpty)
        Some(caseClass.rawConstruct(Nil))
      else {
        val params = caseClass.parameters.toList.map { p =>
          p.typeclass.retryPathedPrompt(p.label :: label)
        }
        Some(caseClass.rawConstruct(params))
      }
  }

  def dispatch[T](sealedTrait: SealedTrait[Typeclass, T]): Typeclass[T] = new Typeclass[T] {
    override def pathedPrompt(label: List[String]): Option[T] = {
      val labelToSubtype = sealedTrait.subtypes.map(s => s.typeName.short.toLowerCase() -> s).toMap
      val labels = sealedTrait.subtypes.map(s => s.typeName.short)

      val labeledPrompt = if (label.nonEmpty)
        s" for ${label.reverse.mkString(".")}"
      else
        ""

      print(
        s"""
           |Select a type${labeledPrompt}: [${labels.mkString("|")}]
           |>>> """.stripMargin
      )
      val input = StdIn.readLine().toLowerCase
      labelToSubtype.get(input).flatMap(_.typeclass.pathedPrompt(label))
    }
  }

  implicit def gen[T]: Typeclass[T] = macro Magnolia.gen[T]
}
