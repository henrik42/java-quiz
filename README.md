# java-quiz

Some small Java code examples for testing your Java knowledge.

At work we noticed that some developers didn't fully understand some
of the Java basics they were using day in day out. So we came up with
some code examples they could use to check their knowledge. We don't
use these for job interviews yet.

I'll add some comments and Clojure [1] code. Clojure is a LISP-like
functional JVM language. I love Java (and adore Clojure) but I believe
it has some properties that make it hard to get right. So I'll say a
little about why certain error/bugs are less likely in Clojure than in
Java.

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
and `bit-or` (for integer numbers only though). The names are clear
and explicit. You won't confuse them.

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
an object. Java uses __by value__ semantics [3] but this
description/term leads some developers to believe that `sort(i)` will
pass the object/value/array to the method and not just the
__reference__ to that object/value/array.

Here we introduce the `sort` method to further trick the reader: it
looks like a _pure function_, but it just returns its argument
(i.e. the reference to the mutable array) -- well, thank's for that!
This kind of error (i.e. introducing side effects via state sharing
between arguments and return values) is done quite often by developers
unintentionally and these errors are hard to find.

In Clojure you almost always use immutable data types (including
immutable collections -- called _persistent data structures_ [2]). So
there is no danger of side effects. In Clojure __state__ is managed by
several __mutable reference types__. But the values (like lists and
maps) are kept __immutable__. So it's always safe to pass/receive
values to/from functions.

A Clojure program that comes close to the structure of the Java
program from above looks like this:

    (defn main []
      (let [i [3 2 1]
            k i
            _ (println i)
            _ (println k)
            j (into [] (sort i))]
        (println i)
        (println j)
        (println k)))

