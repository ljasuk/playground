import java.io.File;
import java.io.FilenameFilter;

public class scanAdoc {
	private static File FOLDER = new File("E:\\xkovger\\ecas\\doc\\");
	//private static File FOLDER = new File("C:\\eclipse\\adoc\\");

	public static void main(String[] args) {
		File[] listOfFiles = FOLDER.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".adoc");
		    }
		});
		
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
		
		for (File file : listOfFiles) {
			Replacer.newInstance(file, toReplace);
		}

	}

}
