// Test
//  Mixing all operators in the condition of if-else 
class test10 {
    public static void main(String[] a){
     { 
      if (2+1 < 3*2)
        System.out.println(1);
      else
        System.out.println(0);

      if (2+2+2 < (0-2)*(0-5))
        System.out.println(1);
      else
        System.out.println(0);

      if (!false && !!true)
        System.out.println(1);
      else
        System.out.println(0);

      if (true && !false)
        System.out.println(1);
      else
        System.out.println(0);

      if (true && !false && true && false && true)
        System.out.println(1);
      else
        System.out.println(0);

      if (1 + 2 < 4 * 6 * (0-4) && !((0-1) < (0-2)))
        System.out.println(1);
      else
        System.out.println(0);

     }
    }
}



