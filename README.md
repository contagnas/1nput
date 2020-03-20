# 1nput
Prompt for ADT values 1 input at a time

## Example
Represent a deck of cards as an ADT:

```scala
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
```

Now prompt the user for a card.

```scala
val card = Onenput[Card].prompt()
println(card)
```

The `Onenput[Card]` instance is derived using Magnolia. The user is prompted for
values one at a time (`>>>` is the prompt, everything else is informational):

Output:

```
Building Card
Selecting Rank
Select a type for rank: [Ace|Eight|Five|Four|Jack|King|Nine|Queen|Seven|Six|Ten|Three|Two]
>>> Ace

Selecting Suit
Select a type for suit: [Clubs|Diamonds|Hearts|Spades]
>>> Spades

Card(Ace,Spades)
```

Since Magnolia supports recursive ADTs, and `List` is an ADT, it works out of
the box:

```scala
println(Onenput[List[Card]].prompt())
```

Output:

```
Selecting List
Select a type: [::|Nil]
>>> ::

Building ::
Building Card
Selecting Rank
Select a type for head.rank: [Ace|Eight|Five|Four|Jack|King|Nine|Queen|Seven|Six|Ten|Three|Two]
>>> ace

Selecting Suit
Select a type for head.suit: [Clubs|Diamonds|Hearts|Spades]
>>> clubs

Selecting List
Select a type for next: [::|Nil]
>>> ::

Building ::
Building Card
Selecting Rank
Select a type for next.head.rank: [Ace|Eight|Five|Four|Jack|King|Nine|Queen|Seven|Six|Ten|Three|Two]
>>> ace

Selecting Suit
Select a type for next.head.suit: [Clubs|Diamonds|Hearts|Spades]
>>> spades

Selecting List
Select a type for next.next [::|Nil]
>>> nil

List(Card(Ace,Clubs), Card(Ace,Spades))
```

## Restraining inputs
`prompt` takes an optional parameter, which allows you to restrict what will be
prompted and parsed. As more information is entered, more and more prompts will
be filtered out.

```scala
val hands = Set(
  List(Card(Two, Hearts), Card(Three, Clubs)),
  List(Card(Two, Clubs), Card(Five, Spades))
)
println(Onenput[List[Card]].prompt(Some(hands)))
```

Output:

```
Selecting List
Select a type: [::]
>>> ::

Building ::
Building Card
Selecting Rank
Select a type for head.rank: [Two]
>>> two

Selecting Suit
Select a type for head.suit: [Clubs|Hearts]
>>> hearts

Selecting List
Select a type for next: [::]
>>> ::

Building ::
Building Card
Selecting Rank
Select a type for next.head.rank: [Three]
>>> three

Selecting Suit
Select a type for next.head.suit: [Clubs]
>>> clubs

Selecting List
Select a type for next.next: [Nil]
>>> nil

List(Card(Two,Hearts), Card(Three,Clubs))
```
