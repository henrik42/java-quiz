import static java.lang.System.*;

class FlowQuiz {

    public static void main(String... args) {

        try {

            for (int i : new int[]{ 2, 1, 3 })

                switch (i) {

                case 1: 
                    for (int j = 1; j < 5; j++) {
                        out.println(j);
                        if (j == 3) 
                            break;
                    }
                    break;
            
                case 3:
                    for (int j = 1; j < 5; j++) {
                        if (j == 2) 
                            return;
                        out.println(j);
                    }

                case 2:
                    for (int j = 1; j < 5; j++) {
                        if (j == 2) 
                            continue;
                        out.println(j);
                    }

                default:
                    out.println("default");

                }

            out.println("done");
        }
        finally {
            out.println("finally");
        }
    }
}
