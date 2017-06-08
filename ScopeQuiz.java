import static java.lang.System.*;

class ScopeQuiz {

    static int i = 0;
    String x = "foo";

    ScopeQuiz(String x) {
        this.x = x;
    }

    ScopeQuiz(int x) {
        this("" + x);
        out.println(this.x + i);
    }

    public String toString() { return x + i; }

    public static void main(String... args) {

        out.println(new ScopeQuiz("bar"));
        out.println(i++);
        {
            int i = 1;
            new ScopeQuiz(i++);
        }
        out.println(i++);
        new ScopeQuiz(i++);
    
    }
}
