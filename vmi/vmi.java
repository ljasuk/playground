import java.util.Scanner;

public class vmi 
{
	static Scanner beolv = new Scanner(System.in);
	/*public static*/ int tipp=1;
	public static int randomszam=(int)(Math.random()*50);
	public static int recur(int a)
	{
		System.out.println(a);
		if (a!=0) {
			a+=recur(a/2);
			
		}
		return a;
	}
	
	public static int Egyezike(int v)
	{
		if (randomszam==recur(v)) 
				{
			return -1;
				} else 
					{System.out.println("nem jo");
					return 0;
					}
	}
	
	
public static void main(String[] args)
{
	System.out.print("ide irjal valamit: ");
	//int b = beolv.nextByte();
	System.out.println(recur(beolv.nextInt()));
	System.out.println("rnd: "+randomszam);
	System.out.println("tippelj: ");
	while (Egyezike(beolv.nextInt())!=-1) System.out.println("ujra: ");
	System.out.println("jol van");
}

}