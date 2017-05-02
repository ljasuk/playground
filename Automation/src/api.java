import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class api{
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
	
	private static void findTitles(){
		while(fileInput.hasNextLine()){
			String line = fileInput.nextLine();
			if (line.startsWith("===")) {
				int index = line.indexOf(" ")+1;
				String rLine = line.substring(index).trim();
				//System.out.println(line);
				if (h1.contains(rLine)) {
					text = (text.replace(line, (rLine + "\r\n" + ul("-",rLine.length())+ "\r\n")));
					//System.out.println(rLine + "\r\n" + ul("-",rLine.length()));
					h1.removeAll(Collections.singleton(rLine));
				} else 
				if (h2.contains(rLine)) {
					System.err.println("asdf");
					text = (text.replace(line, (rLine + "\r\n" + ul("~",rLine.length())+ "\r\n")));
					//System.out.println(rLine + "\r\n" + ul("~",rLine.length()));
					System.err.println("asdf");
					//System.out.println(h2.removeAll(Collections.singleton(rLine)));
				} else
				if (h3.contains(rLine)) {
					text = (text.replace(line, (rLine + "\r\n" + ul("^",rLine.length())+ "\r\n")));
					//System.out.println(rLine + "\r\n" + ul("^",rLine.length()));
					h3.remove(rLine);
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
		
		//h1 = new ArrayList();
		findTitles();
		toClipBoard();
		//System.out.println(text);
		//System.out.println(h2);
		
	}
}