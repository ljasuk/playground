import java.util.Arrays;
import java.util.List;

/**
 * Tool to turn one line or an array of lines title capitalized.
 * @author s24267
 *
 */
public class titleCap {
	final private String complete;
	
	private titleCap(String line) {
		String[] szavak = line.split(" ");
		final List<String> notCap = Arrays.asList(new String[] { "a", "an", "the",
				"and", "as", "as if", "as long as", "at", "but", "by", /*"about",*/
				"even if", "for", "from", "if", "if only", "in", "into", "like",
				"near", "now that", "nor", "of", "off", "on", "on top of",
				"once", "onto", "or", "out of", "over", "past", "so", "so that",
				"than", "that", "till", "to", "up", "upon", "with", "when",
				"yet" });
		for (int i = 0; i < szavak.length; i++) {
			if (!notCap.contains(szavak[i]) || i == 0) {
				szavak[i] = szavak[i].substring(0, 1).toUpperCase()
						+ szavak[i].substring(1);
			}
		}
		complete = String.join(" ", szavak);
	}
	
	/**
	 * Factory method for a single line.
	 * @param line - the string that needs to be modified
	 * @return capitalized string
	 */
	public static String single(String line){
		return new String(new titleCap(line).complete);
	}
	
	/**
	 * Factory method for an array of lines.
	 * @param lines - the string array that needs to be modified
	 * @return an array of title capitalized lines
	 */
	public static String[] array(String[] lines){
		final String[] completeStrings = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			completeStrings[i] =  new String(new titleCap(lines[i]).complete);
		}
		return completeStrings;
	}
}
