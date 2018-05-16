// Test
//   A lot of local variables (of type int only) in main()
//   Use of variables in expressions
class test15 {
    public static void main(String[] a){
     int x1;
     int x2;
     int x3;
     int x4;
     int x5;
     int x6;
     int x7;
     int x8;
     int x9;
     int x10;
     int x11;
     int x12;
     int x13;
     int x14;
     int x15;
     int x16;
     {
       x1 = 1;
       x2 = 2;
       x3 = 3;
       x4 = 4;
       x5 = 5;
       x6 = 6;
       x7 = 7;
       x8 = 8;
       x9 = 9;
       x10 = 10;
       x11 = 11;
       x12 = 12;
       x13 = 13;
       x14 = 14;
       x15 = 15;
       x16 = 16;

       x1 = x1 + x1;
       x2 = x2 + x1;
       x3 = x3 + x1;
       x4 = x4 + x1;
       x5 = x5 + x1;
       x6 = x6 + x1;
       x7 = x7 + x1;
       x8 = x8 + x1;
       x9 = x9 + x1;
       x10 = x10 + x1+x2+x3+x4;
       x11 = x11 - x1-x2-x3-x4;
       x12 = x12 + x12;
       x13 = x13 + 1 - (x13 + 1) * 2;
       x14 = x14 + 1 - x14 - x15;
       x15 = x15 * 2 * 0 * x15;
       x16 = x16 + (0-1);

       System.out.println(x1); 
       System.out.println(x2); 
       System.out.println(x3); 
       System.out.println(x4); 
       System.out.println(x5); 
       System.out.println(x6); 
       System.out.println(x7); 
       System.out.println(x8); 
       System.out.println(x9); 
       System.out.println(x10); 
       System.out.println(x11); 
       System.out.println(x12); 
       System.out.println(x13); 
       System.out.println(x14); 
       System.out.println(x15); 
       System.out.println(x16); 
     }
    }
}

