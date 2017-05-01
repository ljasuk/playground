import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

public class OutputTester {
	private static String content;
	private static File target;

	private static String targetPath;
	//private static long fileSize = 0;
	
	private static void writeFile(){
		long lengthBefore = targetFile.length();
		try (BufferedWriter writer = Files.newBufferedWriter(target.toPath())) {
		    writer.write(content);
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		System.out.println("\nSize difference: " + (targetFile.length() - lengthBefore));
		System.out.println("\nDONE");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String scanFile(){
		File target = new File(targetPath);
		//fileSize = target.length();
		String text = "";
		System.out.println(target.exists());
		Scanner fileIn = null;
		try {
			fileIn = new Scanner(target);
			//System.out.println(in.useDelimiter("\\Z").hasNext());

			text = fileIn.useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		} finally {
			fileIn.close();
		}
		return text;
	}
	
	private static String byteRead(){
		String text = null;
		try {
			FileInputStream fis = new FileInputStream(targetPath);
			//fileSize = target.length();
			byte[] data = new byte[(int) target.length()];
			fis.read(data);
			fis.close();

			text = new String(data, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
	
	private static String buffRead(){
		Charset charset = Charset.forName("UTF-16");
		String text = "";
		//File target = new File(targetPath);
		Path tp = Paths.get(targetPath);
		System.err.println(tp.toFile().exists());
		try (BufferedReader reader = Files.newBufferedReader(tp, charset)) {
		    /*String line = null;
		    while ((line = reader.readLine()) != null) {
		        System.out.println(line);
		    }*/
		    //text = reader.;
		    text =  reader.lines().collect(Collectors.joining());
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		
		return text;
	}
	
	public static void main(String[] args) {
		File folder = "/media/koger/New Volume/sheet/";
		// read the folder and filter for .adoc extension
		File[] listOfFiles = folder.listFiles();
		
		// sending all the files to the replacer
		for (File file : listOfFiles) {
			target =  file;
			targetPath = file.getPath();
			content = byteRead();
			writeFile();
		}
		
		//targetPath = "/media/koger/New Volume/Downloads/13938237_494210704109654_1447229537017601039_o.jpg";
		//target = new File(targetPath);
		//System.out.println(byteRead());
		//content = byteRead();
		//System.out.println(buffRead());
		
		
		//System.out.println(content);
		
		//writeFile();
		//System.out.println(target.getName()+"\nDifference: " + (target.length() - fileSize));
		
		
	}
	
	
	
}
