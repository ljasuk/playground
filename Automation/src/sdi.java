//import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
/*import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;*/
//import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class sdi {
	private static String text;
	private static Scanner fileInput = null;
	private static final String source = "C:\\eclipse\\33docs.txt";
	private static final String source2 = "C:\\eclipse\\test.txt";
	//private static final String target = "C:\\eclipse\\gyujto.txt";
	
	private static String pairs[][] = new String[57][2];
	
	public static void main(String[] a) {
		
		
		
		
		try {
			fileInput = new Scanner(new File(source));
			
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException" + e);
			e.printStackTrace();
		}
		
		pairs = getPairs();
		
		/*for (String[] p : pairs) {
			System.out.println(p[0]+" - "+p[1]);
		}*/
		
		try {
			fileInput = new Scanner(new File(source2));
			
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException" + e);
			e.printStackTrace();
		}
		text = fileInput.useDelimiter("\\Z").next();
		text = text.replaceAll("<tp> </tp>", "");
		text = text.replaceAll("<caption> </caption>", "<caption></caption>");
		/*text = text.replaceAll("\\n", "");
		text = text.replaceAll("\\r", " ");
		text = text.replaceAll("3.2", "3.3");
		text = text.replaceAll("3.1", "3.2");*/
		//text = text.replaceAll(">", ">\r");
		fileInput.close();
		
		replaceArr(tableFormat());
		
		//System.out.println(text);
		
		swap();
		
		/*PrintWriter fOut = createFile(target);
		fOut.println(text);*/
		System.out.println(text);
		
		StringSelection selection = new StringSelection(text);
	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clipboard.setContents(selection, selection);
		
		/*PrintWriter fOut = createFile(target);
		write(fOut, i);

		fOut.close();
		try {
			Desktop.getDesktop().open(new File(target));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		
	}
	
	/*private static PrintWriter createFile(String filename) {
		try {
			File celFile = new File(filename);

			PrintWriter infoToWrite = new PrintWriter(new BufferedWriter(new FileWriter(celFile, false), 20000));
			return infoToWrite;
		}

		catch (IOException e) {
			System.out.println("IO Error occurred!");
			System.exit(0);
		}
		return null;
	}*/
	
		
	
	private static String[][] getPairs() {
		int i = 0;
		String pairList[][] = new String[57][2];
		String[] pairTemp;
		while(fileInput.hasNext()){
			pairTemp = fileInput.nextLine().split("\\t");
			pairTemp[0] = pairTemp[0].replaceAll("\"", "").trim();
			pairTemp[1] = pairTemp[1].replace("P", "");
			pairTemp[1] = pairTemp[1].replaceFirst("\\d", "");
			pairList[i] = pairTemp;
			i++;		
			//System.out.println(pairTemp[0]+" - "+pairTemp[1]);
		}
		return pairList;
	}
	
	private static void swap() {
		//int i=0;
		for (String[] p : pairs) {
			if (!text.contains(p[0])) {
				System.out.println(p[0]/*+" rev: "+p[1]*/);
				continue;}
			int index = text.indexOf(p[0]);
			//System.out.println((i++)+": "+p[0]+" "+index);
			index = text.indexOf("Uen", index);
			int endIndex = text.indexOf("</tp", index);
			text=text.substring(0,index-1)+" Uen "+p[1]+text.substring(endIndex);
		}

	}
	
	private static String[][] tableFormat(){
		ArrayList<String> found = new ArrayList<String>();
		ArrayList<String> replacement = new ArrayList<String>();

		Pattern checkRegex = Pattern.compile("(?s)<tp.*?</tp");
		Matcher regexMatcher = checkRegex.matcher(text);
		
		while (regexMatcher.find()) {
			String a = regexMatcher.group().trim();

			
			found.add(a);

			String b=replaceSpaces(a);
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
	
	private static String replaceSpaces(String t) {
		t = t.replaceAll("\\n", "");
		t = t.replaceAll("\\r", " ");
		t = t.replaceAll("3.2", "3.3");
		t = t.replaceAll("3.1", "3.2");
		//t = t.replaceAll(">", ">\r");
		return t;
	}
}