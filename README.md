# java-quiz

Some small Java code examples for testing your Java knowledge.

At work we noticed that some developers didn't fully understand some
of the Java basics they were using day in day out. So we came up with
some code examples they could use to check their knowledge.

This repo is a spin-off. I'll add some comments and clojure code.

# AndOr

Build & run:

	~/java-quiz$ javac AndOr.java
	~/java-quiz$ java AndOr
	yep
	nope
	true
	yep
	true
	nope
	yep
	false
	nope
	false

## Background

The logical operators `||` and `&&` [2] are _short-circuit_ [1]
operators. So they're only evaluated until the overall result is
known. The bit-wise operators `|` and `&` are always evaluated.

Some developers use the bit-wise operators instead of the logical
operators accidentally. The code will compile and run in many cases.

[1] https://en.wikipedia.org/wiki/Short-circuit_evaluation

[2] https://docs.oracle.com/javase/tutorial/java/nutsandbolts/operators.html

In clojure there are no operators (and no operator precedences!). You
use `and` [3], `or`, `bit-and` and `bit-or`. The names are clear and
explicit. You won't confuse them.

[3] https://clojuredocs.org/clojure.core/and

## ArraySideeffect

Build & run:

	~/java-quiz$ javac ArraySideeffect.java
	~/java-quiz$ java ArraySideeffect
	[3, 2, 1]
	[3, 2, 1]
	[1, 2, 3]
	[1, 2, 3]
	[1, 2, 3]

## Background

Mutation of state (und thus side effects [1]) is ubiquitous in
Java. Here we first create a state sharing and then sort the
array. All variables refer to the same mutable array.

Many developers do not understand the difference between the
object/instance (the array in this case) and the reference to such an
object.

Here we introduce the `sort` method to further trip the reader: it
looks like a _pure_ _function_, but it just returns its mutable
argument --- well, thank's for that! This kind of error
(i.e. introducing side effects via state sharing between arguments and
return values) is done quite often by delevopers and hard to find.

In clojure you almost always use immutable data types (including
immutable collections --- called persistent collections). So there is
no danger of side effects.

[1] https://en.wikipedia.org/wiki/Side_effect_(computer_science)
