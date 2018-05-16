// Test
//   Multiple local variables (of type int only) in main()
//   Use of variables in expressions
class test14 {
    public static void main(String[] a){
     int x;
     int y;
     int z;
     {
       z = 1;
       x = 4 - 8;
       y = z + 2;
       z = (x+1) * (x+1) * (x+1);
       System.out.println(z); 
       System.out.println(y); 
       System.out.println(x); 
     }
    }
}

