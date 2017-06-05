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
