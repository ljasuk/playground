import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Replacer {
	private final File targetFile;
	private final String content;
	private final String[][] toReplace;
	private final boolean modified;
	private final String newContent;
	
	/**
	 * writes the replaced content to the file
	 */
	private void writeFile(){
		try (BufferedWriter writer = Files.newBufferedWriter(targetFile.toPath())) {
		    writer.write(newContent);
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	/**
	 * replaces all the pairs of strings in the content of the file
	 * @return the updated content
	 */
	private String replace(){
		String modified = content;
		for (String[] pair : toReplace) {
			modified = modified.replace(pair[0], pair[1]);
		}
		return modified;
	}
	
	/**
	 * checks whether the file contains anything that has to be changed
	 * @return boolean marking whether change is needed or not
	 */
	private boolean check(){
		boolean contains = false;
		for (String[] pair : toReplace) {
			if (content.contains(pair[0])) contains = true;
		}
		return contains;
	}

	/**
	 * reads the whole content of a file
	 * @return the contents of the file as a single string
	 */
	private String readContent() {
		Scanner fileIn = null;
		String content = null;
		try {
			fileIn = new Scanner(targetFile);
			//System.out.println(in.useDelimiter("\\Z").hasNext());

			content = fileIn.useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			// System.out.println(e.getMessage());
		} finally {
			fileIn.close();
		}
		return content;
	}
	
	private Replacer(File file, String[][] toReplace){
		targetFile = file;
		this.toReplace = toReplace;
		content = readContent();
		
		if (check()){
			replace();
			System.out.println(targetFile.getName());
			modified = true;
			newContent = replace();
			writeFile();
		} else {
			modified = false;
			newContent = null;
		}	
	}
	
	/**
	 * static builder method
	 * @param file - the file to check
	 * @param toReplace - a multidimensional array of strings: pairs of strings
	 * that have to be changed and the value to change to
	 * @return an instance of Replacer
	 */
	public static Replacer newInstance(File file, String[][] toReplace) {
		return new Replacer(File file, String[][] toReplace);
	}

	public boolean isModified() {
		return modified;
	}
	
	
}
