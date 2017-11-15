

import java.util.Scanner;

public class PalindromeFinder {

	private int input, result;
	private boolean isIncremented;

	public PalindromeFinder() {
	}
	
	public static void main(String[] args) {
		PalindromeFinder finder = new PalindromeFinder();
		finder.process();
	}

	/**
	 * Main entry point
	 */
	public void process() {
		readInt();

		do {
			findNextPalindrome();
		} while (!isPalindrome());

		System.out.println();
		System.out.println("Next palindrome is: " + result);
	}

	/**
	 * Reads natural number from stdout
	 */
	private void readInt() {
		while (input <= 0) {
			System.out.print("Enter a natural number: ");
			input = result = (int) new Scanner(System.in).nextInt();
		}
	}

	/**
	 * Checks whether the result is a palindrome
	 * 
	 * @return
	 */
	private boolean isPalindrome() {
		StringBuilder builder = new StringBuilder(String.valueOf(result));
		String straight = builder.toString();
		String reversed = builder.reverse().toString();

		return straight.equals(reversed);
	}

	/**
	 * Searches for the next bigger palindrome
	 */
	private void findNextPalindrome() {
		String hi = null, lo = null, mid = null, revHi = null;

		String resultStr = String.valueOf(result);
		int midIndex = resultStr.length() / 2;

		hi = resultStr.substring(0, midIndex);
		lo = resultStr.substring(resultStr.length() - midIndex);
		mid = String.valueOf((resultStr.length() % 2 == 1) ? resultStr
				.charAt(midIndex) : "");
		revHi = new StringBuilder(hi).reverse().toString();

		if (resultStr.length() > 1 // for numbers between [1,9]
				&& Integer.parseInt(revHi) > Integer.parseInt(lo)
				|| isIncremented) {
			result = Integer.parseInt(hi + mid + revHi);
		} else {
			increaseResult();
			findNextPalindrome(); // Recursive call
		}
	}

	/**
	 * Increases the result to ensure that the middle digit is changed
	 */
	private void increaseResult() {
		result += Math.pow(10, String.valueOf(result).length() / 2);
		isIncremented = true;
	}
}
