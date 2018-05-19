class paravar
{
    public static void main(String[] s)
    {
        A a;
        {
            a = new A();
            System.out.println(a.set1());
            System.out.println(a.set2());
            System.out.println(a.set3());
            System.out.println(a.print(456,999));
        }
    }
}

class A
{
    int a1; int a2; int a3;
    public int set1() {a1 = 11;return 111;}
    public int set2() {a2 = 22;return 222;}
    public int set3() {a1 = 33;return 333;}
    public int print(int a1, int a3) {
      int a2;
      a2 = 789;
      System.out.println(a1);
      System.out.println(a2);
      System.out.println(a3);
      System.out.println(this.set1());
      return 444;
    }
}