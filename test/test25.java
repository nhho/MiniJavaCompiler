// Test
//   Class and method
class test25 {
    public static void main(String[] _str){
      Foo f;
      int a;
      int b;
      {
        f = new Foo();
        a = f.m1().m2();
        System.out.println(a);
      }
    }
}

class Foo {
  public Foo m1() {
    return new Foo();
  }

  public int m2() {
    System.out.println(456);
    return 1;
  }

}

