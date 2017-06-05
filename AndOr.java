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
