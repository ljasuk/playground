import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.NullPointerException;

class Play4{
	private static Scanner cin;
	
	private static void end(){
		System.out.println("This is the end.");
		//System.exit(0);
	}
	
	private static int read() {
		Scanner userInput = new Scanner(System.in);
		try {
			int a = cin.nextInt();
			return a;
		} catch (InputMismatchException e){
			throw new NullPointerException("input wrong", (Throwable) e);
			return 0;
		} finally {
			userInput.close();
		}

	}
	
	public static void main(String[] asdf){
		Scanner cin = new Scanner(System.in);
		int a = 0;
		try {
			a = Integer.parseInt(cin.next());
		} catch (InputMismatchException e) {
			return;
		} finally {
			System.out.println("over and out");
		}
		System.out.println(a*2);
	}
}