import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
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
		// check for wrong characters
		if (newContent.contains("â")) System.out.println("ENCODING ERROR");
	}
	
	private String removeRev(String text){
		text = text.replaceFirst("(?s)<row>.{1,200}yyyy.+?</row>", "");
		String rev = "";
		Matcher revMatcher = Pattern.compile("<tp>([A-Z]{1,2})</tp>").matcher(text);
		while (revMatcher.find()) rev = revMatcher.group(1);
		//System.out.println(rev);
		text = text.replaceFirst("<rev>.+?</rev>", ("<rev>"+rev+"</rev>"));
		System.out.println("Modified to revision: "+rev);
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

	public Approver(File targetFile) {
		this.targetFile = targetFile;
		content = byteRead();
		newContent = modify(content);
		writeFile();
	}

	public Approver(String content) {
		this.content = content;
		newContent = modify(content);
		targetFile = null;
	}
	
	public static String apply(String content){
		return new String(new Approver(content).newContent);
	}
	
}
