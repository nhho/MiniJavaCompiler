// Test
//   Multiple local variables (of type int only) in main()
//   while loop
class test16 {
    public static void main(String[] a){
     int x;
     {
       x = 1;
       while (x < 10) {
         System.out.println(x);
         x = x + 1;
       }
     }
    }
}

