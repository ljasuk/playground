import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class api2{
	private static String text;
	private static Scanner fileInput = null;
	private static final String source = "C:\\eclipse\\ESM_api_203.html";
	private static final String source2 = "C:\\eclipse\\ESM_API_gergo.adoc";
	
	private static List<String> h1 = new ArrayList<String>();
	private static List<String> h2 = new ArrayList<String>();
	private static List<String> h3 = new ArrayList<String>();
	
	private static void toClipBoard(){
		StringSelection selection = new StringSelection(text);
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(selection, selection);
	}
	
	private static void findLongLines() {
		String[]lines = text.split(System.getProperty("line.separator"));
		for(String tmpLine : lines){
		   if (tmpLine.length() > 72) {
			   text = text.replace(tmpLine, breakLine(tmpLine));
			   //System.out.println(breakLine(tmpLine));
		   }
		}
		
	}

	private static String breakLine(String line) {
		if (line.length() > 72) {
			int index = line.substring(0, 72).lastIndexOf(" ");
			return (line.substring(0, index) + "\n"
					+ breakLine(line.substring(index + 1)));
		}
		return line;
	}

	private static void findTitles(){
		while(fileInput.hasNextLine()){
			String line = fileInput.nextLine();

			if (line.startsWith("===")) {
				int index = line.indexOf(" ")+1;
				String rLine = line.substring(index).trim();
				String rep = titleCap.single(rLine) + "\r\n";
				//System.out.println(line);
				//if (line.contains("/")) line = line.replace("/", "a");
				if (line.contains("{")) line = line.replace("{", "\\{");
				//if (line.contains("}")) line = line.replace("/", "a");
				if (h1.contains(rLine)) {
					rep += ul("-",rLine.length())+ "\r\n";
					text = (text.replaceFirst(line, rep));
					//System.out.println(rep);
					h1.remove(rLine);
				} else 
				if (h2.contains(rLine)) {
					rep += ul("~",rLine.length())+ "\r\n";
					text = (text.replaceFirst(line, rep));
					//System.out.println(rep);
					h2.remove(rLine);
				} else
				if (h3.contains(rLine)) {
					rep += ul("^",rLine.length())+ "\r\n";
					text = (text.replaceFirst(line, rep));
					//System.out.println(rep);
					h3.remove(rLine);
				} else {
					if (fileInput.next().startsWith("[options=")) {
						text = text.replaceFirst((line+"\r\n"), ("."+rLine));
						//System.err.println(text.contains(line+"\r\n"));
					} else {
						text = text.replaceFirst(line, ("*"+rLine.trim()+"*\r\n"));
						//System.err.println(("*"+rLine+"*").trim());
					}
					
					//System.err.println(fileInput.nextLine());
				}
			}
		}
	}
	
	private static String ul(String ch, int length){
		String retVal = "";
		while(retVal.length()<length) retVal+=ch;
		return retVal;
	}
	
	private static void hListSetter(String content){
		h1 = regexChecker("<h1>.*?</h1>", content);
		h1.replaceAll(justTitle);
		//for (String a : h1) System.out.println("h1: " + a);
		
		h2 = regexChecker("<h2>.*?</h2>", content);
		h2.replaceAll(justTitle);
		//for (String a : h2) System.out.println("h2: " + a);
		
		h3 = regexChecker("<h3>.*?</h3>", content);
		h3.replaceAll(justTitle);
		//for (String a : h3) System.out.println("h3: " + a);
	}
	
	private static UnaryOperator<String> justTitle = new UnaryOperator<String>(){
		public String apply(String a){
			if (a.contains("’")) a = a.replace("’", "'");
			int beginIndex = a.indexOf(". ") + 2;
			int endIndex = a.indexOf("</");
			String b = a.substring(beginIndex, endIndex).trim();
			if (b.charAt(b.length()-1)=='.') b = b.substring(0, b.length()-2);
			if (b.contains("  ")) b = b.replace("  ", " ");
			return b;
		}
	};
	
	private static ArrayList<String> regexChecker(String theRegex, String text) {
		ArrayList<String> found = new ArrayList<String>();

		Pattern checkRegex = Pattern.compile(theRegex);

		Matcher regexMatcher = checkRegex.matcher(text);

		while (regexMatcher.find()) {
			String a = regexMatcher.group().trim();
			if (regexMatcher.group().length() != 0 && !found.contains(a)) {
				if (a.charAt(1) == '/')
					found.remove("<" + a.substring(2));
				else
					found.add(regexMatcher.group().trim());
			}
		}

		return found;
	}
	
	public static Scanner setInput(String fileName){

		try {
			return new Scanner(new File(fileName));
			
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException" + e);
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){
		
		fileInput = setInput(source);	
		String content = fileInput.useDelimiter("\\Z").next();
		hListSetter(content);
		
		fileInput = setInput(source2);
		text = (fileInput.useDelimiter("\\Z").next());
		
		fileInput = setInput(source2);
		findTitles();
		
		findLongLines();
		text = text.replace(" ESM", " Security Manager");
		toClipBoard();
		//System.out.println(text);
		//System.out.println(h2);
		
	}
}