class null2
{
    public static void main(String[] z_)
    {
        C c;
        int [] d;
        {
            System.out.println(1234);
            c = new C();
            System.out.println(1234);
            d = c.c();
            System.out.println(1234);
            d[0] = 1;
        }
    }
}

class C
{
    int [] c;
    public int [] c() {return c;}
}