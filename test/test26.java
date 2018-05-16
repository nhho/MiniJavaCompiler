// Test
//  Multiple classes with fields
class test26 {
  public static void main(String[] args) {
    Foo foo;
    Bar bar;
    int dummy;
    {
      foo = new Foo();
      bar = new Bar();

      dummy = foo.init(1000, 2000);
      dummy = foo.f();
      dummy = foo.f();
      dummy = foo.g();
      dummy = foo.g();
      dummy = bar.setFoo(foo);
      System.out.println(bar.f());
      System.out.println(bar.g());
    }
  }
}

class Foo {
  int x;
  int y;

  public int init(int _x, int _y) {
    x = _x;
    y = _y;
    return 0;
  }

  public int f() {
    x = x + 1;
    return 0;
  }

  public int g() {
    y = y + 10;
    return 0;
  }

  public int getX() { return x; }
  public int getY() { return y; }

}

class Bar {
  Foo foo;
 
  public int setFoo(Foo _foo) {
    foo = _foo;
    return 0;
  }

  public int f() {
    int dummy;
    dummy = foo.f(); 
    return foo.getX();
  }

  public int g() {
    int dummy;
    dummy = foo.g(); 
    return foo.getY();
  }
}

