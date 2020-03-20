package com.boredmage.onenput

trait Onenput[A] {
  def prompt(): Option[A] = pathedPrompt(Nil)
  def pathedPrompt(path: List[String]): Option[A]
}

object Onenput extends OnenputDerivation {
  def apply[A: Onenput]: Onenput[A] = implicitly[Onenput[A]]
}
