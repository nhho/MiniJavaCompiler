class Test3 {
    public static void main(String[] a){
      int x;
      int y;
      {
        while (x < 10) {
          x = x + 1;
          y = y + 2;
        }

	System.out.println(x);
	System.out.println(y);
      }
    }
}



