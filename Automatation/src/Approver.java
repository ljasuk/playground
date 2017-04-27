import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Approver {
	private final File targetFile;
	private final String content;
	private final String newContent;
	
	private void writeFile(){
		try (BufferedWriter writer = Files.newBufferedWriter(targetFile.toPath())) {
		    writer.write(newContent);
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	private String repairTM(String text){
		text = text.replaceFirst("p>.+?Ericsson AB", "p>&copy; Ericsson AB");
		if (text.contains("�")){
			System.out.println("Repairing (�)");
			text = text.replace("’", "&rsquo;");	// apostrophe
			text = text.replace("→", "&rarr;");		// right arrow
			//content = content.replace("&acirc;&ldquor;&cent;", "&trade;");
			text = text.replace("™", "&trade;");	// trademark mark
			text = text.replace("®", "&reg;");		// R in circle
			text = text.replace("├", "&boxvr;");	// tree line T
			text = text.replace("─", "&boxh;");		// tree line horizontal
			text = text.replace("│", "&boxv;");		// tree line vertical
			text = text.replace("└", "&boxur;");	// tree corner UP-RIGHT
			
			text = text.replace("—", "&mdash;");	// em dash
			text = text.replace(" ", " ");			// spaces next to em dash
			System.out.println((text.contains("�")) ? "Could not repair \"�\"" : "Removed all \"�\"");
		}
		return text;
	}
	
	private String removeRev(String text){
		text = text.replaceFirst("<row>.+?dd\\.mm\\.yyyy.+?</row>", "");
		int endIndex = text.lastIndexOf("Updates for ECAS") - 85;
		System.err.println("endIndex: "+endIndex);
		int beginIndex = text.substring(0, endIndex).lastIndexOf("<tp>") +4;
		System.err.println("beginIndex: "+beginIndex);
		endIndex = text.substring(0, endIndex).lastIndexOf("</tp>");
		System.err.println("endIndex2: "+endIndex);
		String rev = text.substring(beginIndex, endIndex);
		System.err.println("rev: "+rev);
		text.replaceFirst("<rev>.+?</rev>", rev);
		return text;
	}
	
	private String modify(String text){
		text = text.replaceAll("new-page-right=\".*?\"", 
				"new-page-right=\"npr-no\"");
		text = text.replace("<rev>P(.+?)\\d</rev>", "<rev></rev>");
		text = text.replace("<date>.*?</date>", "<date><y>2017</y><m>04</m><d>27</d></date>");
		if (text.contains("dd.mm.yyyy")) text = removeRev(text);
		
		
		return text;
	}
	
	private String readContent() {
		Scanner fileIn = null;
		String fileContent = null;
		try {
			fileIn = new Scanner(targetFile);
			//System.out.println(in.useDelimiter("\\Z").hasNext());

			fileContent = fileIn.useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		} finally {
			fileIn.close();
		}
		return fileContent;
	}

	public Approver(File targetFile) {
		this.targetFile = targetFile;
		content = readContent();
		newContent = repairTM(modify(content));
		writeFile();
	}
}
