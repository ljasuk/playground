import java.util.*;
//import java.util.function;

interface IntUnaryOperator {
	public int pek(int a);
}

interface IntConsumer{
	public void out(int x);
}

class ujabb {
	
	static int cout(int d, IntUnaryOperator c){
		return c.pek(d);
	}
	
	static void ki(int f, IntConsumer g) {
		g.out(f);
	}
	
public static void main(String[] args) {	
	Scanner cin = new Scanner(System.in);
	/*Hopp egy = new Hopp(){
		public int pek(int a) {return a*(a-1);}
	};*/
	System.out.println("ide: ");
	int b = cin.nextInt();
	ki(cout(b, e->{if (e>1)return e*(e-1); else return e*(e+1);}),a->{System.out.println(a);});
	//cout(b, e->{if (e>1)return e*(e-1); else return e*(e+1);});
}}