And run (you'll need to get `clojure.jar`):

    ~/java-quiz$ java -jar clojure.jar -i array-side-effect.clj -e '(main)'
    [3 2 1]
    [3 2 1]
    [3 2 1]
    [1 2 3]
    [3 2 1]

So only `j` is sorted -- no side effect.

Note: Clojure can use Java's mutable arrays directly and in this case
you get uncontrolled state sharing and side effects in Clojure as
well [4].

[1] https://en.wikipedia.org/wiki/Side_effect_(computer_science)  
[2] https://clojure.org/reference/data_structures  
[3] http://javadude.com/articles/passbyvalue.htm  
[4] https://clojuredocs.org/clojure.core/sort

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

In Clojure there is no auto-boxing since you're usually using the
wrapper types only (see "Numbers" in [3]). But for Java-interop [4]
there are ways to deal with native number types.

[1] https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html#valueOf-int-  
[2] https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#equals-java.lang.Object-  
[3] https://clojure.org/api/cheatsheet  
[4] https://clojure.org/reference/java_interop

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
1)`). Note that Clojure has built-in literals for `BigDecimal` (not
`BigInteger` though):

    (+ 1.20M 1.4M) ; -> 2.60M
    (type 2.60M)   ; -> java.math.BigDecimal

-------------------------------------------------------------------

# NumberQuiz

    import static java.lang.System.*;

    class NumberQuiz {

        public static void main(String... args) {

            out.println("A:" + (0.2 == 0.2f));
            out.println("B:" + (0.5 == 0.5f));

            float sf = 0.2f + 0.2f + 0.2f;
            double sd = 0.2 + 0.2 + 0.2;

            out.println("sf:" + sf);
            out.println("sd:" + sd);

            out.println("C:" + (0.0 == 0.0f));
            out.println("D:" + (0.0 == -0.0f));
            out.println("E:" + (0.0 == -0.0));

            out.println("F:" + new Double(0.0).equals(0.0f));
            out.println("G:" + new Double(0.0).equals(-0.0f));
            out.println("H:" + new Double(0.0).equals(-0.0));

            out.println("I:" + (Double.NaN == Double.NaN));
            out.println("J:" + (Double.NaN > 0.0));
            out.println("K:" + (Double.NaN < 0.0));

            out.println("L:" + (new Double(Double.NaN).equals(Double.NaN)));
            out.println("M:" + (new Double(0.0).compareTo(Double.NaN)));

            short s = (short) (Short.MAX_VALUE + 1);

            out.println("N:" +  Short.MAX_VALUE);
            out.println("O:" + (Short.MAX_VALUE + 1));
            out.println("P:" + s);

            out.println("Q:" + Integer.MAX_VALUE);
            out.println("R:" + (Integer.MAX_VALUE + 1));

        }
    }

Build & run:

    ~/java-quiz$ javac NumberQuiz.java
    ~/java-quiz$ java NumberQuiz
    A:false
    B:true
    sf:0.6
    sd:0.6000000000000001
    C:true
    D:true
    E:true
    F:false
    G:false
    H:false
    I:false
    J:false
    K:false
    L:true
    M:-1
    N:32767
    O:32768
    P:-32768
    Q:2147483647
    R:-2147483648

## Background

Floating point numbers cannot represent all decimal numbers exactly
(see IEEE 754 below). `0.5` can be represented exactly (because it is
a division of `1` by a power of `2`) and conversion from `float` to
`double` introduces no error ("Widening Primitive Conversion"; see
[1]). `0.2` cannot be represented this way. When converting `float` to
`double` an _error_ is introduced.

Funny: when adding up `0.2f` values the result seems _more exact_ than
when adding up `0.2d` (but this is not true for all/any values).

`0.0` and `-0.0` are considered being equal for `double` eventhough
their bit representations are not. Comparing against `float`
(i.e. conversion to `double`) introduces no error -- so they're equal.

Not so for `Double`! `0.0` and `-0.0` and `Float`s are all
__non-equal__.

For `NaN` (_not a number_) it's different: `NaN` is not even equal to
itself (like `NULL` in databases is not `= NULL` but `IS NULL` is used
for testing for `NULL`). Then again it's different for `Double`.

Integer number types have some strange properties as well: they _wrap
around_ when they reach their `MIN_VALUE`/`MAX_VALUE`. Note that there
is one more negative value than there are positive values. In the
expression `Short.MAX_VALUE + 1` the `short` value is converted to
`int` and then `1` is added. So there is no _wrap around_.

[1] https://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html

## Conversion

When comparing a `float` value against a `double` value Java converts
the `float` value to a `double` value (not the other way around!). You
can check by looking at the byte-code of `CompareFloatAndDouble.java`.

    import static java.lang.System.*;

    class CompareFloatAndDouble {

        public static void main(String... args) {

            double d = Double.parseDouble(args[0]);
            float f = (float) d;
            boolean b = d == f;

            double fd = f;

            out.println("d ="+d+"/"+String.format("%016X", Double.doubleToRawLongBits(d)));
            out.println("f ="+f+"/"+String.format("%08X", Float.floatToRawIntBits(f)));
            out.println("fd="+fd+"/"+String.format("%016X", Double.doubleToRawLongBits(fd)));

            out.println("b="+b);

        }
    }

Build & run:

    ~/java-quiz$ javac CompareFloatAndDouble.java
    
    ~/java-quiz$ java CompareFloatAndDouble 0.5
    d =0.5/3FE0000000000000
    f =0.5/3F000000
    fd=0.5/3FE0000000000000
    b=true

    ~/java-quiz$ java CompareFloatAndDouble 0.2
    d =0.2/3FC999999999999A
    f =0.2/3E4CCCCD
    fd=0.20000000298023224/3FC99999A0000000
    b=false

    ~/java-quiz$ java CompareFloatAndDouble NaN
    d =NaN/7FF8000000000000
    f =NaN/7FC00000
    fd=NaN/7FF8000000000000
    b=false

    ~/java-quiz$ java CompareFloatAndDouble 0.0
    d =0.0/0000000000000000
    f =0.0/00000000
    fd=0.0/0000000000000000
    b=true

    ~/java-quiz$ java CompareFloatAndDouble -0.0
    d =-0.0/8000000000000000
    f =-0.0/80000000
    fd=-0.0/8000000000000000
    b=true

Now look at the byte-code:

    ~/java-quiz$ javap -v CompareFloatAndDouble
         [...]
         0: aload_0       
         1: iconst_0      
         2: aaload        
         3: invokestatic  #2                  // Method java/lang/Double.parseDouble:(Ljava/lang/String;)D
         6: dstore_1
         
Here the `double` value gets converted to `float` (`d2f`):

         7: dload_1       
         8: d2f           
         9: fstore_3

Here the `float` value gets converted (back) to a `double` (`f2d`) ...

        10: dload_1       
        11: fload_3
        12: f2d

... and the two `double` values are compared (`dcmpl`):

        13: dcmpl         
        14: ifne          21
        17: iconst_1      
        18: goto          22
        21: iconst_0      
        22: istore        4
        24: fload_3       
        25: f2d           
        26: dstore        5
        28: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
        [...]
        
Looking at the _bit-representation_ of the `float` and the `double`
values you can tell that the values for `0.5` look a lot more alike
than the values for `0.2` (details can be found in the "IEEE 754 –
IEEE Standard for Binary Floating-Point Arithmetic for microprocessor
systems (ANSI/IEEE Std 754-1985/IEEE 754-2008)" [1]).

[1] https://en.wikipedia.org/wiki/IEEE_floating_point

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
are `java.lang.Boolean/TRUE` and `java.lang.Boolean/FALSE` at
runtime). Test for equality is done via `=` but you usually use
`true?` and `false?` and the fact that __only__ `nil` and `false` (or
equally `Boolean/FALSE`) are `false?` and everything else
(__including__ `(Boolean. false)`) is `true?`. So when interacting
with Java code you must be carefull when _receiving_ `Boolean` values
from the Java-side -- you should convert them via `(boolean
<java-Boolean>)`.

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
a class' field on the heap) reference the same object but you can also
have more than one reference __within__ a structure refering to the
same object (note that this means you have to expect cyclic structures
and even objects with __references to themselves__!). This also
introduces the possibility of side effects.

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
clients the server will see a structure without any _co-references_
(which you could call __by value__ semantics -- see above). This may
lead to a different program behaviour.

In Clojure you also have _co-references_ (i.e. _sharing_) but since
things are __immutable__ this causes no problem.

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

Clojure does not support Enums well. You can use the Java Enums but
you cannot easely define them. You use namespaced names/vars instead.

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
abstraction and list comprehension. For example:

* Produce output 1..4:

        (for [i (range 1 5)]
            (println i))

* Do not output 2 -- produces 1,3,4.

        (doseq [i (remove #{2} (range 1 5))]
            (println i))

* Do not output 2 and 3 -- produces 1,4.

        (doseq [i (remove #{2 3} (range 1 5))]
            (println i))

* Produce until 3 -- produces 1,2.

        (doseq [i (take-while #(not (#{3} %)) (range 1 5))]
            (println i))

  or

        (doseq [i (range 1 5)
                :while (not (#{3} i))]
          (println i))

If you want to really control the _loop variable_ you can use
`loop`. This produces 1,2,3:

    (loop [i 1]
      (when (<= i 3)
        (println i)
        (recur (inc i))))

In none of these examples you have a mutable __variable__ which could
be used outside of the loop by mistake. All you get is successive
__bindings__ of a name to different __immutable__ values.

_Early returns_ are _imperative trickery_. In Clojure the semantics of
execution follow the structure of code much more than in Java. Things
like _early returns_, `break` or `continue` are only _structured
gotos_ which you cannot do in Clojure.

[1] https://clojure.org/about/functional_programming#_recursive_looping  
[2] https://en.wikibooks.org/wiki/Clojure_Programming/Examples/Cookbook#Looping

-------------------------------------------------------------------

# Loops

    import static java.lang.System.*;

    class Loops {

        public static void main(String... args) {

            int[] a = new int[] { 3, 2, 1, 0 };

            int j = 0;
            do 
                out.println(j);
            while (a[j++] != 1);

            for (int i : a)
                if (i == 1)
                    continue;
                else
                    out.println(a[j]);

            for (int k = 0;; k++)
                if (a[k] > 2)
                    out.println(k);
                else 
                    break;
        }
    }

Build & run:

    ~/java-quiz$ javac Loops.java
    ~/java-quiz$ java Loops 
    0
    1
    2
    0
    0
    0
    0

## Background

Loops are "fun" in Java if you like. `while` loops do not scope
looping variables so you have to define them in the surrounding scope
(which may be larger than you want -- but you can use a block scope).

-------------------------------------------------------------------

# Mutable

    import static java.lang.System.*;
    import java.util.*;

    class Mutable {

        public static void main(String... args) {

            Set set = new HashSet();
            String[] strings = new String[] { "zoo", "foo", "bar" };
            List list = Arrays.asList(strings);

            set.add(list);

            out.println("set.size() = " + set.size());
            out.println("set = " + Arrays.toString(set.toArray()));
            out.println("set.contains(list) = " + set.contains(list));

            out.println("list = " + list);
            Arrays.sort(strings);
            out.println("list = " + list);

            out.println("set = " + Arrays.toString(set.toArray()));
            out.println("set.contains(list) = " + set.contains(list));

        }
    }

Build & run:

    ~/java-quiz$ javac Mutable.java
    ~/java-quiz$ java Mutable 
    set.size() = 1
    set = [[zoo, foo, bar]]
    set.contains(list) = true
    list = [zoo, foo, bar]
    list = [bar, foo, zoo]
    set = [[bar, foo, zoo]]
    set.contains(list) = false

## Background

Mutable keys are bad for you (`HashSet`, `HashMap`, `TreeSet`). The
set does not `contains` the element (a `List` in this case) which is
its first element -- "confusing".

-------------------------------------------------------------------

# NullCheck

Which of the four _null-checks_ are "correct"? The code is supposed to
check if the argument is `null` (like in a check to prevent a
`NullPointerException` when re-referencing `x`).

    import static java.lang.System.*;

    class NullCheck {

        static Object NULL = null;

        static void foo(Object x) {

            if (NULL.equals(x)); // 1
            if (x.equals(NULL)); // 2
            if (x == NULL);      // 3
            if (NULL == x);      // 4

        }

        public static void main(String... args) {

            // foo(...);

        }
    }

## Background

Option `// 1` causes a `NullPointerException` since `NULL.equals()`
de-references `null`. Option `// 2` will cause a
`NullPointerException` in the case "x is null". So both are wrong.

