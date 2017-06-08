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
