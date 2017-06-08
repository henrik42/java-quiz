# java-quiz

Some small Java code examples for testing your Java knowledge.

At work we noticed that some developers didn't fully understand some
of the Java basics they were using day in day out. So we came up with
some code examples they could use to check their knowledge. We don't
use these for job interviews yet.

I'll add some comments and Clojure [1] code. Clojure is a LISP-like
functional JVM language. I love Java but I believe it has some
properties that make it hard to get right. So I'll say a little about
why certain error/bugs are less likely in Clojure than in Java.

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

# ArraySideeffect

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
an object. Java uses __by value__ semantics [3] but this description
leads some developers to believe that `sort(i)` will pass the
object/value/array to the method and not just the reference to that
object/value/array.

Here we introduce the `sort` method to further trick the reader: it
looks like a _pure function_, but it just returns its argument
(i.e. the reference to the mutable array) -- well, thank's for that!
This kind of error (i.e. introducing side effects via state sharing
between arguments and return values) is done quite often by delevopers
unintentionally and hard to find.

In Clojure you almost always use immutable data types (including
immutable collections -- called _persistent data structures_ [2]). So
there is no danger of side effects.

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

# Autoboxing

    import static java.lang.System.*;

    class Autoboxing {

        public static void main(String... args) {

            Integer a = 42, b = 42;

            out.println("a == b      : " + (a == b));
            out.println("a == 42     : " + (a == 42));
            out.println("a.equals(b) : " + (a.equals(b)));

            Integer c = 666, d = 666;

            out.println("c == d      : " + (c == d));
            out.println("c == 666    : " + (c == 666));
            out.println("c.equals(d) : " + (c.equals(d)));

            Boolean t = true, s = true;

            out.println("t == s      : " + (t == s));
            out.println("t == true   : " + (t == true));
            out.println("c.equals(d) : " + (c.equals(d)));

        }
    }

Build & run:

    ~/java-quiz$ javac Autoboxing.java
    ~/java-quiz$ java Autoboxing 
    a == b      : true
    a == 42     : true
    a.equals(b) : true
    c == d      : false
    c == 666    : true
    c.equals(d) : true
    t == s      : true
    t == true   : true
    c.equals(d) : true

## Background

Comparing `a == b` and `c == d` is done with no auto-unboxing: this
just compares references. `a` and `b` reference __the same cached
object__ because they are both auto-boxed from `42` via
`Integer.valueOf(int)` [1]. So they're identical. On my JVM the
auto-boxed values for `666` are not cached in `Integer` so `c` and `d`
reference two different instances.

Comparing `a == 42` will auto-unbox `a` and compare the two (native)
`int` (not `Integer`) values.

Autoboxing seems to give the developer the freedom to mix the use of
native types (`int`, `long`, `float`, `double`, `boolean`) and their
wrapper counterparts (`Integer` etc.) arbitrarily and to substitute
one for the other.

But:

* boxing und unboxing costs time and space -- so you should only do it
  when you really need it. Some developers will use `for (Integer i =
  0; [...])` without knowing what they're doing.

* it introduces the chance to get the comparision wrong (see above) --
  in this case your code may work for the first 127 test cases but
  then no more.

* `Double`/`Float` and `double`/`float` have different `equals` and
  ordering semantics for `0.0`/`-0.0` and `NaN` [2] which may come as
  a surprise.

[1] https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html#valueOf-int-  
[2] https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#equals-java.lang.Object-

-------------------------------------------------------------------

# BigIntegerQuiz

    import static java.lang.System.*;
    import java.math.BigInteger;

    class BigIntegerQuiz {

        public static void main(String... args) {

            BigInteger a = BigInteger.valueOf(Integer.parseInt(args[0]));
            a.add(BigInteger.valueOf(Integer.parseInt(args[1])));

            out.println(a);
        }
    }

Build & run:

    ~/java-quiz$ javac BigIntegerQuiz.java
    ~/java-quiz$ java BigIntegerQuiz 1 2
    1

## Background

`BigInteger` is an immutable class and `BigInteger.add()` is a _pure
function_ and not a mutator (like `List.add()`). Readers/Developes may
not know that and misbelieve that `BigInteger.add()` changes the
`Integer` instance.

