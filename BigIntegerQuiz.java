import static java.lang.System.*;
import java.math.BigInteger;

class BigIntegerQuiz {

    public static void main(String... args) {

        BigInteger a = BigInteger.valueOf(Integer.parseInt(args[0]));
        a.add(BigInteger.valueOf(Integer.parseInt(args[1])));

        out.println(a);
    }
}
