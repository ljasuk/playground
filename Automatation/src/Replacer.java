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
	
	private void writeFile(String modified){
		try (BufferedWriter writer = Files.newBufferedWriter(targetFile.toPath())) {
		    writer.write(modified);
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	private void replace(){
		String modified = content;
		for (String[] pair : toReplace) {
			modified = modified.replace(pair[0], pair[1]);
		}
		writeFile(modified);
	}
	
	private boolean check(){
		boolean contains = false;
		for (String[] pair : toReplace) {
			if (content.contains(pair[0])) contains = true;
		}
		return contains;
	}

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
	
	public Replacer(File file, String[][] toReplace){
		targetFile = file;
		this.toReplace = toReplace;
		content = readContent();
		
		if (check()){
			replace();
			System.out.println(targetFile.getName());
			modified = true;
		} else modified = false;
		
	}

	public boolean isModified() {
		return modified;
	}
	
	
}
