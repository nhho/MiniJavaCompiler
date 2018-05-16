// Test
//   if (e1 && e2) s1 else s2 (with constant values only)
class test7 {
    public static void main(String[] a){
     { 
      if (true && true)
        System.out.println(1);
      else
        System.out.println(0);

      if (false && true)
        System.out.println(1);
      else
        System.out.println(0);

      if (false && false)
        System.out.println(1);
      else
        System.out.println(0);

      if (true && false)
        System.out.println(1);
      else
        System.out.println(0);
     }
    }
}



