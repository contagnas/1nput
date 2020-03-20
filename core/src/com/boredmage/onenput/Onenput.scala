package com.boredmage.onenput

trait Onenput[A] {
  def prompt(possibleValueSet: Option[Set[A]] = None): A = retryPathedPrompt(Nil)

  def pathedPrompt(path: List[String]): Option[A]
  def retryPathedPrompt(path: List[String]): A = {
    var a: Option[A] = None
    while (a.isEmpty) a = pathedPrompt(path)
    a.get
  }
}

object Onenput extends OnenputDerivation {
  def apply[A: Onenput]: Onenput[A] = implicitly[Onenput[A]]
}
