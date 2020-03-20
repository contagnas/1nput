package com.boredmage.onenput

import magnolia.{CaseClass, Magnolia, SealedTrait}

import scala.io.StdIn
import scala.language.experimental.macros

trait OnenputDerivation {
  type Typeclass[T] = Onenput[T]
  def combine[T](caseClass: CaseClass[Typeclass, T]): Typeclass[T] =
    (label: List[String], possibleValueSet: Option[Set[T]]) =>
      if (caseClass.parameters.isEmpty)
        Some(caseClass.rawConstruct(Nil))
      else {
        println(s"Building ${caseClass.typeName.short}")
        val (params, _) = caseClass.parameters.toList.foldLeft((List.empty[Any], possibleValueSet)) { case ((ps, pvs), p) =>
          val possibleParams = pvs.map(_.map(p.dereference))
          val param: p.PType = p.typeclass.retryPathedPrompt(p.label :: label, possibleParams)
          val newPvs = pvs.map(_.filter(v => p.dereference(v) == param))
          val newParams = param :: ps
          (newParams, newPvs)
        }

        Some(caseClass.rawConstruct(params.reverse))
      }

  def dispatch[T](sealedTrait: SealedTrait[Typeclass, T]): Typeclass[T] =
    (label: List[String], possibleValueSet: Option[Set[T]]) => {
      println(s"Selecting ${sealedTrait.typeName.short}")
      val subtypes = possibleValueSet.map { vs =>
        sealedTrait.subtypes.filter(s => vs.exists(v => s.cast.isDefinedAt(v)))
      }.getOrElse(sealedTrait.subtypes)

      val labelToSubtype = subtypes.map(s => s.typeName.short.toLowerCase() -> s).toMap
      val labels = subtypes.map(s => s.typeName.short)

      val labeledPrompt = if (label.nonEmpty)
        s" for ${label.reverse.mkString(".")}"
      else
        ""

      print(
        s"""|Select a type${labeledPrompt}: [${labels.mkString("|")}]
            |>>> """.stripMargin
      )
      val input = StdIn.readLine().toLowerCase
      println()
      labelToSubtype.get(input).flatMap { t =>
        val remainingPossibleValues = possibleValueSet.map(_.collect(t.cast))
        t.typeclass.pathedPrompt(label, remainingPossibleValues)
      }
    }

  implicit def gen[T]: Typeclass[T] = macro Magnolia.gen[T]
}
