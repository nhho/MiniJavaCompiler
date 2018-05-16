// Test
//   Class and method
class test23 {
    public static void main(String[] _str){
      Foo f;
      int a;
      int [] A;
      {
        A = new int [10];
        f = new Foo();
        a = 5;
        a = f.m1(1,a,a+a,true,A);
        System.out.println(a);
        System.out.println(A[1]);
      }
    }
}

class Foo {
  public int m1(int a, int b, int c, boolean bar, int [] A) {
    System.out.println(a);
    System.out.println(b);
    System.out.println(c);
    if (bar)
      System.out.println(1000);
    else
      System.out.println(2000);
    A[1] = 999;

    return a + b + c;
  }

}

