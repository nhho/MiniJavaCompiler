// Test
//   Passing array via parameter
//   Returning an array from a method
class test27 {
  public static void main(String[] args) {
    Foo foo;
    int [] B;
    int i;
    {
      foo = new Foo();
      B = new int [10];
      i = 0;
      while (i < B.length) {
        B[i] = i+1;
        i = i + 1; 
      }

      System.out.println(foo.bar(B)[1]);
      System.out.println(foo.bar(B)[4]);
    }
  }
}

class Foo {

  public int [] bar(int [] A) {
    int i;

    i = 0;
    while (i < A.length) {
      A[i] = A[i] * 2;
      i = i + 1;
    }

    return A;
  }
}
