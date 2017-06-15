import static java.lang.System.*;

class CompareFloatAndDouble {

    public static void main(String... args) {

        double d = Double.parseDouble(args[0]);
        float f = (float) d;
        boolean b = d == f;

        out.println("d="+d+"/"+String.format("%16X", Double.doubleToRawLongBits(d)));
        out.println("f="+f+"/"+String.format("%08X", Float.floatToRawIntBits(f)));
        out.println("b="+b);

    }
}
