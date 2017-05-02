import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.*;

class RepairXml {

	private final File targetFile;
	private final String content;
	private final String newContent;
	private final String[] sig;
	private final Scanner in;

	private void writeFile() {
		long lengthBefore = targetFile.length();
		try (BufferedWriter writer = Files.newBufferedWriter(targetFile.toPath())) {
			writer.write(newContent);
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
		System.out.println("\nContent difference: " + (newContent.length() - content.length()));
		System.out.println("Size difference:    " + (targetFile.length() - lengthBefore) 
				+ "  (" + String.format("%.2f", 
						(((double) Math.abs(targetFile.length() - lengthBefore)*100) 
								/ (double)lengthBefore))+ "%)");
		System.out.println("\nDONE");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private String tableToGrid(String text) {
		Matcher tableMatcher = Pattern.compile("(?s)<list(.+?)</list>").matcher(text);
		while (tableMatcher.find()) {
			String list = tableMatcher.group(1);
			if (list.contains("<table")) {
				System.out.println("Replacing tables in lists to grids...");
				String grid = list.replace("<caption></caption>", "");
				grid = grid.replace("<caption/>", "");
				grid = grid.replaceAll("<table.*?>", "<grid frame =\"all\">");
				grid = grid.replace("</table>", "</grid>");
				text = text.replace(list, grid);
			}
		}
		return text;
	}

	private String turnToSL(String text) {
		int cursor = 0;
		text = text.replaceFirst("<list type=\"numeric\".*?>", "<step-list>");
		while (text.substring(cursor).contains("<list type=\"unordered\" compact=\"no\">")) {
			int index = cursor + (text.substring(cursor).indexOf("<list type=\"unordered\" compact=\"no\">"));
			text = text.substring(0, cursor) 
					+ text.substring(cursor, index).replaceAll("<list-item.*?>", "<sl-item>")
					.replace("</list-item>", "</sl-item>")
					.replaceAll("<example.*?>", "<stepxmp>")
					.replace("</example>", "</stepxmp>")
					.replace("<p><preform>", "<stpexmp><preform>")
					.replace("</preform></p>", "</preform></stpexm>")
					+ text.substring(index);
			cursor = index + text.substring(index).indexOf("</list>")+7;
		}
		
		text = text.substring(0, cursor) 
				+ text.substring(cursor)
				.replaceAll("<list-item.*?>", "<sl-item>")
				.replace("</list-item>", "</sl-item>")
				.replaceAll("<example.*?>", "<stepxmp>")
				.replace("</example>", "</stepxmp>")
				.replace("</list>", "</step-list>")
				.replace("<p><preform>", "<stpexp><preform>")
				.replace("</preform></p>", "</preform></stpexp>");
		
		return text;
	}

	private String turnToSubstep(String list) {
		list = list.replaceAll("<list-item.*?>", "<substep>");
		list = list.replaceAll("</list-item>", "</substep>");
		list = list.replaceAll("<list.*?>", "<substeps>");
		list = list.replace("</list>", "</substeps>");
		return list;
	}

	private String substeps(String text) throws StringIndexOutOfBoundsException {
		int cursor = 0;
		String oTag = "<list type=\"numeric";
		String cTag = "</list>";
		
		// while there are numeric lists after the cursor
		while (text.substring(cursor).contains(oTag)) {
			int listBegin = text.indexOf(oTag, cursor);
			int listClosing = text.indexOf(cTag, listBegin) + 7;
			String list = text.substring(listBegin, listClosing);
			
			// while the list is not complete, move listClosing to next closing tag
			while (count("<list type", list) != count(cTag, list)) {
				System.out.println("Nested list!");
				listClosing = text.indexOf(cTag, listClosing) + 7;
				list = text.substring(listBegin, listClosing);
			}
			
			// turn nested numeric lists into substeplists
			while (count(oTag, list) > 1) {
				int beginIndex = text.indexOf(oTag, listBegin + 10);
				int endIndex = text.indexOf(cTag, beginIndex) + 7;
				int kul = turnToSubstep(text.substring(beginIndex, endIndex)).length() 
						- text.substring(beginIndex, endIndex).length(); 
				text = text.substring(0, beginIndex) 
						+ turnToSubstep(text.substring(beginIndex, endIndex))
						+ text.substring(endIndex);
				listClosing += kul;
				list = text.substring(listBegin, listClosing);

			}
			
			int newSize = turnToSL(text.substring(listBegin, listClosing)).length();
			text = text.substring(0, listBegin) 
					+ turnToSL(text.substring(listBegin, listClosing))
					+ text.substring(listClosing);

			cursor = listBegin + newSize;
		}
		return text;
	}

	private int count(String regex, String target) {
		Matcher matcher = Pattern.compile(regex).matcher(target);
		int count = 0;
		while (matcher.find()) {
			count += 1;
		}

		return count;
	}
	
	// might be taken out
	private String figInP(String text) {
		int openingTag = count("<p><figure", text);
		if (openingTag > 0 && openingTag == count("</figure></p>", text)) {
			System.out.println(openingTag + " figures in paragraphs. Take " + 
			"figures out of paragraphs?");
			if (in.nextLine().toLowerCase().equals("y")) {
				text = text.replace("<p><figure", "<figure");
				text = text.replace("</figure></p>", "</figure>");
			}
		}
		return text;
	}

	private String betweenTags(String tag, String where) {
		String regex = "<" + tag + ".*?>.*?</" + tag + ">";
		Matcher matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(where);
		String raw;
		if (matcher.find())
			raw = matcher.group();
		else
			return "";
		int beginIndex = raw.indexOf('>') + 1;
		int endIndex = raw.lastIndexOf('<');
		return raw.substring(beginIndex, endIndex);
	}
	
	private String colwidth(String text) {
		if (text.contains("colwidth")){
			System.out.println("Remove colwidht?(y/*) ");
			if (in.nextLine().trim().toLowerCase().equals("y")) {
				text = text.replaceAll("\\scolwidth=\".+?\"", "");
			}
			
		}
		return text;
	}

	private String addSignature(String text) {
		final String[][] SIGNATURES = new String[][] { 
			{ "Gergely Kovacs", "XKOVGER" }, 
			{ "Izsak Soos", "XIZSSOO" },
			{ "Ferenc Nagy", "ENAGFER" }, 
			{ "Ismo Paukamainen", "LMFISP" }, 
			{ "Antti Tolonen", "EANTTTO" },
			{ "Juha Ritvanen", "LMFJURI" }, 
			{ "Juha S&auml;&auml;skilahti", "LMFJSAA" },
			{ "Syed Safi Ali Shah", "ESYISHH" }};

		if (sig[1].length() < 3) {
			System.out.println("\nNo signature!\nAdd signature?");
		} else {
			System.out.println("\nSignature: " + Arrays.toString(sig));
			System.out.println("Change signature?");
		}

		System.out.println("0 NO");
		for (int i = 0; i < SIGNATURES.length; i++) {
			System.out.println((i + 1) + " " + Arrays.toString(SIGNATURES[i]));
		}

		int choice = 0;
		choice = in.nextInt() - 1;
		in.nextLine();
		
		if (choice < 0) {
			return text;
		}

		int beginIndex = text.indexOf("<drafted-by>") + 12;
		int endIndex = text.indexOf("</drafted-by>");
		beginIndex = text.indexOf("<person>", beginIndex) + 8;
		endIndex = text.indexOf("<location", beginIndex);

		text = text.substring(0, beginIndex) + "<name>" 
				+ SIGNATURES[choice][0] + "</name><signature>"
				+ SIGNATURES[choice][1] + "</signature>\n" + text.substring(endIndex);
		return text;
	}

	private String clearSpaces(String text) {
		text = text.replaceAll("<p>\\s+", "<p>");
		text = text.replaceAll("\\s+</p>", "</p>");
		text = text.replaceAll("<tp>\\s+", "<tp>");
		text = text.replaceAll("\\s+</tp>", "</tp>");
		text = text.replaceAll("<rf-title>\\s+", "<rf-title>");
		text = text.replaceAll("\\s+</rf-title", "</rf-title");
		text = text.replaceAll("<term>\\s+", "<term>");
		text = text.replaceAll("\\s+</term", "</term");
		text = text.replace("xml:id=\"_", "xml:id=\"");
		return text;
	}

	private String repair(String text) {
		text = clearSpaces(text);
		text = addSignature(text);
		text = colwidth(text);
		text = figInP(text);
		try {
			text = substeps(text);
		} catch (StringIndexOutOfBoundsException e1) {
			e1.printStackTrace();
		}

		// resource-id inside emph
		text = text.replaceAll("<emph.*?>(<resource-id.+?/resource-id>)</emph>", "$1");
		// figures in paragraphs
		text = text.replaceAll("(?s)<p>Figure \\d\\.\\s(.*?)(<figure.+?/>).+?</p>",
				"$2" + "\n<caption>" + "$1" + "</caption></figure>");
		// figures in table paragraphs
		text = text.replaceAll("(?s)<tp><figure.+?(<graphics.+?/>).*?</figure>", "<tp>$1");
		// empty reference list
		text = text.replaceFirst("(<reference xml:id=\"references\">)\n(</reference>)",
				"$1<reference-list><rf-subsection></rf-subsection></reference-list>$2");
		
		text = tableToGrid(text);
		// scalefit of graphics set to 1
		text = text.replace("scalefit=\"0\"", "scalefit=\"1\"");
		// check for wrong characters
		if (text.contains("�")) System.out.println("ENCODING ERROR");		
		return text;
	}

	private String[] readMetadata() {
		int beginIndex = content.indexOf("<meta-data>");
		int endIndex = content.indexOf("</meta-data>");
		String metadata = content.substring(beginIndex, endIndex);
		String docNo = betweenTags("doc-no", metadata);
		String title = betweenTags("title", metadata);
		System.out.println("Title: " + title);
		System.out.println("Document Number: " + docNo);
		String rev = betweenTags("rev", metadata);
		System.out.println("Revision: " + rev);
		String drafted = betweenTags("drafted-by", metadata);
		String signum[] = new String[2];
		signum[0] = betweenTags("name", drafted);
		signum[1] = betweenTags("signature", drafted);

		return signum;
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

	public RepairXml(File targetFile) {
		this.targetFile = targetFile;
		System.out.println(targetFile.getName());
		content = byteRead();
		sig = readMetadata();
		in = new Scanner(System.in);
		newContent = repair(content);
		writeFile();
	}
}
