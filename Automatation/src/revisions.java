import java.util.Arrays;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.Desktop;

public class revisions {
	private static aDoc[] files;
	private static Scanner fileInput = null;
	private static File target = new File("E:\\xkovger\\ecasDocs.html");
	private static File folder = new File("E:\\xkovger\\ecas\\doc\\");
	//private static final String target = "E:\\xizssoo\\ecasDocs.html";
	//private static final String directory = "E:\\xizssoo\\ecas\\doc\\";
	//private static File target = new File("C:\\eclipse\\ecasDocs.html");
	//private static File folder = new File("C:\\eclipse\\adoc\\");
	private final static FilenameFilter adocExt = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.toLowerCase().endsWith(".adoc");
		}
	};
	private final static FilenameFilter incExt = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.toLowerCase().endsWith(".inc");
		}
	};
	
	private static final String[] LOOK_FOR = new String[]{
			"(TM)",
			"dd.mm.yyyy",
			"Glossary of Terms and Acronyms"
	};
	
	private static final String[] ABBREV = new String[]{
			"(TM)",
			"nextR",
			"glossary"
	};
	
	private static void write(PrintWriter cout, int x) {
		cout.println("<!DOCTYPE html>\n<html>\n<body>\n<table border=1>");
		
		//header
		cout.println("<tr>");
		cout.println("<td><b>title</b></td>");
		cout.println("<td><b>doctype</b></td>");
		cout.println("<td><b>docNo</b></td>");
		cout.println("<td><b>rev</b></td>");
		for (int i = 0; i < ABBREV.length; i++) {
			cout.println("<td><b>"+ABBREV[i]+"</b></td>");
		}
		cout.println("</tr>");
		
		for (int i = 0; i < x; i++) {
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

	private static void parseArgs(String[] args) {
		String full = String.join(" ",args).replace("\\", "\\\\")+"\\\\";
		System.out.println(full);
		folder = new File(full);
		int endIndex = full.substring(0, full.length()-2).lastIndexOf('\\');
		target = new File(full.substring(0, endIndex)+"\\ecasDocs.html");
		System.out.println(target);
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			//System.out.println("args: "+Arrays.toString(args));
			parseArgs(args);
		}
		File[] listOfFiles = folder.listFiles(adocExt);
		
		files = new aDoc[listOfFiles.length];
		
		int i=0;
		for (File file : listOfFiles) {
			if (file.isFile()) {
				//if(!file.getName().endsWith(".adoc")) continue;
				try {
					fileInput = new Scanner(file);
					
				} catch (FileNotFoundException e) {
					System.out.println("FileNotFoundException" + e);
					e.printStackTrace();
				}
				files[i] = new aDoc(fileInput, LOOK_FOR);

				i++;
				
			}
			
		}
		fileInput.close();
		PrintWriter fOut = createFile(target);
		write(fOut, i);

		fOut.close();
		try {
			Desktop.getDesktop().open(target);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	private static class aDoc {
		final private String title;
		final private String rev;
		final private String docNo;
		final private String type;
		final private boolean[] found;
		final private String tableLine;
		
		aDoc (Scanner fileInput, String[] lookFor){
			found = new boolean[lookFor.length];
			title=fileInput.nextLine();
			fileInput.nextLine();
			rev=fileInput.nextLine().substring(12).trim();
			
			type=fileInput.nextLine().substring(10).trim().toLowerCase().replaceAll("\\b(.)", ("$1").toUpperCase());
			docNo=fileInput.nextLine().substring(8).trim();
			String rest=fileInput.useDelimiter("\\Z").next();
			//System.err.println(rest.length());
			for (int i = 0; i < lookFor.length; i++) {
				found[i] = rest.contains(lookFor[i]);
			}
			tableLine = makeTableLine();
		}
		
		String makeTableLine(){
			StringBuilder table = new StringBuilder(200);
		
			table.append("<tr>\n");
			table.append("<td>"+title+"</td>\n");
			table.append("<td>"+type+"</td>\n");
			table.append("<td>"+docNo+"</td>\n");
			table.append("<td>"+checkRev(rev)+"</td>\n");
			for (boolean f : found) {
				table.append("<td>"+(f ? "X" : "")+"</td>\n");
			}
			table.append("</tr>\n");
			
			return table.toString();
		}
		
		private String checkRev(String revision){
			char[] skipLetters = new char[]{'I','O','Q','R','W'};
			boolean OK = true;
			for (char letter : skipLetters) {
				if (revision.indexOf(letter)>=0) OK = false;
			}
			if (OK) return revision; else return ("<b>"+revision+"</b>");
			
		}
		
		public synchronized String getTableLine() {
			return tableLine;
		}
		
	}
}