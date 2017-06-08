import static java.lang.System.*;

class Loops {

    public static void main(String... args) {

        int[] a = new int[] { 3, 2, 1, 0 };

        int j = 0;
        do 
            out.println(j);
        while (a[j++] != 1);

        for (int i : a)
            if (i == 1)
                continue;
            else
                out.println(a[j]);

        for (int k = 0;; k++)
            if (a[k] > 2)
                out.println(k);
            else 
                break;
    }
}
