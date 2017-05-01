import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.*;

class RepairClass {

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
		System.out.println("\nSize difference: " + (targetFile.length() - lengthBefore));
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

	private String repairTM(String text) {
		text = text.replaceFirst("p>.+?Ericsson AB", "p>&copy; Ericsson AB");
		if (text.contains("�")) {
			System.out.println("Repairing (�)");
			text = text.replace("’", "&rsquo;");	// apostrophe
			text = text.replace("→", "&rarr;");		// right arrow
			text = text.replace("™", "&trade;");	// trademark mark
			text = text.replace("®", "&reg;");		// R in circle
			text = text.replace("├", "&boxvr;");	// tree line T
			text = text.replace("─", "&boxh;");		// tree line horizontal
			text = text.replace("│", "&boxv;");		// tree line vertical
			text = text.replace("└", "&boxur;");	// tree corner UP-RIGHT
			text = text.replace("…", "&mldr;");		// three dots
			text = text.replace("—", "&mdash;");	// em dash
			text = text.replace(" ", " ");			// spaces next to em dash
			System.out.println((text.contains("�")) ? "Could not repair \"�\"" : "Removed all \"�\"");
		}
		return text;
	}

	private String exampleToP(String list) {
		list = list.replaceAll("<example.*?>", "<p>");
		list = list.replace("</example>", "</p>");
		System.out.println(list);
		return list;
	}

	private String turnToSL(String list) {
		list = list.replaceAll("<list-item.*?>", "<sl-item>");
		list = list.replaceAll("</list-item>", "</sl-item>");
		list = list.replaceAll("<list.*?>", "<step-list>");
		list = list.replace("</list>", "</step-list>");
		list = list.replaceAll("<example.*?>", "<stepxmp>");
		list = list.replace("</example>", "</stepxmp>");
		return list;
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

		while (text.substring(cursor).contains(oTag)) {
			int listBegin = text.indexOf(oTag, cursor);
			int listClosing = text.indexOf(cTag, listBegin);
			String list = text.substring(listBegin, listClosing + 7);

			if (count(oTag, list) == count(cTag, list)) {

				if (list.contains("<example")) {
					int newSize = exampleToP(text.substring(listBegin, listClosing)).length();
					text = text.substring(0, listBegin) + exampleToP(text.substring(listBegin, listClosing))
							+ text.substring(listClosing);
					cursor = listBegin + newSize + 7;
				} else
					cursor = listClosing + 7;
				continue;
			}

			while (count(oTag, list) != count(cTag, list)) {
				System.out.println("Substeplist!");
				listClosing = text.indexOf(cTag, listClosing + 7);
				list = text.substring(listBegin, listClosing + 7);
			}

			while (count(oTag, list) > 1) {
				int beginIndex = text.indexOf(oTag, listBegin + 10);
				int endIndex = text.indexOf(cTag, listBegin + 10);
				text = text.substring(0, beginIndex) + turnToSubstep(text.substring(beginIndex, endIndex + 7))
						+ text.substring(endIndex + 7);
				list = text.substring(listBegin, listClosing + 7);

			}

			text = text.substring(0, listBegin) 
					+ turnToSL(text.substring(listBegin, listClosing))
					+ text.substring(listClosing);

			cursor = text.substring(listBegin, listClosing).lastIndexOf(cTag) + 7;
		}
		return text;
	}

	private int count(String regex, String target) {
		Matcher matcher = Pattern.compile(regex).matcher(target);
		int count = 0;
		while (matcher.find()) {
			count += 1;
		}
		// System.out.println(regex + ": "+count);
		return count;
	}

	private String figInP(String text) {
		int openingTag = count("<p><figure", text);
		if (openingTag > 0 && openingTag == count("</figure></p>", text)) {
			Scanner in = new Scanner(System.in);
			System.out.println(openingTag + " figures in paragraphs. Take " + "figures out of paragraphs?");
			if (in.next().toLowerCase().equals("y")) {
				text = text.replace("<p><figure", "<figure");
				text = text.replace("</figure></p>", "</figure>");
			}
			in.close();
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
			in.close();
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
		if (choice < 0) {
			return text;
		}

		int beginIndex = text.indexOf("<drafted-by>") + 12;
		int endIndex = text.indexOf("</drafted-by>");
		beginIndex = text.indexOf("<person>", beginIndex) + 8;
		endIndex = text.indexOf("<location", beginIndex);

		text = text.substring(0, beginIndex) + "<name>" + SIGNATURES[choice][0] + "</name><signature>"
				+ SIGNATURES[choice][1] + "</signature>\n" + text.substring(endIndex);
		System.out.println(in.nextLine());
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
		//text = repairTM(text);
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

	/*
	private String readContent() {
		String text = null;
		Scanner fileIn = null;
		try {
			fileIn = new Scanner(targetFile);
			text = fileIn.useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} finally {
			fileIn.close();
		}
		return text;
	}
	*/

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

	public RepairClass(File targetFile) {
		this.targetFile = targetFile;
		System.out.println(targetFile.getName());
		content = byteRead();
		sig = readMetadata();
		in = new Scanner(System.in);
		newContent = repair(content);
		System.out.println("content difference: " + (newContent.length() - content.length()));
		writeFile();
	}
}
