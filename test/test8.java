// Test
//   if (e1 < e2) s1 else s2 (with constant values only)
class test8 {
    public static void main(String[] a){
     { 
      if (1 < 2)
        System.out.println(1);
      else
        System.out.println(0);

      if (2 < 2) 
        System.out.println(1);
      else
        System.out.println(0);

      if ((0-100) < 3)
        System.out.println(1);
      else
        System.out.println(0);

      if ((0-3) < (0-100))
        System.out.println(1);
      else
        System.out.println(0);
     }
    }
}