Options `// 3` and `// 4` both work.

In Clojure you use `nil?` without trouble.

-------------------------------------------------------------------

# OverLoad

    import static java.lang.System.*;

    interface X { void foo(Object x); }

    class A implements X { 
        public void foo(Object x)  { out.println("A1" + x); }
        public void foo(String x)  { out.println("A2" + x); }
        public void foo(Integer x) { out.println("A2" + x); }
        public String toString() { return "A"; }
    }

    class B implements X { 
        public void foo(Object x)  { out.println("B1" + x); }
        public void foo(String x)  { out.println("B2" + x); }
        public void foo(Integer x) { out.println("B3" + x); }
        public String toString() { return "B"; }
    }

    class OverLoad {

        public static void main(String... args) {

            X a = new A();
            B b = new B();

            a.foo("foo");
            b.foo("bar");

            a.foo(1);
            b.foo(2);

            new A().foo("fred");
            new B().foo(a);
        }
    }

Build & run:

    ~/java-quiz$ javac OverLoad.java
    ~/java-quiz$ java OverLoad 
    A1foo
    B2bar
    A11
    B32
    A2fred
    B1A

## Background

_Overloading_ and _overriding_ are confused by many developers. One of
the frequent error is that developers believe that `a.foo("foo")`
calls `A.foo(String)` which it doesn't. But in `new A().foo("fred")`
it does -- of course.

