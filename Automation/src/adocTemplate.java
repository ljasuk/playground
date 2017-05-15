import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class adocTemplate {
    
    private final File targetFile;
    
    private static String findLongLines(String text) {
        String[] lines = text.split(System.getProperty("line.separator"));
        for (String tmpLine : lines) {
            if (tmpLine.length() > 72) {
                text = text.replace(tmpLine, breakLine(tmpLine));
                // System.out.println(breakLine(tmpLine));
            }
        }
        return text;
    }

    private static String breakLine(String line) {
        if (line.length() > 72) {
            int index = line.substring(0, 72).lastIndexOf(" ");
            return (line.substring(0, index) + "\n" + breakLine(line.substring(index + 1)));
        }
        return line;
    }
    
    private String fillString(String str, String toReplace) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < toReplace.length(); i++) {
            sb.append(str);
        }
        sb.append("\n\n<TBF>\n\r\n");
        return sb.toString();
    }
    
    private String length(String str){
        return "" + str.length();
    }
    
    private void writeFile(String text) {
        long lengthBefore = targetFile.length();
        try (BufferedWriter writer = Files.newBufferedWriter(targetFile.toPath())) {
            writer.write(text);
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        System.out.println("Size difference:    " + (targetFile.length() - lengthBefore) + "  ("
                + String.format("%.2f", (((double) Math.abs(targetFile.length() - lengthBefore) * 100) / (double) lengthBefore)) + "%)");
        // check for wrong characters
        if (text.contains("�")) {
            System.out.println("ENCODING ERROR");
        }
        
        System.out.println("\nDONE");
    }
    
    private String mapToAdoc(String text){
        System.out.println(text);
        String[][] fr = new String[][]{
            {"\n\\d (.+?)\r", "-"},
            {"\n\\d\\.\\d (.+?)\r", "~"},
            {"\n\\d\\.\\d\\.\\d (.+?)\r", "^"}
        };
        for (String[] f : fr) {
            Matcher matcher = Pattern.compile(f[0]).matcher(text);
            while(matcher.find()) {
                text = text.replace(matcher.group(), ("\r\n" + matcher.group(1) + "\r\n" + fillString(f[1],matcher.group(1))));
            }
        }
//        Matcher matcher = Pattern.compile("\n\\d (.+?)\r\n").matcher(text);
//        while(matcher.find()) {
//            text = text.replace(matcher.group(), ("\n" + matcher.group(1) + "\n" + fillString("-",matcher.group(1))));
//        }
//        text = text.replaceAll("\n\\d (.+?)\r\n","asdf");
//        text = text.replaceAll("\n\\d (.+?)\r\n", "\n$1\n"+"$1".length() + "\n\n<TBF>\n\n" + length("$1"));
//        text = text.replaceAll("\n\\d\\.\\d (.+?)\r\n", "\n$1\n"+"$1".replaceAll(".", "~")) + "\n\n<TBF>\n\n";
//        text = text.replaceAll("\n\\d\\.\\d\\.\\d (.+?)\r\n", "\n$1\n"+"$1".replaceAll(".", "^")) + "\n\n<TBF>\n\n";
        return text;
    }
    
    private String byteRead() {
        String text = null;
        try {
            text = new String(Files.readAllBytes(targetFile.toPath()), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
    

    public adocTemplate(File targetFile) {
        this.targetFile = targetFile;
    }

    public static void main(String[] args) {
        adocTemplate at = new adocTemplate(new File("C:\\eclipse\\test.txt"));
        String text = at.byteRead();
        text = at.mapToAdoc(text);
        at.writeFile(text);
    }

}
