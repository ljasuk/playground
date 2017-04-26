import java.io.File;
import java.io.FilenameFilter;
import java.util.Scanner;

/**
 * Scans a directory for .adoc files, and runs the {@code Replacer} on them,
 * also providing what to replace.
 * @author Gergely Kovacs
 *
 */
public class scanAdoc {
	private static File folder = new File("E:\\xkovger\\ecas\\doc\\");
	//private static File FOLDER = new File("C:\\eclipse\\adoc\\");

	public static void main(String[] args) {
		
		// look for working folder
		if (args.length>0) {
			folder = new File(String.join(" ",args).replace("\\", "\\\\"));
		} else
		if (!folder.exists()) {
			folder = new File("C:\\eclipse\\adoc\\");
		}
		
		// ask for user input if folder does not exist
		Scanner in = new Scanner(System.in);
		while (!folder.exists()) {
			System.out.println(folder+" does not exist. Provide different folder," +
					" or press Ctrl-X to exit:");
			
			folder = new File(in.next());
			//in.next();
			
		}
		in.close();
		
		// read the folder and filter for .adoc extension
		File[] listOfFiles = folder.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".adoc");
		    }
		});
		
		// define strings to be replaced, and replacements
		String[][] toReplace = new String[][]{
				{	//Strings to be replaced
					"NDP 3.3",
					"3.3"
				},
				{	//Replacement strings
					"NDP {sys: cat ../NEXT_RELEASE}",
					"{sys: cat ../NEXT_RELEASE}"
				}
		};
		
		// sending all the files to the replacer
		for (File file : listOfFiles) {
			Replacer.newInstance(file, toReplace);
		}

	}

}
