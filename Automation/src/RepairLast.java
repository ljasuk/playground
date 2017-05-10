import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

public class RepairLast {
    private static File targetFile;
    private static File downloadsFolder;
    private static FilenameFilter extensionFilter;

    private static File lastFileModified(File workDir) {
        File[] files = workDir.listFiles(extensionFilter);
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

    private static File parseArgs(String[] args) {
        File arg = new File(String.join(" ", args).replace("\\", "\\\\"));
        // System.out.println(args[0]);
        if (arg.isFile()) {
            return arg;
        } else if (arg.isDirectory()) {
            return lastFileModified(arg);
        }
        return null;
    }

    private static File setTarget(String[] args) {
        File target = null;

        if (args.length > 0 && parseArgs(args) != null) {
            target = parseArgs(args);
        } else {
            target = lastFileModified(downloadsFolder);
        }
        return target;
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(RepairLast.class.getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        downloadsFolder = new File(props.getProperty("downloadsFolder"));
        String extension = props.getProperty("extension");
        extensionFilter = (File dir, String name) -> name.toLowerCase().endsWith("."+extension);
        targetFile = setTarget(args);
        new RepairXml(targetFile);
    }
}
