import java.io.File;
import java.io.FilenameFilter;
import java.util.Scanner;

/**
 * Scans a directory for .xml files, and runs a class on them.
 * @author Gergely Kovacs
 *
 */
public class scanXML {
	private static File folder = new File("\\\\vhub.rnd.ki.sw.ericsson.se\\home\\xkovger\\Downloads\\xml");
	private static File folder2 = new File("C:\\eclipse\\xml\\");
	
	private final static FilenameFilter xmlExt = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.toLowerCase().endsWith(".xml");
		}
	};
	
	public static void main(String[] args) {
		
		// look for working folder
		if (args.length>0) {
			folder = new File(String.join(" ",args).replace("\\", "\\\\"));
		} else
		if (!folder.exists()) {
			folder = folder2;
		}
		
		// ask for user input if folder does not exist
		if (!folder.exists()) {
			Scanner in = new Scanner(System.in);
			while (!folder.exists()) {
				System.out.println(
						folder + " does not exist. Provide different folder,"
								+ " or press Ctrl-X to exit:");

				folder = new File(in.nextLine());
				// in.next();

			}
			in.close();
		}
		
		// read the folder and filter for .adoc extension
		File[] listOfFiles = folder.listFiles(xmlExt);
		
		// sending all the files to the replacer
		for (File file : listOfFiles) {
			new RepairXml(file);
		}

	}

}
