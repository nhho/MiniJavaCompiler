// Test
//   Array in general
class test19 {
    public static void main(String[] a){
     int [] A;
     int [] B;
     int [] C;
     int i;
     int j;
     int N;
     {
       N = 10;
       A = new int [N];
       B = new int [N*N];   

       i = 0;
       while (i < A.length) {
         A[i] = i;

         j = 0;
         while (j < N) {
           B[i*N + j] = i * N + j;
           j = j + 1;
         }

         i = i + 1;
       }

       i = 0;
       while (i < A.length) {
         System.out.println(A[i]);
         System.out.println(B[i*N]);   // If you get parsing error for this line, it might be
                                       // a bug in minijava.jj for ArrayLookup(). :P
                                       // Make sure you change it to 
                                       //   ArrayLookup ::= PrimaryExpression [ Expression() ]
                                       // instead of 
                                       //   ArrayLookup ::= PrimaryExpression() [ PrimaryExpression() ]
         i = i + 1;
       }

     }
    }
}

