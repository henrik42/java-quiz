import static java.lang.System.*;

interface X { void foo(Object x); }

class A implements X { 
    public void foo(Object x)  { out.println("A1" + x); }
    public void foo(String x)  { out.println("A2" + x); }
    public void foo(Integer x) { out.println("A2" + x); }
    public String toString() { return "A"; }
}

class B implements X { 
    public void foo(Object x)  { out.println("B1" + x); }
    public void foo(String x)  { out.println("B2" + x); }
    public void foo(Integer x) { out.println("B3" + x); }
    public String toString() { return "B"; }
}

class OverLoad {
    
    public static void main(String... args) {

        X a = new A();
        B b = new B();

        a.foo("foo");
        b.foo("bar");

        a.foo(1);
        b.foo(2);

        new A().foo("fred");
        new B().foo(a);
    }
}
