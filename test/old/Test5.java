class Test5 {
    public static void main(String[] a){
      int [] A;
      int i;
      {
        A = new int [10];
        A[9] = 1234;
	System.out.println(A[9]);   // 1234
	System.out.println(A[0]);   // 0
	System.out.println(A.length);   // 10

        A[1+2] = 123;
	System.out.println(A[3]);

        A[1] = A[2] + A[3] + 1;
	System.out.println(A[1]);  // 124

        A[0] = 5;
        A[A[0]] = A[0];          
	System.out.println(A[5]);  // 5
      }
    }
}



