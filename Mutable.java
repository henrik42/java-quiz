import static java.lang.System.*;
import java.util.*;

class Mutable {

    public static void main(String... args) {

        Set set = new HashSet();
        String[] strings = new String[] { "zoo", "foo", "bar" };
        List list = Arrays.asList(strings);

        set.add(list);

        out.println("set.size() = " + set.size());
        out.println("set = " + Arrays.toString(set.toArray()));
        out.println("set.contains(list) = " + set.contains(list));

        out.println("list = " + list);
        Arrays.sort(strings);
        out.println("list = " + list);

        out.println("set = " + Arrays.toString(set.toArray()));
        out.println("set.contains(list) = " + set.contains(list));
    
    }
}
