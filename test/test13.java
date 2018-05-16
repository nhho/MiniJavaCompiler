// Test
//   Multiple local variables (of type int only) in main()
class test13 {
    public static void main(String[] a){
     int x;
     int y;
     int z;
     {
       x = 1;
       y = 2;
       z = 3;
       System.out.println(z); 
       System.out.println(y); 
       System.out.println(x); 
     }
    }
}



