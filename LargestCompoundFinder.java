
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;

public class LargestCompoundFinder {

	ArrayList<String> prefixes = new ArrayList<String>();

	public LargestCompoundFinder() {
	}

	public static void main(String[] args) {
		// fourfivetwo is the longest compound (11 symbols)
		String[] argsWords = { "five", "fivetwo", "fourfive", "fourfivetwo",
				"one", "onefiveone", "two", "twofivefourone" };

		// ratcatdogcat is the longest compound (12 symbols)
		String[] argWords1 = { "cat", "cats", "catsdogcats", "catxdogcatsrat",
				"dog", "dogcatsdog", "hippopotamuses", "rat", "ratcat",
				"ratcatdog", "ratcatdogcat" };

		LargestCompoundFinder finder = new LargestCompoundFinder();
		finder.findLongestCompound(argsWords);
		System.out.println();
		finder.findLongestCompound(argWords1);
	}

	/**
	 * Searches for the longest compound word in given string array
	 * 
	 * @param words
	 *            - array of strings sorted in alphabetical order
	 */
	public void findLongestCompound(String[] words) {
		System.out.println(Arrays.toString(words));
		TreeMap<String, String> map = new TreeMap<String, String>();

		for (String current : words) {
			String suffix = "";
			Entry<String, String> lastEntry = map.lastEntry();

			if (lastEntry != null && current.contains(lastEntry.getKey())) {
				// Previous entry key is a current prefix
				suffix = current.substring(lastEntry.getKey().length());
			}

			map.put(current, suffix);

			if (suffix.isEmpty()) {
				// It's not a compound but a common prefix
				prefixes.add(current);
			}
		}

		String longest = "";
		while (!map.isEmpty()) {
			Entry<String, String> entry = map.pollFirstEntry();

			String suffix = entry.getValue();
			if (suffix.isEmpty()) { // It's not a compound
				continue;
			}

			String current = entry.getKey();
			if (isSuffixCompound(suffix)) {
				longest = (current.length() > longest.length()) ? current
						: longest;
			}
		}

		System.out.println("The longest compound word is: " + longest + " ("
				+ longest.length() + " symbols)");
	}

	/**
	 * Trying to replace whole suffix with prefixes
	 * 
	 * @param suffix
	 * @return true - if suffix is empty
	 */
	private boolean isSuffixCompound(String suffix) {
		String temp = new String(suffix);

		for (int i = 0; i < prefixes.size() && !temp.isEmpty(); i++) {
			temp = temp.replace(prefixes.get(i), "");
		}

		return temp.isEmpty();
	}
}
