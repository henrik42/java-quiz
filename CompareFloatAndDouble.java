import static java.lang.System.*;

class CompareFloatAndDouble {

    public static void main(String... args) {

        double d = Double.parseDouble(args[0]);
        float f = (float) d;
        boolean b = d == f;

        double fd = f;

        out.println("d ="+d+"/"+String.format("%016X", Double.doubleToRawLongBits(d)));
        out.println("f ="+f+"/"+String.format("%08X", Float.floatToRawIntBits(f)));
        out.println("fd="+fd+"/"+String.format("%016X", Double.doubleToRawLongBits(fd)));

        out.println("b="+b);

    }
}
