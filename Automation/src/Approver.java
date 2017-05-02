import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
			text = text.replace("→", "&rarr;");	// right arrow
			text = text.replace("™", "&trade;");	// trademark mark
			text = text.replace("®", "&reg;");		// R in circle
			text = text.replace("├", "&boxvr;");	// tree line T
			text = text.replace("─", "&boxh;");	// tree line horizontal
			text = text.replace("│", "&boxv;");	// tree line vertical
			text = text.replace("└", "&boxur;");	// tree corner UP-RIGHT
			text = text.replace("—", "&mdash;");	// em dash
			text = text.replace(" ", " ");		// spaces next to em dash
			System.out.println((text.contains("�")) ? "Could not repair \"�\"" : "Removed all \"�\"");
		}
		return text;
	}
	
	private String removeRev(String text){
		text = text.replaceFirst("(?s)<row>.{1,200}yyyy.+?</row>", "");
		String rev = "";
		Matcher revMatcher = Pattern.compile("<tp>([A-Z]{1,2})</tp>").matcher(text);
		while (revMatcher.find()) rev = revMatcher.group(1);
		//System.out.println(rev);
		text = text.replaceFirst("<rev>.+?</rev>", ("<rev>"+rev+"</rev>"));
		System.out.println(targetFile.getName()+": "+rev);
		return text;
	}
	
	private String modify(String text){
		text = text.replaceFirst("new-page-right=\".*?\"", 
				"new-page-right=\"npr-no\"");
		text = text.replaceFirst("<rev>P(.+?)\\d</rev>", "<rev>$1</rev>");
		
		LocalDateTime now = LocalDateTime.now();
		int year = now.getYear();

		String month = String.format("%02d", now.getMonthValue());
		String day = String.format("%02d", now.getDayOfMonth());

		text = text.replaceFirst("<date>.*?</date>", 
				"<date><y>"+year+"</y><m>"+month+"</m><d>"+day+"</d></date>");
		text = text.replace("<approved-by approved=\"no\">","<approved-by approved=\"yes\">");
		text = text.replaceFirst("<subtitle>.*?</subtitle>","<subtitle>Ericsson Certificate Administration Server</subtitle>");
		text = text.replaceFirst("<confidentiality.+?/>","<confidentiality class=\"ericsson-internal\"/>");
		
		text = text.replaceFirst("(?s)<drafted-by>.*?</drafted-by>",
				"<drafted-by>\n<person>\n<name>Juha Ritvanen</name>\n"+
						"<signature>LMFJURI</signature>\n<location/>"+
						"<company/><department/>\n</person>\n</drafted-by>");
		if (text.contains("yyyy")) text = removeRev(text);
		
		//System.err.println(text.substring(0, 5300));
		return text;
	}
	
	private String byteRead() {
		String text = null;
		try {
			FileInputStream fis = new FileInputStream(targetFile.getPath());
			byte[] data = new byte[(int) targetFile.length()];
			fis.read(data);
			fis.close();
			text = new String(data, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		content = byteRead();
		newContent = repairTM(modify(content));
		writeFile();
	}
	
}
