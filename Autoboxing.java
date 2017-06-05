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
