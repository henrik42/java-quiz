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
