import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//import java.nio.file.Files;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CheckAll {
	private static Scanner fileInput = null;
	private static File target = new File("\\\\vhub.rnd.ki.sw.ericsson.se\\home\\xkovger\\Downloads\\xmlDocs.html");
	private static File folder = new File("\\\\vhub.rnd.ki.sw.ericsson.se\\home\\xkovger\\Downloads\\doc");
	private static XMLDoc[] files;
	
	
	private static void write(PrintWriter cout) {
		cout.println("<!DOCTYPE html>\n<html>\n<body>\n<table border=1>");
		
		//header
		cout.println("<tr>");
		cout.println("<td><b>title</b></td>");
		cout.println("<td><b>docNo</b></td>");
		cout.println("<td><b>rev</b></td>");
		cout.println("<td><b>sig</b></td>");
		cout.println("<td><b>(TM)</b></td>");
		cout.println("</tr>");
		
		for (int i = 0; i < files.length; i++) {
			cout.println(files[i].getTableLine());
			
		}
		cout.println("</table>\n</body>\n</html>");
		
	}
	
	private static PrintWriter createFile(File celFile) {
		try {
	
			PrintWriter infoToWrite = new PrintWriter(new BufferedWriter(new FileWriter(celFile, false)));
			return infoToWrite;
		}
	
		catch (IOException e) {
			System.out.println("IO Error occurred!");
			System.exit(0);
		}
		return null;
	}
	
	public static void main(String[] args) {
		File[] listOfFiles = folder.listFiles();
		files = new XMLDoc[listOfFiles.length];
		for (int i = 0; i < files.length; i++) {
			try {
				fileInput = new Scanner(listOfFiles[i]);
				
			} catch (FileNotFoundException e) {
				System.out.println("FileNotFoundException" + e);
				e.printStackTrace();
			}
			String text = fileInput.useDelimiter("\\Z").next();
			files[i] = new XMLDoc(text);
			fileInput.close();
		}
		
		
		PrintWriter fOut = createFile(target);
		write(fOut);

		fOut.close();
		try {
			Desktop.getDesktop().open(target);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class XMLDoc {
		private final String content;
		private final String rev;
		private final String title;
		private final String docNo;
		private final String[] sig = new String[2];
		private final boolean hasTM;
		private final String tableLine;
		
		private String makeTableLine(){
			StringBuilder table = new StringBuilder(200);
		
			table.append("<tr>\n");
			table.append("<td>"+title+"</td>\n");
			table.append("<td>"+docNo+"</td>\n");
			table.append("<td>"+rev+"</td>\n");
			table.append("<td>"+sig[1]+"</td>\n");
			table.append("<td>"+(hasTM ? "X" : "")+"</td>\n");
			table.append("</tr>\n");
			
			return table.toString();
		}
		
		private static String betweenTags(String tag, String where){
			String regex = "<"+tag+".*?>.*?</"+tag+">";
			String raw = findFirst(regex, where);
			if (raw==null) return "";
			int beginIndex = raw.indexOf('>') + 1;
			int endIndex = raw.lastIndexOf('<');
			return raw.substring(beginIndex, endIndex);
		}
		
		private static String findFirst(String regex, String where){
			Matcher matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(where);
			if (matcher.find()) return matcher.group();
			return null;
		}
	
		public XMLDoc(String content) {
			this.content = content;
			int beginIndex = content.indexOf("<meta-data>");
			int endIndex = content.indexOf("</meta-data>");
			String metadata = content.substring(beginIndex, endIndex);
			docNo = betweenTags("doc-no", metadata);
			title = betweenTags("title", metadata);
			System.out.println("Title: " + title);
			System.out.println("Document Number: "+docNo);
			rev = betweenTags("rev", metadata);
			System.out.println("Revision: " + rev);
			String drafted = betweenTags("drafted-by", metadata);
			sig[0] = betweenTags("name", drafted);
			sig[1] = betweenTags("signature", drafted);
			hasTM = content.contains("™");/*content.contains("&acirc;&ldquor;&cent;")||*/
			tableLine = makeTableLine();
		}
	
		public String getTableLine() {
			return tableLine;
		}
		
		
	}
}