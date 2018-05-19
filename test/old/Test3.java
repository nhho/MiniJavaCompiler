class Test3 {
    public static void main(String[] a){
      int x;
      int y;
      {
        x = 0;
        y = 0;
        while (x < 10) {
          x = x + 1;
          y = y + 2;
        }

	System.out.println(x);
	System.out.println(y);
      }
    }
}