In Clojure you use `+`, `-`, `*` and `/` as you expect (like in `(+ a
1)`.

-------------------------------------------------------------------

# BooleanQuiz

    import static java.lang.System.*;
    import static java.lang.Boolean.*;

    class BooleanQuiz {

        public static void main(String... args) {

            boolean a = new Boolean(true);
            boolean b = new Boolean(true);
            Boolean A = new Boolean(true);
            Boolean B = new Boolean(true);

            Boolean C = Boolean.valueOf(true);
            Boolean D = Boolean.valueOf(true);

            out.println(a == b);
            out.println(a == B);
            out.println(A == B);

            out.println(C == D);

            out.println(a == TRUE);
            out.println(A == TRUE);

            out.println(C == TRUE);

            out.println(A.equals(B));
            out.println(A.equals(true));
            out.println(A.equals(TRUE));

        }
    }

Build & run:

    ~/java-quiz$ javac BooleanQuiz.java
    ~/java-quiz$ java BooleanQuiz 
    true
    true
    false
    true
    true
    false
    true
    true
    true
    true

## Background

`Boolean` is not a Java 5 Enum class and thus we can use the `public`
constructor to create instances. When comparing these via `==` for
__identitiy__ they are different. `equals` and auto-(un)-boxing behave
as expected (`Boolean.valueOf(boolean)` uses a cache which returns
`Boolean.TRUE` and `Boolean.FALSE`).

In Clojure you use the boolean [1] literals `true` and `false` (which
are `Boolean/TRUE` and `Boolean/FALSE` at runtime). Test for equality
is done via `=` but you usually use `true?` and `false?` and the fact
that __only__ `nil` and `false` (or equally `Boolean/FALSE`) are
`false?` and everything else (including `(Boolean. false)`) is
`true?`. So when interacting with Java code you must be carefull when
_receiving_ `Boolean` values from the Java-side -- you should convert
them via `(boolean <java-Boolean>)`.

[1] https://clojuredocs.org/clojure.core/boolean

-------------------------------------------------------------------

# CollectionSideeffect

    import static java.lang.System.*;
    import java.util.*;

    class CollectionSideeffect {

        static List sort(List x) {

            Collections.sort(x);
            return x;

        }

        public static void main(String... args) {

            List a = new ArrayList(Arrays.asList("b", "a", "c"));
            List b = sort(a);
            List c = a;

            c.add(0, "d");

            out.println(a);
            out.println(b);
            out.println(c);

        }
    }

Build & run:

    ~/java-quiz$ javac CollectionSideeffect.java
    ~/java-quiz$ java CollectionSideeffect 
    [d, a, b, c]
    [d, a, b, c]
    [d, a, b, c]

## Background

This code is similar to the `ArraySideeffect` from
above. `Collections.sort` is a mutator but we make it look like a
_pure function_. So `a`, `b` and `c` all reference the __same__
mutable `List`.

In Clojure we have __immutable__ lists and vectors which can be sorted
(like in `(sort [6 3 4])` which gives `(3 4 6)`).

-------------------------------------------------------------------

# CorefQuiz

    import static java.lang.System.*;
    import java.util.*;

    class CorefQuiz {

        public static void main(String... args) {

            int[][] i = new int[2][];

            int[] j = new int[] { 1, 2 };
            i[0] = j;

            out.println(Arrays.deepToString(i));

            j[0]++;
            j[1]++;

            i[1] = j;

            out.println(Arrays.deepToString(i));
        }
    }

Build & run:

    ~/java-quiz$ javac CorefQuiz.java 
    ~/java-quiz$ java CorefQuiz
    [[1, 2], null]
    [[2, 3], [2, 3]]

# Background

You cannot only have more than one variable (local on the stack or as
a class field on the heap) reference the same object but you can also
have more than one reference __within__ a structure refering to the
same object. This also introduces the possibility of side effects.

In this example we have `i` -- an array of `int[]`. The elements of
the array that `i` refers to are __references__ (not `int`s!).

After we make `i[0]` and `i[1]` reference the same object (which is an
`int[]` in this case) we can change the `int`s in this array and _see_
the effect of the change in more than one place.

So side effects cannot only be introduced by returning argument
reference values (see above) but also by constructing _co-refering_
(any better term for this?) object-graphs to mutable objects.

This can introduce subtle bugs. Imagine a _service oriented_
application where clients (remote and local) call services through
their interfaces. When a service is called locally or through a remote
__RMI client__, _co-refering_ object graphs will hit the
server/implemenation (since RMI's de-serialization preserves the
_co-refering_ graph-structure). But once you switch to REST or SOAP
clients the server will see a structure without any
_co-references_. This may lead to a different program behaviour.

In Clojure you also have _co-references_ but since things are
__immutable__ they cause no problem.

-------------------------------------------------------------------

# EnumQuiz

    import static java.lang.System.*;

    enum EnumQuiz {

        FOO, BAR;

        public static void main(String... args) {

            Object a = FOO;
            Object c = valueOf("FOO");
            Object d = valueOf(EnumQuiz.class, "FOO");

            out.println(a == FOO);
            out.println(a.equals(FOO));
            out.println(FOO.equals(a));

            out.println(a == c);
            out.println(a.equals(c));
            out.println(c.equals(a));

            out.println(a == d);
            out.println(a.equals(d));
            out.println(d.equals(a));
        }
    }

Build & run:

    ~/java-quiz$ javac EnumQuiz.java
    ~/java-quiz$ java EnumQuiz 
    true
    true
    true
    true
    true
    true
    true
    true
    true

# Background

For every Enum you have exactly one instance. So you can use the
identity check via `==` when comparing Enums.

-------------------------------------------------------------------

# FlowQuiz

    import static java.lang.System.*;

    class FlowQuiz {

        public static void main(String... args) {

            try {

                for (int i : new int[]{ 2, 1, 3 })

                    switch (i) {

                    case 1: 
                        for (int j = 1; j < 5; j++) {
                            out.println(j);
                            if (j == 3) 
                                break;
                        }
                        break;

                    case 3:
                        for (int j = 1; j < 5; j++) {
                            if (j == 2) 
                                return;
                            out.println(j);
                        }

                    case 2:
                        for (int j = 1; j < 5; j++) {
                            if (j == 2) 
                                continue;
                            out.println(j);
                        }

                    default:
                        out.println("default");

                    }

                out.println("done");
            }
            finally {
                out.println("finally");
            }
        }
    }

Build & run:

    ~/java-quiz$ javac FlowQuiz.java
    ~/java-quiz$ java FlowQuiz 
    1
    3
    4
    default
    1
    2
    3
    1
    finally

## Background

`switch/case` with _fall-through_ (i.e. with-out `break`) and loops
with `break` and `continue` are hard to grasp/understand for some
developers. I didn't even put `break` and `continue` __with labels__
(_structured goto_) in there (which many developers don't even know
about).

In Clojure you use _recusive looping_ [1] [2], the sequence
abstraction and list comprehension.

[1] https://clojure.org/about/functional_programming#_recursive_looping  
[2] https://en.wikibooks.org/wiki/Clojure_Programming/Examples/Cookbook#Looping

