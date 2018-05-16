// Test
//   Array in general
class test20 {
    public static void main(String[] a){
     int [] A;
     int i;
     {
       A = new int [10];

       i = 10;
       while (0 < i) {
         i = i - 1;
         A[i] = i+1;
       }

       A[9] = A[ A[ A[0] ] ];
       A[8] = A[ 2 * 2 * 2 ] + A[0] * 30;

       i = 0;
       while (i < A.length) {
         System.out.println(A[i]);
         i = i + 1;
       }
     }
    }
}

