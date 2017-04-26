import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class scrtest {
	private static String source = "C:\\eclipse\\test.txt";
	private static String target = "C:\\eclipse\\gyujto.txt";
	private static String regEx = "<.*?>";

	public static void main(String args[]) {
		if(args.length>0) setArguments(args);
		
		String content = null, title = null;
		Scanner fileInput = null;
		try {
			fileInput = new Scanner(new File(source));
			
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException" + e);
			e.printStackTrace();
		}
		content = fileInput.useDelimiter("\\Z").next();
		fileInput.close();
		
		title = findTitle(content);
		System.out.println("cim: " + title);

		ArrayList<String> talalat = regexChecker(regEx, content);
		for (String a : talalat)
			System.out.println("var: " + a);
		PrintWriter fOut = createFile(target);
		writeSA(talalat, fOut, title);

		fOut.close();

	}

	private static ArrayList<String> regexChecker(String theRegex, String text) {
		ArrayList<String> found = new ArrayList<String>();

		Pattern checkRegex = Pattern.compile(theRegex);

		Matcher regexMatcher = checkRegex.matcher(text);

		while (regexMatcher.find()) {
			String a = regexMatcher.group().trim();
			if (regexMatcher.group().length() != 0 && !found.contains(a)) {
				if (a.charAt(1) == '/')
					found.remove("<" + a.substring(2));
				else
					found.add(regexMatcher.group().trim());
			}
		}

		return found;
	}

	private static PrintWriter createFile(String filename) {
		try {
			File celFile = new File(filename);

			PrintWriter infoToWrite = new PrintWriter(new BufferedWriter(new FileWriter(celFile, true)));
			return infoToWrite;
		}

		catch (IOException e) {
			System.out.println("IO Error occurred!");
			System.exit(0);
		}
		return null;
	}

	private static void writeSA(ArrayList<String> a, PrintWriter cout, String cim) {
		cout.println(cim);
		for (String b : a) {
			cout.println(b);
		}
		cout.println();
	}

	private static String findTitle(String text) {
		int i = 0, start=0, end = 1, l=0;
		while(l<6){if(text.charAt(i++)=='\n')l++;}
		while (!(text.charAt(i++) == '\n' && Character.isAlphabetic(text.charAt(i)))) {
			start = i;
		}
		while (text.charAt(i++) != '\r') {
			end = i;
		}
		return text.substring(start, end);
	}
	
	private static void setArguments(String[] arg){
		CharSequence ctarget="\\", replacement="\\\\";
		System.out.println(Arrays.toString(arg));
		source=arg[0].replace(ctarget, replacement);
		if (arg.length>1) target=arg[1].replace(ctarget, replacement);
		if (arg.length>2) regEx=arg[2].replace(ctarget, replacement);
		System.err.println(target);
	}
	
}