In Clojure you use "interfaces" (_protocols_) and implemenations for
these. But usually you do not use inheritence much. For Java-interop
you can extends Java classes without trouble.

-------------------------------------------------------------------

# ScopeQuiz

OK -- this one is silly.

    import static java.lang.System.*;

    class ScopeQuiz {

        static int i = 0;
        String x = "foo";

        ScopeQuiz(String x) {
            this.x = x;
        }

        ScopeQuiz(int x) {
            this("" + x);
            out.println(this.x + i);
        }

        public String toString() { return x + i; }

        public static void main(String... args) {

            out.println(new ScopeQuiz("bar"));
            out.println(i++);
            {
                int i = 1;
                new ScopeQuiz(i++);
            }
            out.println(i++);
            new ScopeQuiz(i++);

        }
    }

Build & run:

    ~/java-quiz$ javac ScopeQuiz.java
    ~/java-quiz$ java ScopeQuiz 
    bar0
    0
    11
    1
    23

-------------------------------------------------------------------

# StringQuiz

    import static java.lang.System.*;

    class StringQuiz {

        public static void main(String... args) {

            String a = new String("a");
            String b = new String("a");

            out.println("a" == "a");
            out.println(a == "a");
            out.println(a == b);
            out.println(a.equals(b));

            a.concat("b");

            out.println(a);
            out.println(a == b);
            out.println(a.equals(b));

        }
    }


