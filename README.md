# java-quiz

Some small Java code examples for testing your Java knowledge.

At work we noticed that some developers didn't fully understand some
of the Java basics they were using day in day out. So we came up with
some code examples they could use to check their knowledge. We don't
use these for job interviews yet.

This repo is a spin-off. I'll add some comments and Clojure [1]
code. Clojure is a LISP-like functional JVM language. I love Java but
I believe it has some properties that make it hard to get right. So
I'll say a little about why certain error/bugs are less likely in
Clojure than in Java.

[1] https://clojure.org/

-------------------------------------------------------------------

# AndOr

    import static java.lang.System.*;

    class AndOr {

        static boolean yep() {

            out.println("yep");
            return true;

        }

        static boolean nope() {

            out.println("nope");
            return false;

        }

        public static void main(String... args) {

            out.println(yep() | nope());
            out.println(yep() || nope());
            out.println(nope() & yep());
            out.println(nope() && yep());

        }
    }

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
operators. So they're only evaluated (i.e. their operands are
evaluated) until the overall result is known. The bit-wise operators
`|` and `&` are always evaluated completely.

Some developers __accidentally__ use the bit-wise operators instead of
the logical operators. The code will compile and run in many cases and
it will even (kind of) _work_. But `(x != null) && (x.foo())` is
different from `(x != null) & (x.foo())`.

In Clojure there are no operators (and no operator precedence!). You
use `and` [3], `or` (both with _short-circuit_ semantics), `bit-and`
and `bit-or`. The names are clear and explicit. You won't confuse
them.

[1] https://en.wikipedia.org/wiki/Short-circuit_evaluation  
[2] https://docs.oracle.com/javase/tutorial/java/nutsandbolts/operators.html  
[3] https://clojuredocs.org/clojure.core/and  

-------------------------------------------------------------------

## ArraySideeffect

    import static java.lang.System.*;
    import java.util.Arrays;

    class ArraySideeffect {

        static int[] sort(int[] x) {
        
            Arrays.sort(x);
            return x;
            
        }

        public static void main(String... args) {

            final int[] i = { 3, 2, 1 };
            final int[] k = i;

            out.println(Arrays.toString(i));
            out.println(Arrays.toString(k));

            final int[] j = sort(i);

            out.println(Arrays.toString(i));
            out.println(Arrays.toString(j));
            out.println(Arrays.toString(k));
            
        }
    }

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
Java. Here we first create a state sharing and then `sort` the
array. All variables refer to the __same__ mutable array.

Many developers do not understand the difference between the
object/instance (the array in this case) and the reference(s) to such
an object. Java uses __by value__ semantics but this description leads
some developers to believe that `sort(i)` will pass the
object/value/array to the method and not just the reference to that
object/value/array.

Here we introduce the `sort` method to further trick the reader: it
looks like a _pure_ _function_, but it just returns its argument
(i.e. the reference to the mutable array) --- well, thank's for that!
This kind of error (i.e. introducing side effects via state sharing
between arguments and return values) is done quite often by delevopers
unintentionally and hard to find.

In Clojure you almost always use immutable data types (including
immutable collections --- called _persistent_ _data_ _structures_
[2]). So there is no danger of side effects.

Note: Clojure can use Java's mutable arrays directly and in this case
you get uncontrolled state sharing and side effects in Clojure as
well.

[1] https://en.wikipedia.org/wiki/Side_effect_(computer_science)  
[2] https://clojure.org/reference/data_structures
[3] http://javadude.com/articles/passbyvalue.htm

## More on mutable state

So many hard problem are introduced through mutable shared state:

* side effects

* the need for defensive copies and cloning

* race conditions and the need for synchronization in multi-threaded
  programms (and the need to understand chapter 17 of the JLS [1])

With Java 8 we have _functional_ closures/lamdas/streams: given
mutable state all this will lead to more broken code.

[1] https://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html

-------------------------------------------------------------------

