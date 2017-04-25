import java.util.Arrays;

import javax.swing.plaf.synth.SynthSpinnerUI;

class play2{
	private static String argument;
	private static String argumentClone;
	private static int[] intArray;
	private static int[] intArrayClone;
	
	public static void main(String[] asdf){
		//if (asdf.length < 1) throw new IllegalArgumentException(" wtf? ");
		try {
			argument = asdf[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Provide at least one argument, please.");
		}
		
		argumentClone = new String(asdf[0]);
		
		System.out.println(
				"argument == argumentClone? " + (argument == argumentClone));

		System.out.println("argument.equals(argumentClone)? "
				+ (argument.equals(argumentClone)));
		
		intArray = new int[]{1,2,3,4,5,6};
		intArrayClone = intArray.clone();
		
		System.out.println(Arrays.toString(intArray));
		System.out.println(Arrays.toString(intArrayClone));
		
		System.out.println(
				"intArray == intArrayClone? " + (intArray == intArrayClone));

		System.out.println("intArray.equals(intArrayClone)? "
				+ (intArray.equals(intArrayClone)));
		
		System.out.println("Arrays.equals(intArray,intArrayClone)? "
				+ (Arrays.equals(intArray,intArrayClone)));
		
		System.out.println(intArray[0] == intArrayClone[0]);
		
		byte anchor = (byte) 244;
		
		for (byte i = (byte) (anchor+1);i!=anchor;i++){
			System.out.println(i & 0xFF);
		}
		
	}
	
}