Build & run:

    ~/java-quiz$ javac StringQuiz.java
    ~/java-quiz$ java StringQuiz
    true
    false
    false
    true
    a
    false
    true

## Background

`String` is immutable and so `a.concat("b")` doesn't change anything
but returns a __new__ `String`. `String` literals are _interned_
(i.e. _pooled_) and so `"a"` is __identical__ to `"a"` in `"a" == "a"`.

Some developers misbelieve that `String` comparison can always be done
by `==` (because `"a" == "a"`) since they do not know about _interned_
`String`s.

In Clojure you use `(= "a" x)` which behaves like `String.equals` but
handles `null` (Clojure `nil`) without throwing. Clojure _interns_
`String` literals as well.

    (= "foo" "foo") ; -> true
    (= nil "foo") ; -> false
    (identical? "foo" "foo") ; -> true
    (identical? (String. "foo") "foo") ; -> false
    (identical? (.intern (String. "foo")) "foo") ; -> true

-------------------------------------------------------------------

# TryCatchFinally

    import static java.lang.System.*;

    class TryCatchFinally {

        public static void main(String... args) { 

            out.println(foo(args)); 

        }

        public static int foo(String... args) {

            int i = 0;

            oops: try {

                out.println("foo" + args[0]);
                if (true)
                    return i++;

            } 
            catch (Throwable t) {

                out.println("catch");
                return i++;

            } 
            finally {

                out.println("finally" + i);
                if (args.length > 0 && "foo".equals(args[0]))
                    break oops;

            }

            out.println("done" + i);

            return i;
        }
    }

Build & run:

    ~/java-quiz$ javac TryCatchFinally.java
    ~/java-quiz$ java TryCatchFinally    
    catch
    finally1
    0

    ~/java-quiz$ java TryCatchFinally foo
    foofoo
    finally1
    done1
    1

    ~/java-quiz$ java TryCatchFinally bar
    foobar
    finally1
    0

## Background

Maybe the `foo` case is the most surprising. `finally` can introduce
its own _termination reason_ (via `return` or `break`). So eventhough
the `try` block is terminated by `return` the `finally` block can
_overrule_ this by `break`. The semantics (the question "when does it
happen?")  of the post-increment (`return i++`) are also interesting.

In Clojure we don't have `return` and `break` but we do have
`try/catch/finally`. `finally` is evaluated __only__ for side effects
(like closing streams etc.) but __not__ for returning results. So the
result of a `try/catch/finally` always comes from the `try` expression
or the `catch` expression.

-------------------------------------------------------------------

