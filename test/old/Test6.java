class Test6 {
    public static void main(String[] a){
      Foo foo;
      int sum;
      int dummy;
      {
        foo = new Foo();
        dummy = foo.init();
        dummy = foo.print();

        sum = foo.sum(sum, 5+sum);

	System.out.println(sum); 
      }
    }
}

class Foo {
  int x;
  int y;
  int [] A;
  int z;
  public int init() {
    x = 1; y = 2; z = 3;
    A = new int [100];
    A[99] = 1234;
    return 0;
  }

  public int print() {
    System.out.println(x);     // 1
    System.out.println(y);     // 2
    System.out.println(z);     // 3
    System.out.println(A[0]);  // 0
    System.out.println(A[99]); // 1234

    return 0;
  }

  public int sum(int x, int y) {
    return x + y;
  }
}


