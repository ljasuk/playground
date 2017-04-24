import java.util.*;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileFilter;
import java.lang.*;

class Holder{
	private int value;

	synchronized void outValue() {
		System.err.println(value);
	}

	synchronized void setValue(int value) {
		this.value = value;
	}

	Holder(int content) {
		this.value = content;
	}
	
	synchronized void increment(){
		value++;
		this.notifyAll();
	}
	
	synchronized void print(int min) throws InterruptedException{
		while(value<min){
			wait();
		}
		
		System.out.println("finally "+value+" ni natta, ne!");
	}
	
	
	
}



public class ujabb {
	static Holder ob;
	
	private static Runnable asd = new Runnable(){
		public void run(){
			
			try {
				ob.print(15);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	};
	
	private static Runnable qwe = new Runnable() {
		public void run(){
			while (true) {
				ob.increment();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					//throw new Exception("interrupted", e);
					e.printStackTrace();
				}
				ob.outValue();
			}
		}
	};
	
	
	
	public static void main(String[] asdf){
		/*ob = new Holder(12);
		new Thread(asd).start();
		new Thread(qwe).start();*/
		
		/*Scanner in = new Scanner(System.in);
		
		ListOfNumbers lon = new ListOfNumbers();
		lon.readList("C:\\gyujto.txt");
		File targetFile = lastFileModified("D:\\Downloads");
		System.out.println(targetFile.getPath());
		System.out.println(Pattern.quote(in.nextLine()));
		in.close();*/
		Node first = fibNode.newInstance(16);
		eval fibEval = new FibValuator(16);
		nodeFilter fibFilter = nodeFilter.newInstance(first, fibEval);
		Node current = fibFilter.getFilteredFirst();
		while (current!=null){
			System.out.println(current.getValue());
			current = current.next();
		}
		
	}
    	
}