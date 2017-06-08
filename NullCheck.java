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
