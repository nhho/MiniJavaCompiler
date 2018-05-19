class instance
{
    public static void main(String[] s)
    {
        A a1;
        A a2;
        {
            a1 = new A();
            System.out.println(a1.set_wrong(1));
            System.out.println(a1.get());
            System.out.println(a1.set1());
            System.out.println(a1.get());
            System.out.println(a1.set2());
            System.out.println(a1.get());
            a1 = new A();
            System.out.println(a1.set_wrong(1));
            System.out.println(a1.get());
            System.out.println(a1.set2());
            System.out.println(a1.get());
            System.out.println(a1.set1());
            System.out.println(a1.get());
            a2 = new A();
            System.out.println(a2.set_wrong(1));
            System.out.println(a2.get());
            System.out.println(a2.set1());
            System.out.println(a2.get());
            System.out.println(a2.set2());
            System.out.println(a2.get());
            System.out.println(a1.get());
        }
    }
}

class A
{
    int a;
    public int set_wrong(int a) {a = 1;return 11;}
    public int set1() {a = 1;return 22;}
    public int set2() {a = 2;return 33;}
    public int get() {return a;}
}