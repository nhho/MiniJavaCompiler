// Test
//   Nested while loop + if-else
class test17 {
    public static void main(String[] a){
     int x;
     int y;
     int sum;
     {
       sum = 0;
       x = 1;
       while (x < 10) {

         y = 1;
         while (y < 10) {

            if (x < y)
             sum = sum + 1;
            else { }

            y = y + 1;
         }
         x = x + 1;
       }

       System.out.println(sum);
     }
    }
}

