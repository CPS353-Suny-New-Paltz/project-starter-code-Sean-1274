package project.NetworkAPI;

/**
 * Defines delimiters for formatting results.
 * 
 * Example:
 * Input/Output Pair → separated by ";"
 * Input/Result      → separated by ":"
 * 
 * So a formatted string might look like:
 *   "input1:result1;input2:result2;..."
 */
public class DelimiterPair {
	private final String pairDelimiter;   // separates pairs
	private final String resultDelimiter; // separates input from result

	public DelimiterPair(String pairDelimiter, String resultDelimiter) {
		this.pairDelimiter = pairDelimiter;
		this.resultDelimiter = resultDelimiter;
	}

	// Factory method to return default delimiters
	public static DelimiterPair defaultDelimiters() {
		return new DelimiterPair(";", ":");
	}

	public String getPairDelimiter() { return pairDelimiter; }
	public String getResultDelimiter() { return resultDelimiter; }
}

