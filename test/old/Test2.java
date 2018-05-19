class Test2 {
    public static void main(String[] a){
      int x;
      int y;
      {
        y = 0;
        x = 10 + y;
        y = x + 6;
	System.out.println(x);
	System.out.println(y);
      }
    }
}



