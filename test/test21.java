// Test
//   Instantiation of a class and invoking a method
class test21 {
    public static void main(String[] _str){
      Foo f;
      {
        System.out.println((new Foo()).f());
      }
    }
}

class Foo {
  public int f() {
    return 999;
  }
}


