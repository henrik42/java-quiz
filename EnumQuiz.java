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
