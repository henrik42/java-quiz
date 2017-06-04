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

[1] https://en.wikipedia.org/wiki/Short-circuit_evaluation

[2] https://docs.oracle.com/javase/tutorial/java/nutsandbolts/operators.html
