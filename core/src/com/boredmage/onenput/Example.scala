package com.boredmage.onenput

sealed trait Suit
case object Hearts extends Suit
case object Diamonds extends Suit
case object Spades extends Suit
case object Clubs extends Suit

sealed trait Rank
case object Two extends Rank
case object Three extends Rank
case object Four extends Rank
case object Five extends Rank
case object Six extends Rank
case object Seven extends Rank
case object Eight extends Rank
case object Nine extends Rank
case object Ten extends Rank
case object Jack extends Rank
case object Queen extends Rank
case object King extends Rank
case object Ace extends Rank

case class Card(rank: Rank, suit: Suit)

object Example extends App {
  println(Onenput[List[Card]].prompt(Some(Set(
    List(Card(Queen, Diamonds)),
    List(Card(King, Spades), Card(Two, Clubs))
  ))))
}
