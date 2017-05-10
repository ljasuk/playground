import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Scans a directory for .xml files, and runs a class on them.
 * @author Gergely Kovacs
 *
 */
public class scanXML {
    private static File folder;
    private static File folder2;
    private static String extension;

    private static FilenameFilter extensionFilter;
    
    private static void execute(File[] ListOfFiles, Consumer<File> consumer) {
        for (File file : ListOfFiles) {
            consumer.accept(file);
        }
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(scanXML.class.getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        folder = new File(props.getProperty("folder"));
        folder2 = new File(props.getProperty("folder2"));
        extension = props.getProperty("extension");
        extensionFilter = (File dir, String name) -> name.toLowerCase().endsWith("."+extension);
        
        // look for working folder
        if (args.length > 0 && !args[0].matches("\\d{1}")) {
            folder = new File(String.join(" ", args).replace("\\", "\\\\"));
        } else if (!folder.exists()) {
            folder = folder2;
        }
        
        // ask for user input if folder does not exist
        if (!folder.exists()) {
            Scanner in = new Scanner(System.in);

            while (!folder.exists()) {
                System.out.println(
                        folder + " does not exist. Provide different folder,"
                                + " or press Ctrl-C to exit:");

                folder = new File(in.nextLine());
                // in.next();

            }
            in.close();
        }
        
        // read the folder and filter for .adoc extension
        File[] listOfFiles = folder.listFiles(extensionFilter);
        
        // sending all the files to the replacer
        if (args.length>0 && args[0].matches("\\d{1}")) {
            Arrays.asList(listOfFiles).stream().forEach(t -> new RepairXml(t, Integer.parseInt(args[0])));
            execute(listOfFiles, t -> new RepairXml(t, Integer.parseInt(args[0])));
        } else {
            execute(listOfFiles, t -> new RepairXml(t));
        }
        

    }
}
