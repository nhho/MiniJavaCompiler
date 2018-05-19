class null1
{
    public static void main(String[] z_)
    {
        C c;
        {
            System.out.println(1234);
            c = new C();
            System.out.println(1234);
            c = c.c();
            System.out.println(1234);
            c = c.c();
        }
    }
}

class C
{
    C c;
    public C c() {return c;}
}