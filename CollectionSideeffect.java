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
