class ext
{
    public static void main(String[] s)
    {
        B b;
        {
            b = new B();
            System.out.println(b.set());
            System.out.println(b.get());
            System.out.println(b.set4());
            System.out.println(b.get());
            System.out.println(b.getA());
            System.out.println(b.getAA());
        }
    }
}

class A
{
    int a;
    public int set4() {a = 4;return 4;}
    public int set() {a = 1;return 11;}
    public int get() {return a;}
    public int getA() {return this.get();}
    public int getAA() {return a;}
}

class B extends A
{
    int a;
    public int set() {a = 2;return 22;}
    public int get() {return a;}
}