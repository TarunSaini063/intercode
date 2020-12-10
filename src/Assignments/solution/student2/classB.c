package files.solution.student2;

// java program to demonstrate
// need of try-catch clause
import java.util.Arrays;
import java.util.Scanner;

class GFG {
	public static void main (String[] args) {

		// array of size 4.
		int[] arr = new int[4];

		/*Taking input in an array(elements in same line) without knowing the size

		 * ArrayList<Integer> a = Arrays.stream(scanner.nextLine().split("\\s+"))
            							.map(Integer::parseInt)
            							.collect(Collectors.toCollection(ArrayList::new));
		*/

		// this statement causes an exception
		try{
			int i = arr[4];
		}
		catch(Exception e)
		{
			System.out.println(e+" ll **");
		}
		// the following statement will never execute if try catch is not there
		System.out.println("Hi, I want to execute");


		// Boxing is the conversion of primitive types to objects of corresponding wrapper classes.
		// Unboxing is the reverse process. The following code illustrates both processes:
		// https://www.geeksforgeeks.org/comparison-autoboxed-integer-objects-java/
		int primitive = 100;
		Integer reference = Integer.valueOf(primitive); // boxing
		int anotherPrimitive = reference.intValue();    // unboxing

		// Autoboxing and auto-unboxing are automatic conversions performed by the Java compiler.

		double primitiveDouble = 10.8;
		Double wrapperDouble = primitiveDouble; // autoboxing
		double anotherPrimitiveDouble = wrapperDouble; // auto-unboxing

		Long i1 = Long.valueOf("2000");
		Long i2 = Long.valueOf("2000");
		System.out.println(i1 == i2);      // false
		System.out.println(i1.equals(i2)); // true

		// Autoboxing works only
		// when the left and the right parts of an assignment have the same type.
		// In other cases, you will get a compilation error.

		Long n1 = 10L; // OK, assigning long to Long
		Integer n2 = 10; // OK, assigning int to Integer

		// Long n3 = 10; // WRONG, compilation error,  assigning int to Long
		// Integer n4 = 10L; // WRONG, compilation error, assigning long to Integer


		String s="ab";
		System.out.println(s);
		s = s+"cd";
		System.out.println(s);
		s = "cd";
		System.out.println(s);

		double d = 20.3;
		System.out.println(d);

		// here long[] longNumbers = {1000_000_000_01, 100000000002, 100000000003};
		// i.e. numbers without l at end will not compile
		long[] longNumbers = {1000_000_000_01l, 100000000002l, 100000000003l};
        System.out.println(Arrays.toString(longNumbers));

		final double PI = 3.1415;
		// PI = 3.1416; // compile error line

		// 3 and 5 bekow will cause compile error
		final Scanner scanner = new Scanner(System.in); // 1
		final int a;                                    // 2
		// final int b = scanner.nextInt() + a;            // 3
		final int c = 0;                                // 4
		// c = b;                                          // 5
		System.out.println(c + 1);                      // 6


		final double X;
		// System.out.println(X); // compile error line

        // here the commented array delaration+instantiation will not compile
        // char[] array1 = new char[10000000000000];
		char[] array2 = new char[0];
		char[] array3 = new char[-1]; // this compiles gives NegativeArraySizeException at runtime
		char[] array4 = { 'a', 'b', 'c', 'd' };
		char[] array5 = new char[1];

	}
}