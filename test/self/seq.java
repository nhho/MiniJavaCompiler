class seq
{
    public static void main(String[] s)
    {
        A a;
        {
            a = new A();
            System.out.println(a.set1(11));
            System.out.println(a.i());
        }
    }
}

class A
{
    int a1;
    public int set1(int a) {a1 = a;return a;}
    public int i() {
      a1 = a1 - 1;
      System.out.println(a1);
      if (0 < a1)
        System.out.println(this.i());
      else {
a1=a1;
      }
      return a1;
    }
}