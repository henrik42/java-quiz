import static java.lang.System.*;

class TryCatchFinally {

    public static void main(String... args) { 

        out.println(foo(args)); 

    }

    public static int foo(String... args) {

        int i = 0;

        oops: try {

            out.println("foo" + args[0]);
            if (true)
                return i++;
            
        } 
        catch (Throwable t) {

            out.println("catch");
            return i++;
            
        } 
        finally {

            out.println("finally" + i);
            if (args.length > 0 && "foo".equals(args[0]))
                break oops;

        }

        out.println("done" + i);

        return i;
    }
}
