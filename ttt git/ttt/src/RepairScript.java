//import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
//import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.Reader;
import java.util.Arrays;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.nio.file.Files;
//import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.*;

class RepairScript {

	private static File targetFile;
	private static File workDir = new File("C:\\Users\\Koger\\Downloads");
	private static String content;
	private static String rev;
	private static String[] sig = new String[2];
	private static Scanner in;
	//private static Reader CLInput;
	
	private static void writeFile(){
		try (BufferedWriter writer = Files.newBufferedWriter(targetFile.toPath())) {
		    writer.write(content);
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	private static void addSignature(){
		final String[][] SIGNATURES = new String[][]{
			{"Gergely Kovacs","XKOVGER"},
			{"Izsak Soos","XIZSSOO"},
			{"Ferenc Nagy","ENAGFER"},
		};
		
		if (sig[1].length()<3){
			System.out.println("\nNo signature!\nAdd signature?");
		} else {
			System.out.println("\nSignature: " + Arrays.toString(sig));
			System.out.println("Change signature?");
		}
		
		System.out.println("0 NO");
		for (int i = 0; i < SIGNATURES.length; i++) {
			System.out.println((i+1) + " " + Arrays.toString(SIGNATURES[i]));
		}
		
		int choice = 0;
		/*try {
			choice = CLInput.read()-48;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		choice = in.nextInt()-1;
		if (choice<0) return;
		
		//System.out.println(Arrays.toString(SIGNATURES[choice]));
		int beginIndex = content.indexOf("<drafted-by>")+12;
		int endIndex = content.indexOf("</drafted-by>");
		beginIndex = content.indexOf("<name>",beginIndex)+6;
		endIndex = content.indexOf("</signature>",beginIndex);
		//System.out.println(content.substring(beginIndex, endIndex));
		content = content.substring(0,beginIndex) + 
				SIGNATURES[choice][0] + "</name><signature>" + 
				SIGNATURES[choice][1] +
				content.substring(endIndex);
		//System.out.println(content.substring(0,1300));
	}
	
	private static String betweenTags(String tag, String where){
		String regex = "<"+tag+".*?>.+?</"+tag+">";
		String raw = findFirst(regex, where);
		int beginIndex = raw.indexOf('>') + 1;
		int endIndex = raw.lastIndexOf('<');
		return raw.substring(beginIndex, endIndex);
	}
	
	private static String findFirst(String regex, String where){
		Matcher matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(where);
		if (matcher.find()) return matcher.group();
		return ("NO MATCH FOR: "+regex);
	}
	
	/*public static String find(String regex){
		Matcher matcher = (new Pattern(regex)).matcher(content);
	}*/
	
	private static void readMetadata(){
		int beginIndex = content.indexOf("<meta-data>");
		int endIndex = content.indexOf("</meta-data>");
		String metadata = content.substring(beginIndex, endIndex);
		String docNo = betweenTags("doc-no", metadata);
		
		String title = betweenTags("title", metadata);
		System.out.println("Title: " + title);
		System.out.println("Document Number: "+docNo);
		rev = betweenTags("rev", metadata);
		System.out.println("Revision: " + rev);
		String drafted = betweenTags("drafted-by", metadata);
		sig[0] = betweenTags("name", drafted);
		sig[1] = betweenTags("signature", drafted);
		
		
	}
	
	private static void clearSpaces(){
		content = content.replaceAll("<p>\\s+", "<p>");
		content = content.replaceAll("\\s+</p>", "</p>");
		content = content.replaceAll("<tp>\\s+", "<tp>");
		content = content.replaceAll("\\s+</tp>", "</tp>");
		content = content.replaceAll("<rf-title>\\s+", "<rf-title>");
		content = content.replaceAll("\\s+</rf-title", "</rf-title");
		content = content.replace("xml:id=\"_", "xml:id=\"");
	}

	/*private static void readLines() {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(targetFile.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder contentBuilder = new StringBuilder();
		for (String l : lines) {
			contentBuilder.append(l);
		}
		content = contentBuilder.toString();
	}*/

	private static void readContent() {
		Scanner fileIn = null;
		try {
			fileIn = new Scanner(targetFile);
			//System.out.println(in.useDelimiter("\\Z").hasNext());

			content = fileIn.useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		} finally {
			fileIn.close();
		}
	}

	/*private static void streamContent() {
		try (InputStream in = Files.newInputStream(targetFile.toPath());
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException x) {
			System.err.println(x);
		}
	}*/

	private static File lastFileModified() {
		File[] files = workDir.listFiles();
		long lastMod = Long.MIN_VALUE;
		File choice = null;
		for (File file : files) {
			if (file.lastModified() > lastMod) {
				choice = file;
				lastMod = file.lastModified();
			}
		}
		System.out.println("Last modified file in " + workDir.getPath() + ":");
		return choice;
	}

	private static void parseArgs(String[] args) {
		File arg = new File(String.join(" ",args).replace("\\", "\\\\"));
		//System.out.println(args[0]);
		if (arg.isFile()) {
			targetFile = arg;
		} else {
			workDir = arg;
			targetFile = lastFileModified();
		}
		
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			parseArgs(args);
		} else {
			targetFile = lastFileModified();
		}

		System.out.println(targetFile.getName());

		readContent();
		clearSpaces();
		//System.out.println(content.substring(0, 1200));
		readMetadata();
		in = new Scanner(System.in);
		//CLInput = System.console().reader();
		addSignature();
		writeFile();
		System.out.println(content);
	}

}