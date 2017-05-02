import java.io.*;

import java.util.*;
import java.util.regex.Matcher;
//import javax.swing.text.Document;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class htmlReader{
	private static String[][] texts;
	private static PrintWriter fOut;
	private static final String target = "H:\\gyujto.txt";
	private static final String URLlist = "H:\\URLlist.txt";
	private static final String regEx = "<.*?>";
	
	public static void main(String[] args) {
		/*String html = "<html><head><title>First parse</title></head>"
				  + "<body><p>Parsed HTML into a doc.</p></body></html>";*/
		
		ArrayList<String> URLs = readFile(URLlist);
		texts = new String[URLs.size()][2];
		int j = 0;
		for (String b:URLs) {
			texts[j++]=readURL(b);
			//String x=readURL(b);
			//System.out.println(texts[j-1][0]+": "+texts[j-1][1]);
		}
		if (args.length!=0) fOut = createFile("H:\\"+args[0]+".txt"); else
		fOut = createFile(target);
		
		url2file();
		
		fOut.close();

	}
	
	private static ArrayList<String> readFile(String target){
		
		File celFile = new File(target);
		ArrayList<String> beolvasott = new ArrayList<String>();
		
		try{
			BufferedReader fIn = new BufferedReader(
					new FileReader(celFile));
			
			String line = fIn.readLine();
			
			while(line!=null){
				//System.out.println(line);
				/*if(!line.equals("1"))*/
				beolvasott.add(line);
				line = fIn.readLine();
			}
			fIn.close();
			
		}
		
		catch(FileNotFoundException e){
			System.out.println("nincs file");
			System.exit(0);
		}
		
		catch(IOException a) {
			System.out.println("IO Hiba");
			System.exit(0);
		}
		return beolvasott;
		
	}
	
	private static String[] readURL(String URL){
		String[] r = new String[2];
		try {
			Document doc = Jsoup.connect(URL).get();
			r[0]=doc.title();
			r[1] = doc.text();
			
		} catch (IOException e1) {
			System.out.println(e1);
			e1.printStackTrace();
		}
		return r;
	}
	
	private static void url2file(){
		ArrayList<String> variables;
		for (String[] text : texts){
			System.out.println(text[0]+": "+text[1]);
			variables =regexChecker2(regEx,text[1]);
			write2File(variables,text[0]);
		}
	}
	
	private static ArrayList<String> regexChecker2(String theRegex, String str2Check){
		ArrayList<String> found = new ArrayList<String>();
		
		Pattern checkRegex = Pattern.compile(theRegex);
		
		Matcher regexMatcher = checkRegex.matcher(str2Check);
		System.out.println(str2Check);
		while(regexMatcher.find()){
			String a = regexMatcher.group().trim();
			if(regexMatcher.group().length()!=0&&!found.contains(a)){if(a.charAt(1)=='/') found.remove("<"+a.substring(2)); else
				found.add(regexMatcher.group().trim());}
		}
		
		//System.out.println(found);
		
		return found;
	}

	
	private static PrintWriter createFile(String filename){
		try {
			File celFile = new File(filename);
			
			PrintWriter infoToWrite = new PrintWriter(
					new BufferedWriter(
							new FileWriter(celFile, true)));
			return infoToWrite;
		}
		
		catch(IOException e){
			System.out.println("IO Error occurred!");
			System.exit(0);
		}
		return null;
	}
	
	private static void write2File(ArrayList<String> a, String cim){
		fOut.println(cim);
		//System.out.println(a);
		for (String b:a){
			fOut.println(b);
			//System.err.println(b);
		}
		fOut.println();
	}
	
	
	
}