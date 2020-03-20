package com.boredmage.onenput

trait Onenput[A] {
  def prompt(possibleValueSet: Option[Set[A]] = None): A = retryPathedPrompt(Nil, possibleValueSet)

  def pathedPrompt(path: List[String], possibleValueSet: Option[Set[A]]): Option[A]
  def retryPathedPrompt(path: List[String], possibleValueSet: Option[Set[A]]): A = {
    var a: Option[A] = None
    while (a.isEmpty) a = pathedPrompt(path, possibleValueSet)
    a.get
  }
}

object Onenput extends OnenputDerivation {
  def apply[A: Onenput]: Onenput[A] = implicitly[Onenput[A]]
}
