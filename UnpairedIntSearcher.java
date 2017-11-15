import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UnpairedIntSearcher {

	private Map<Key, Object> settings;

	public UnpairedIntSearcher() {
	}

	public static void main(String[] args) {
		// Generate demo, simulating arguments passed from command line
		String[] argsGen = { "--mode", "gen", "--file", "arrayfile.yml",
				"--length", "15", "--range", "50" };

		UnpairedIntSearcher searcher = new UnpairedIntSearcher();
		// parsing incoming program arguments
		searcher.setup(argsGen);
		// perform processing
		searcher.perform();

		// Search demo, simulating arguments passed from command line
		String[] argSearch = { "--mode", "search", "--file", "arrayfile.yml" };
		// parsing incoming program arguments
		searcher.setup(argSearch);
		// perform processing
		searcher.perform();
	}

	/**
	 * prints usage
	 */
	private void printUsage() {
		System.out.println("Usage: SingleNumberSearch [KEYS] [VALUES]");
		System.out.println("Posible keys {values}: ");
		System.out.println("	--mode {gen, search} - a program working mode;");
		System.out
				.println("	--file {filename} - output(generate)/intput(search) filename;");
		System.out
				.println("	--length {int value} - new array odd value legth (optional for generate only);");
		System.out
				.println("	--range {int value} - range of integers to operate, in form of [-value, value), uses 100 if not present.");
	}

	/**
	 * Parses all incoming command line arguments
	 */
	private void parseArgs(String[] args) {
		// New HashMap for storing current settings
		settings = new HashMap<Key, Object>();

		for (int i = 0; i < args.length; i += 2) {
			Key key = null;
			Object value = null;

			try {
				key = Key.parseKey(args[i]);
			} catch (IllegalArgumentException iae) {
				System.out.println("Invalid key passed!");
			}

			value = parseValue(key, args, i);

			if (value == null) {
				return;
			}

			settings.put(key, value);
		}
	}

	/**
	 * Parses incoming value
	 * 
	 * @param key
	 * @param args
	 * @param i
	 * @return
	 */
	private Object parseValue(Key key, String[] args, int i) {
		Object result = null;

		try {
			switch (key) {
			case MODE:
				result = Mode.valueOf(args[i + 1].toUpperCase());
				break;
			case FILE:
				String filename = args[i + 1].toLowerCase();
				// cat .yml extension
				if (filename.lastIndexOf(".") < 0) {
					filename += ".yml";
				}

				File file = new File(filename);
				if (fileProcessed(file)) {
					result = file;
				}
				break;
			case LENGTH:
				int temp = Integer.parseInt(args[i + 1]);

				if (temp % 2 == 0) { // It's even value
					System.out.println(temp + " is even! Select odd value!");
				} else {
					result = temp;
				}

				break;
			case RANGE:
				int range = Integer.parseInt(args[i + 1]);
				result = range;
			default:
				break;
			}

		} catch (ArrayIndexOutOfBoundsException obe) {
			System.out.println("No valid " + key.toString() + " value passed!");
		} catch (IllegalArgumentException iae) {
			System.out.println("Illegal " + key.toString() + " passed: "
					+ args[i]);
		}

		return result;
	}

	/**
	 * Prepares and checks given YAML file
	 * 
	 * @param file
	 * @return
	 */
	private boolean fileProcessed(File file) {
		Mode current = (Mode) settings.get(Key.MODE);

		switch (current) {
		case SEARCH:
			if (!file.exists()) {
				return false;
			}
			break;
		default: // GEN
			if (!file.exists()) { // Create new empty file
				try {
					file.createNewFile();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			break;
		}

		return true;
	}

	/**
	 * Checks current settings
	 * 
	 * @return
	 */
	private boolean isSet() {
		if (settings.containsKey(Key.MODE)) {
			if ((Mode) settings.get(Key.MODE) == Mode.GEN
					&& settings.containsKey(Key.FILE)
					&& settings.containsKey(Key.LENGTH)
					|| (Mode) settings.get(Key.MODE) == Mode.SEARCH
					&& settings.containsKey(Key.FILE)) {
				return true;
			}
		}

		return false;
	}

	static enum Key {
		MODE, FILE, LENGTH, RANGE;

		static Key parseKey(String arg) {
			Key result = null;

			if (arg.toLowerCase().contains("mode")) {
				result = MODE;
			} else if (arg.toLowerCase().contains("file")) {
				result = FILE;
			} else if (arg.toLowerCase().contains("length")) {
				result = LENGTH;
			} else if (arg.toLowerCase().contains("range")) {
				result = RANGE;
			}

			return result;
		}
	}

	static enum Mode {
		GEN, SEARCH;
	}

	public void setup(String[] args) {
		parseArgs(args);
	}

	/**
	 * Perform selected action
	 */
	public void perform() {
		// Check current settings before process
		if (!isSet()) {
			printUsage();
			return;
		}

		switch ((Mode) settings.get(Key.MODE)) {
		case SEARCH:
			int[] intArray = readIntArray();
			int unpairedInt = seacrhPrintUnpaired(intArray);
			System.out.println("Processing incoming data: "
					+ Arrays.toString(intArray));
			System.out.println("Unpaired value is: " + unpairedInt);
			break;
		default: // GEN
			if (writeToFile(genIntArray())) {
				System.out.println("File "
						+ ((File) settings.get(Key.FILE)).getAbsolutePath()
						+ " generated sucessfully!");
			}
			break;
		}

	}

	/**
	 * Generates array of integers
	 */
	private int[] genIntArray() {
		int range = 0;

		try {
			range = (Integer) settings.get(Key.RANGE);
		} catch (NullPointerException npe) {
			// ignore
		}

		range = (range == 0) ? 100 : range;

		// New int[] with odd length
		int length = (Integer) settings.get(Key.LENGTH);
		int[] resultArray = new int[length];

		// Filling array with pairs +
		for (int i = 0; i <= length / 2; i++) {
			int rndValue = (int) (Math.random() * 2 * range) - range; // Generating
																		// new
																		// random
																		// integer
			resultArray[i] = rndValue;
			resultArray[length - 1 - i] = rndValue;
		}

		// Fisher-Yates shuffle
		for (int i = 0; i < length; i++) {
			int rndIndex = new Random().nextInt(length - i); // Generating
																// random index
			int rndElement = resultArray[rndIndex];
			// Swapping elements
			resultArray[rndIndex] = resultArray[i];
			resultArray[i] = rndElement;
		}

		return resultArray;
	}

	/**
	 * Writes given array to YAML file
	 * 
	 * @param intArray
	 */
	private boolean writeToFile(int[] intArray) {
		File file = (File) settings.get(Key.FILE);
		int length = (Integer) settings.get(Key.LENGTH);

		try (BufferedWriter bufWriter = new BufferedWriter(new FileWriter(file))) {
			bufWriter.write(Arrays.toString(intArray));
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Reads incoming array from input file
	 * 
	 * @return
	 */
	private int[] readIntArray() {
		File file = (File) settings.get(Key.FILE);
		System.out.println("Reading data from: " + file.getAbsolutePath());
		String[] incoming = null;

		try (BufferedReader bufReader = new BufferedReader(new FileReader(file))) {
			incoming = bufReader.readLine().split(", ");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		int[] result = new int[incoming.length];
		for (int i = 0; i < incoming.length; i++) {
			result[i] = Integer.parseInt(incoming[i].replaceAll("\\[|\\]", ""));
		}

		return result;
	}

	/**
	 * Searching for unpaired value in given array of integers using bitwise OR
	 * (XOR) which will exclude all paired values
	 * 
	 * @param intArray
	 */
	private int seacrhPrintUnpaired(int[] intArray) {
		int result = 0;
		for (int i = 0; i < intArray.length; i++) {
			// XOR'ing values
			result ^= intArray[i];
		}

		return result;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + settings;
	}
}
