import java.util.Scanner;

public class vmi 
{
	static Scanner be = new Scanner(System.in);
	
	
public static void main(String[] args)
{
	int a=0;
	while (a!=13) {
	a = be.nextInt();
	System.out.println(Integer.toString(a, 2));
	System.out.println(Integer.bitCount(a));
	
	}
}

}