import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.*;

class OldRepairXML {

	private static File targetFile;
	private static File workDir = new File("C:\\Users\\s24267\\Downloads");
	private static String content;
	private static String rev;
	private static String[] sig = new String[2];
	private static Scanner in;
	
	private static void writeFile(){
		try (BufferedWriter writer = Files.newBufferedWriter(targetFile.toPath())) {
		    writer.write(content);
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	private static void tableToGrid(){
		Matcher tableMatcher = Pattern.compile("(?s)<list(.+?)</list>").matcher(content);
		while(tableMatcher.find()){
			String list = tableMatcher.group(1);
			if (list.contains("<table")) {
				String grid = list.replace("<caption></caption>", "");
				grid = grid.replace("<caption/>", "");
				grid = grid.replaceAll("<table.*?>", "<grid frame =\"all\">");
				grid = grid.replace("</table>", "</grid>");
				content = content.replace(list, grid);
			}
		}
	}
	
	private static void emptyReference(){
		content = content.replaceFirst("(<reference xml:id=\"references\">)\n(</reference>)",
				"$1<reference-list><rf-subsection></rf-subsection></reference-list>$2");
	}
	
	private static void figInTp(){
		content = content.replaceAll("(?s)<tp><figure.+?(<graphics.+?/>).*?</figure>",
				"<tp>$1");
	}
	
	private static void figInParagraph(){
		content = content.replaceAll("(?s)<p>Figure \\d\\.\\s(.*?)(<figure.+?/>).+?</p>",
				"$2"+"\n<caption>"+"$1"+"</caption></figure>");
	}
	
	private static void emphResourceID(){
		content = content.replaceAll("<emph.*?>(<resource-id.+?/resource-id>)</emph>", "$1");
	}
	
	private static void repairTM(){
		content = content.replaceFirst("p>.+?Ericsson AB", "p>&copy; Ericsson AB");
		if (content.contains("�")){
			System.out.println("Repairing (�)");
			content = content.replace("’", "&rsquo;");	// apostrophe
			content = content.replace("→", "&rarr;");		// right arrow
			content = content.replace("™", "&trade;");	// trademark mark
			content = content.replace("®", "&reg;");		// R in circle
			content = content.replace("├", "&boxvr;");	// tree line T
			content = content.replace("─", "&boxh;");		// tree line horizontal
			content = content.replace("│", "&boxv;");		// tree line vertical
			content = content.replace("└", "&boxur;");	// tree corner UP-RIGHT
			 
			content = content.replace("…", "&mldr;");		// three dots
			content = content.replace("—", "&mdash;");	// em dash
			content = content.replace(" ", " ");			// spaces next to em dash
			System.out.println((content.contains("�")) ? "Could not repair \"�\"" : "Removed all \"�\"");
		}		
	}
	
	private static String exampleToP(String list){
		list = list.replaceAll("<example.*?>", "<p>");
		list = list.replace("</example>", "</p>");
		System.out.println(list);
		return list;
	}
	
	private static String turnToSL(String list){
		list = list.replaceAll("<list-item.*?>", "<sl-item>");
		list = list.replaceAll("</list-item>", "</sl-item>");
		list = list.replaceAll("<list.*?>", "<step-list>");
		list = list.replace("</list>", "</step-list>");
		list = list.replaceAll("<example.*?>", "<stepxmp>");
		list = list.replace("</example>", "</stepxmp>");
		return list;
	}
	
	private static String turnToSubstep(String list){
		list = list.replaceAll("<list-item.*?>", "<substep>");
		list = list.replaceAll("</list-item>", "</substep>");
		list = list.replaceAll("<list.*?>", "<substeps>");
		list = list.replace("</list>", "</substeps>");
		return list;
	}
	
	private static void substeps() throws StringIndexOutOfBoundsException{
		int cursor = 0;
		String oTag="<list type=\"numeric";
		String cTag = "</list>";
		
		while (content.substring(cursor).contains(oTag)){
			int listBegin = content.indexOf(oTag, cursor);
			int listClosing = content.indexOf(cTag, listBegin);
			String list = content.substring(listBegin, listClosing+7);
			
			if (count(oTag, list)==count(cTag, list)){
				//System.out.println("Normal list");
				//System.out.println(list+"\n");
				//System.out.println(list.contains("<example")+"\n");
				
				if (list.contains("<example")){
					int newSize = exampleToP(content.substring(listBegin, listClosing)).length();
					content = content.substring(0, listBegin) +
							exampleToP(content.substring(listBegin, listClosing)) +
							content.substring(listClosing);
					cursor = listBegin + newSize + 7;
				} else cursor = listClosing + 7;
				continue;
			}
			
			while (count(oTag, list)!=count(cTag, list)){
				System.out.println("Substeplist!");
				listClosing = content.indexOf(cTag, listClosing+7);
				//System.err.println("listEnd: "+list.substring(list.length()-7));
				list = content.substring(listBegin, listClosing+7);	
				//System.err.println("listEnd: "+list.substring(list.length()-7));
			}
			
			while (count(oTag, list)>1) {
				int beginIndex = content.indexOf(oTag, listBegin+10);
				int endIndex = content.indexOf(cTag, listBegin+10);
				//System.err.println("beginIndex: "+beginIndex+" endIndex: "+endIndex);
				//System.out.println(turnToSubstep(content.substring(beginIndex, endIndex)));
				content = content.substring(0, beginIndex) +
						turnToSubstep(content.substring(beginIndex, endIndex+7)) +
						content.substring(endIndex+7);
				list = content.substring(listBegin, listClosing+7);
				//System.err.println("listEnd: "+list.substring(list.length()-7));
				
			}
			
			//System.err.println(content.substring(listBegin, listClosing));
			
			content = content.substring(0, listBegin) +
					turnToSL(content.substring(listBegin, listClosing)) +
					content.substring(listClosing);
			
			cursor = content.substring(listBegin, listClosing).lastIndexOf(cTag)+7;
			//System.out.println(turnToSL(content.substring(listBegin, listClosing)));
		}
	}
	
	private static void figInP(){
		int openingTag = count("<p><figure", content);
		if (openingTag>0 && openingTag==count("</figure></p>", content)) {
			System.out.println(openingTag + " figures in paragraphs. Take "
					+ "figures out of paragraphs?");
			if (in.next().toLowerCase().equals("y")) {
				content = content.replace("<p><figure", "<figure");
				content = content.replace("</figure></p>", "</figure>");
			}
		}
		
	}
	
	private static int count(String regex, String target){
		Matcher matcher = Pattern.compile(regex).matcher(target);
		int count = 0;
		while (matcher.find()){
		    count +=1;
		}
		//System.out.println(regex + ": "+count); 
		return count;
	}
	
	private static void removeColwidth(){
		content = content.replaceAll("\\scolwidth=\"\\d+\"", "");
	}
	
	private static void addSignature(){
		final String[][] SIGNATURES = new String[][]{
			{"Gergely Kovacs","XKOVGER"},
			{"Izsak Soos","XIZSSOO"},
			{"Ferenc Nagy","ENAGFER"},
			{"Ismo Paukamainen","LMFISP"},
			{"Antti Tolonen","EANTTTO"},
			{"Juha Ritvanen","LMFJURI"},
			{"Juha S&auml;&auml;skilahti","LMFJSAA"},
			{"Syed Safi Ali Shah", "ESYISHH"}
		};
		
		System.out.println(sig[1]);
		
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
		beginIndex = content.indexOf("<person>",beginIndex)+8;
		endIndex = content.indexOf("<location",beginIndex);
		//System.out.println(content.substring(beginIndex, endIndex));
		content = content.substring(0,beginIndex) + 
				"<name>"+
				SIGNATURES[choice][0] + "</name><signature>" + 
				SIGNATURES[choice][1] + "</signature>" +
				content.substring(endIndex);
		//System.out.println(content.substring(0,1300));
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
		content = content.replaceAll("<term>\\s+", "<term>");
		content = content.replaceAll("\\s+</term", "</term");
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

	private static void streamContent() {
		try (//InputStream in = Files.newInputStream(targetFile.toPath());
				BufferedReader reader = new BufferedReader(
						new FileReader(targetFile))) {
			char[] cbuf = {};
                        while (reader.ready()){
                        reader.read(cbuf);}
                        content = new String(cbuf);
                        System.out.println(content);
			/*while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}*/
		} catch (IOException x) {
			System.err.println(x);
		}
	}

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
			if (!workDir.exists()) workDir = new File("\\\\vhub.rnd.ki.sw.ericsson.se\\home\\xkovger\\Downloads");
			targetFile = lastFileModified();
		}
		

		System.out.println(targetFile.getName());
                
    
                streamContent();
                System.out.println(content);
		clearSpaces();
		//System.out.println(content.substring(0, 1200));
		readMetadata();
		in = new Scanner(System.in);
		//CLInput = System.console().reader();
		addSignature();
		removeColwidth();
		figInP();
		try {
			substeps();
		} catch (StringIndexOutOfBoundsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		repairTM();
		emphResourceID();
		figInParagraph();
		figInTp();
		emptyReference();
		tableToGrid();
		//writeFile();
		
		
		System.out.println("\nDONE");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(content);
	}

}