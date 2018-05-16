class Test4 {
    public static void main(String[] a){
      int x;
      int a1;
      int a2;
      int a3;
      int a4;
      int a5;
      int a6;
      int a7;
      int a8;
      int a9;
      int a10;
      int a11;
      int a12;
      {
        a1 = 1; a2 = 2; a3 = 3; a4 = 4;
        a5 = 100; a6 = 200; a7 = 300; a8 = 400;
        a9 = 10000; a10 = 20000; a11 = 30000; a12 = 40000;

        x = a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9 + a10 + a11 + a12 -
            (a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9 + a10 + a11) * 1 +
            (a1 + 1 - a1 - 1 + a1 + 1 - a1 - 1 + 9);

	System.out.println(x);

        x = 5 * 2 + 4 - 10 * (0 - 1) * 5;
	System.out.println(x);
      }
    }
}



