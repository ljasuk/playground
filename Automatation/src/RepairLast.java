import java.io.File;

public class RepairLast {
	private final File targetFile;
	
	private File lastFileModified(File workDir) {
		File[] files = workDir.listFiles();
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

	private File parseArgs(String[] args) {
		File arg = new File(String.join(" ",args).replace("\\", "\\\\"));
		//System.out.println(args[0]);
		if (arg.isFile()) {
			return arg;
		} else if (arg.isDirectory()){
			return lastFileModified(arg);
		}
		return null;
	}
	
	private File setTarget(String[] args){
		File target = null;
		
		if (args.length > 0 && parseArgs(args) != null) {
			target = parseArgs(args);
		} else {
			target = lastFileModified(new File("\\\\vhub.rnd.ki.sw.ericsson.se\\home\\xkovger\\Downloads"));
		}
		return target;
	}
	
	private RepairLast(String[] args) {
		targetFile = setTarget(args);
	}

	public static void main(String[] args) {
		new RepairClass(new RepairLast(args).targetFile);
	}
}
