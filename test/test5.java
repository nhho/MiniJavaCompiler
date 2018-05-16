// Assuming your code already works for test[0-3].java.

// Test
//   Long expression that might involve a lot of temporaries
//
// Note: Depending how you specify the production rules in the JavaCC file. 
//       There is a chance that it will take JavaCC a very long time to parse the input.
//       For example, the use of LOOKAHEAD( ... some production rule ) in JavaCC file.
class test5 {
    public static void main(String[] a){
      {
	System.out.println(
         1 + ( 2 + ( 3 + ( 4 + ( 5 + ( 6 + ( 7 + ( 8 + ( 9 
           + ( 10 + ( 11 + ( 12 + ( 13 + ( 14 + 15 )))))))))))))
        ); 

	System.out.println(
         0 - (1 - ( 2 - ( 3 - ( 4 - ( 5 - ( 6 - ( 7 - ( 8 
           - ( 9 - ( 10 - ( 11 - ( 12 - ( 13 - ( 14 - 15 ))))))))))))))
        ); 

	System.out.println(
         2 * (2 * ( 2 * ( 2 * ( 2 * ( 2 * ( 2 * ( 2 * ( 2 
           * ( 2 * ( 2 * ( 2 * ( 2 * ( 2 * ( 2 * 2 ))))))))))))))
        ); 

	System.out.println(
          (((((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1))) + (((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1)))) +
           ((((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1))) + (((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1))))
          ) +
          (((((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1))) + (((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1)))) +
           ((((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1))) + (((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1))))
          ) +
          (((((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1))) + (((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1)))) +
           ((((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1))) + (((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1))))
          ) +
          (((((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1))) + (((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1)))) +
           ((((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1))) + (((2 - 1) + (2 - 1)) + ((2 - 1) + (2 - 1))))
          ) 
        ); 
      }
    }
}



