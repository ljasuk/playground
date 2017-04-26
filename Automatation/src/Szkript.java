import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Szkript {
	private static String source = "C:\\eclipse\\test.txt";
	private static String target = "C:\\eclipse\\gyujto.txt";
	private static String text;

	public static void main(String args[]) {
		//if(args.length>0) setArguments(args);
		

		Scanner fileInput = null;
		Scanner in = new Scanner(System.in);
		
		try {
			fileInput = new Scanner(new File(source));
			
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException" + e);
			e.printStackTrace();
		}
		text = fileInput.useDelimiter("\\Z").next();
		fileInput.close();
		
		whitespace();
		System.out.print("\nTables in lists: grid conversion?(y/*) ");
		if(in.next().equals("y")) replaceArr(table2grid());
		System.out.print("\nAdd signature?(y/*) ");
		if(in.next().equals("y")) addSignature();
		System.out.print("\nxml id normalization?(y/*) ");
		if(in.next().equals("y")) xmlID();
		System.out.print("\nNumeric-list to step-list conversion?(y/*) ");
		if(in.next().equals("y")) replaceArr(numList());
		System.out.print("\nConvert figures in paragraphs?(y/*) ");
		if(in.next().equals("y")) replaceArr(figures());
		System.out.print("\nConvert figures in tables to graphics?(y/*) ");
		if(in.next().equals("y")) replaceArr(tFigures());

		PrintWriter fOut = createFile(target);
		write(fOut);

		fOut.close();

	}
	
	private static void whitespace(){
		text=text.replace("<p> ", "<p>");
		text=text.replace(" </p>", "</p>");
		text=text.replace("<rf-title> ", "<rf-title>");
		text=text.replace(" </rf-title>", "</rf-title>");
		text=text.replace("<tp> ", "<tp>");
		text=text.replace(" </tp>", "</tp>");
	}
	
	private static String[][] table2grid(){
		ArrayList<String> found = new ArrayList<String>();
		ArrayList<String> replacement = new ArrayList<String>();

		Pattern checkRegex = Pattern.compile("(?s)<list-item.*?</list-item");
		Matcher regexMatcher = checkRegex.matcher(text);
		
		while (regexMatcher.find()) {
			String a = regexMatcher.group().trim();

			if (a.contains("<table")) {
				found.add(a);
				
				String b=a.replaceAll("(?s)<table.*?<tgroup", "<grid frame=\"all\">\n<tgroup");
				b=b.replaceAll("(?s)/tgroup>.*?</table>","/tgroup>\n</grid>");
				replacement.add(b);
			}
		}
		String[] f=found.toArray(new String[found.size()]);
		String[] r=replacement.toArray(new String[replacement.size()]);
		String[][] fr = {f,r};
		return fr;
	}

	private static void addSignature(){
		text=text.replaceAll("(?s)<drafted-by>.*?<name></name><signature></signature>", 
				"<drafted-by>\n<person>\n<name>Ferenc Nagy</name><signature>ENAGFER</signature>");
	}
	
	private static void xmlID(){
		text=text.replace("xml:id=\"_","xml:id=\"");
	}
	
	private static String[][] numList(){
		ArrayList<String> found = new ArrayList<String>();
		ArrayList<String> replacement = new ArrayList<String>();
		
		Pattern checkRegex = Pattern.compile("(?s)<list type=\"numeric\".*?</list>");
		Matcher regexMatcher = checkRegex.matcher(text);
		
		while (regexMatcher.find()) {
			String a = regexMatcher.group().trim();
			found.add(a);
			String b=a.replaceAll("<list .*?>","<step-list>");
			b=b.replace("/list>","/step-list>");
			b=b.replaceAll("(?s)list-item.*?>","sl-item>");
			replacement.add(b);
		}
		
		String[] f=found.toArray(new String[found.size()]);
		String[] r=replacement.toArray(new String[replacement.size()]);
		String[][] fr = {f,r};
		return fr;
		
	}
	
	private static String[][] figures(){
		ArrayList<String> found = new ArrayList<String>();
		ArrayList<String> replacement = new ArrayList<String>();
		
		Pattern checkRegex = Pattern.compile("(?s)<p>Figure.*?/figure></p>");
		Matcher regexMatcher = checkRegex.matcher(text);
		
		
		while (regexMatcher.find()) {
			String a = regexMatcher.group().trim();
			found.add(a);
			String title=a.substring(a.indexOf('.', 6)+2, a.indexOf("<figure")).trim();
			String main=a.substring(a.indexOf("<figure"), a.indexOf("/figure>")+8);
			String b=main.replace("<caption></caption>", "<caption>"+title+"</caption>");

			replacement.add(b);
		}
		
		String[] f=found.toArray(new String[found.size()]);
		String[] r=replacement.toArray(new String[replacement.size()]);
		String[][] fr = {f,r};
		return fr;
		
	}
	
	private static String[][] tFigures(){
		ArrayList<String> found = new ArrayList<String>();
		ArrayList<String> replacement = new ArrayList<String>();
		
		Pattern checkRegex = Pattern.compile("(?s)<tp><figure.*?</figure></tp>");
		Matcher regexMatcher = checkRegex.matcher(text);
		
		
		while (regexMatcher.find()) {
			String a = regexMatcher.group().trim();
			found.add(a);
			String b="<tp>\n"+a.substring(a.indexOf("<graphics"), a.indexOf("xlink\"/>")+8)+"\n</tp>";

			replacement.add(b);
		}
		
		String[] f=found.toArray(new String[found.size()]);
		String[] r=replacement.toArray(new String[replacement.size()]);
		String[][] fr = {f,r};
		return fr;
		
	}
	
 	private static void replaceArr(String[][] rf){
		for(int i=0; i<rf[0].length;i++){
			text=text.replace(rf[0][i], rf[1][i]);
		}
	}

	private static PrintWriter createFile(String filename) {
		try {
			File celFile = new File(filename);

			PrintWriter infoToWrite = new PrintWriter(new BufferedWriter(new FileWriter(celFile, false)));
			return infoToWrite;
		}

		catch (IOException e) {
			System.out.println("IO Error occurred!");
			System.exit(0);
		}
		return null;
	}

	private static void write(PrintWriter cout) {
		
		cout.println(text);
		
	}

	
}