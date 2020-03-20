package com.boredmage.onenput

import magnolia.{CaseClass, Magnolia, SealedTrait}
import cats.implicits._

import scala.io.StdIn
import scala.language.experimental.macros

trait OnenputDerivation {
  type Typeclass[T] = Onenput[T]
  def combine[T](caseClass: CaseClass[Typeclass, T]): Typeclass[T] =
    (label: List[String], possibleValueSet: Option[Set[T]]) =>
      if (caseClass.parameters.isEmpty)
        Some(caseClass.rawConstruct(Nil))
      else {
        val params = caseClass.parameters.toList.map { p =>
          val possibleParams = possibleValueSet.map(_.map(p.dereference))
          p.typeclass.retryPathedPrompt(p.label :: label, possibleParams)
        }
        Some(caseClass.rawConstruct(params))
      }

  def dispatch[T](sealedTrait: SealedTrait[Typeclass, T]): Typeclass[T] =
    (label: List[String], possibleValueSet: Option[Set[T]]) => {
      val subtypes = possibleValueSet.map { vs =>
        vs.map(v => sealedTrait.subtypes.find(_.cast.isDefinedAt(v)).get)
      }.getOrElse(sealedTrait.subtypes)

      val labelToSubtype = subtypes.map(s => s.typeName.short.toLowerCase() -> s).toMap
      val labels = subtypes.map(s => s.typeName.short)

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
      labelToSubtype.get(input).flatMap { t =>
        val remainingPossibleValues = possibleValueSet.map(_.collect(t.cast))
        t.typeclass.pathedPrompt(label, remainingPossibleValues)
      }
    }

  implicit def gen[T]: Typeclass[T] = macro Magnolia.gen[T]
}
