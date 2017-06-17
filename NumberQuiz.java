import static java.lang.System.*;

class NumberQuiz {

    public static void main(String... args) {

        out.println("A:" + (0.2 == 0.2f));
        out.println("B:" + (0.5 == 0.5f));

        float sf = 0.2f + 0.2f + 0.2f;
        double sd = 0.2 + 0.2 + 0.2;

        out.println("sf:" + sf);
        out.println("sd:" + sd);
        
        out.println("C:" + (0.0 == 0.0f));
        out.println("D:" + (0.0 == -0.0f));
        out.println("E:" + (0.0 == -0.0));

        out.println("F:" + new Double(0.0).equals(0.0f));
        out.println("G:" + new Double(0.0).equals(-0.0f));
        out.println("H:" + new Double(0.0).equals(-0.0));

        out.println("I:" + (Double.NaN == Double.NaN));
        out.println("J:" + (Double.NaN > 0.0));
        out.println("K:" + (Double.NaN < 0.0));

        out.println("L:" + (new Double(Double.NaN).equals(Double.NaN)));
        out.println("M:" + (new Double(0.0).compareTo(Double.NaN)));
        
        short s = (short) (Short.MAX_VALUE + 1);

        out.println("N:" +  Short.MAX_VALUE);
        out.println("O:" + (Short.MAX_VALUE + 1));
        out.println("P:" + s);

        out.println("Q:" + Integer.MAX_VALUE);
        out.println("R:" + (Integer.MAX_VALUE + 1));

    }
}
