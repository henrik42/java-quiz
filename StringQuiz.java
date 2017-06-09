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
