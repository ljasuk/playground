import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
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
	private static File targetFile;
	private static String targetPath;

	private static void writeFile() {
		long lengthBefore = targetFile.length();
		System.out.println(targetFile.getName());
		System.out.println(targetFile.length());
		try (BufferedWriter writer = Files.newBufferedWriter(targetFile.toPath())) {
			writer.write(content);
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
		System.out.println(targetFile.length());

		System.out.println("\nSize difference: " + (targetFile.length() - lengthBefore));
		System.out.println("\nDONE");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static String scanFile() {
		File target = new File(targetPath);
		// fileSize = target.length();
		String text = "";
		System.out.println(target.exists());
		Scanner fileIn = null;
		try {
			fileIn = new Scanner(target);
			text = fileIn.useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} finally {
			fileIn.close();
		}
		return text;
	}

	private static String byteRead() {
		String text = null;
		try {
			FileInputStream fis = new FileInputStream(targetPath);
			byte[] data = new byte[(int) targetFile.length()];
			fis.read(data);
			fis.close();
			text = new String(data, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	private static String buffRead() {
		Charset charset = Charset.forName("UTF-16");
		String text = "";
		Path tp = Paths.get(targetPath);
		System.err.println(tp.toFile().exists());
		try (BufferedReader reader = Files.newBufferedReader(tp, charset)) {
			text = reader.lines().collect(Collectors.joining());
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}

		return text;
	}

	public static void main(String[] args) {
		File folder = new File("\\\\vhub.rnd.ki.sw.ericsson.se\\home\\xkovger\\Downloads\\testFolder");
		// read the files in the folder
		File[] listOfFiles = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});

		// sending all the files to the read and write
		for (File file : listOfFiles) {
			targetFile = file;
			targetPath = file.getPath();
			content = byteRead();
			// System.out.println(content);
			writeFile();
		}

	}

}
