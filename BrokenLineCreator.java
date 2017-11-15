import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class BrokenLineCreator {

	/**
	 * Incoming dots coordinates in form of Integer[x,y]
	 */
	private List<Integer[]> dots;

	/**
	 * Key-value pairs. Key - the index of a dot from the dots list. Value -
	 * angle in radians formed by the imaginary trigonometric cosine axis, and
	 * the line between current dot and the intersection of the axes of the sine
	 * and cosine.
	 */
	private List<Entry<Integer, Double>> sortedDots;

	public BrokenLineCreator() {
	}

	/**
	 * Registers incoming dots coordinates for current instance
	 * 
	 * @param dots
	 */
	public void init(List<Integer[]> dots) {
		this.dots = new ArrayList<Integer[]>(dots);
	}

	/**
	 * Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		BrokenLineCreator lineCreator = new BrokenLineCreator();
		int maxDots = 7;
		int maxX = 800;
		int maxY = 600;
		lineCreator.init(generateRandomDots(maxDots, maxX, maxY));
		lineCreator.createBrokenLine();
	}

	/**
	 * 
	 */
	public void createBrokenLine() {
		System.out.print("Processing incoming dots: ");
		for (Integer[] dot : dots) {
			System.out.print(Arrays.toString(dot) + " ");
		}
		System.out.println();

		Map<Integer, Double> degMap = new HashMap<Integer, Double>();

		// Searching for min/max x/y
		// Gathering xs/ys of dots
		int[] xs = new int[dots.size()];
		int[] ys = new int[dots.size()];

		for (int i = 0; i < dots.size(); i++) {
			degMap.put(i, 0d); // index-degree
			xs[i] = dots.get(i)[0];
			ys[i] = dots.get(i)[1];
		}

		// Sorting xs/ys
		Arrays.sort(xs);
		Arrays.sort(ys);

		// Calculating center
		int[][] center = new int[1][2];
		center[0][0] = xs[0] + (xs[xs.length - 1] - xs[0]) / 2;
		center[0][1] = ys[0] + (ys[xs.length - 1] - ys[0]) / 2;

		// Calculating degree in radians
		for (Entry<Integer, Double> entry : degMap.entrySet()) {
			int x = dots.get(entry.getKey())[0];
			int y = dots.get(entry.getKey())[1];

			int b = 0;
			int a = 0;
			double shift = 0d;
			int sign = 1;

			if (x > center[0][0] && y < center[0][1]) {
				a = x - center[0][0];
				b = center[0][1] - y;
			} else if (x < center[0][0] && y < center[0][1]) {
				a = center[0][0] - x;
				b = center[0][1] - y;
				shift = Math.PI;
				sign = -1;
			} else if (x < center[0][0] && y > center[0][1]) {
				a = center[0][0] - x;
				b = y - center[0][1];
				shift = Math.PI;
				sign = 1;
			} else if (x > center[0][0] && y > center[0][1]) {
				a = x - center[0][0];
				b = y - center[0][1];
				shift = Math.PI * 2;
				sign = -1;
			}

			double tan = (double) b / (double) a; // Calculating tangent
			double angle = Math.atan(tan) * sign + shift; // Calculating forming
															// angle

			degMap.put(entry.getKey(), angle);
		}

		// Sorting map by degree (value)
		sortedDots = getSortedDotsByComparator(degMap);

		// Result output
		System.out.println();
		System.out.println("Broken line formed, connect the dots in following order:");
		for (Entry<Integer, Double> entry : sortedDots) {
			System.out.println(entry.getKey() + ". " + Arrays.toString(dots.get(entry.getKey())));
		}
	}

	/**
	 * Generates random dots
	 */
	public static List<Integer[]> generateRandomDots(int maxDots, int maxX,
			int maxY) {
		List<Integer[]> result = new ArrayList<Integer[]>(maxDots);

		Random random = new Random();

		for (int i = 0; i < maxDots; i++) {
			Integer[] dot = new Integer[] { random.nextInt(maxX),
					random.nextInt(maxY) };
			result.add(dot);
		}

		return result;
	}

	/**
	 * Sorts dots by the forming angle
	 * 
	 * @param map
	 * @return
	 */
	private List<Entry<Integer, Double>> getSortedDotsByComparator(
			Map<Integer, Double> map) {
		// Converting Map to List
		List<Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(
				map.entrySet());

		// Sorting List by Comparator
		Collections.sort(list, new Comparator<Entry<Integer, Double>>() {
			@Override
			public int compare(Entry<Integer, Double> o1,
					Entry<Integer, Double> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});

		return list;
	}
}
