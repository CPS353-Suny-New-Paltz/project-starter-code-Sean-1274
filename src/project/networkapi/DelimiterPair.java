package project.networkapi;

/**
 * Defines a pair of delimiters used for formatting computation results.
 * Formatting rules for how input-result pairs
 * should be structured in the output. 
 * 
 * Example usage:
 * - Pair delimiter: ";" (separates different input-result pairs)
 * - Result delimiter: ":" (separates input from result within a pair)
 * 
 * Formatted output example: "input1:result1;input2:result2;input3:result3"
 * 
 */
public class DelimiterPair {
	private final String pairDelimiter;   // Separates different input-result pairs
	private final String resultDelimiter; // Separates input from result within a pair

	/**
	 * Constructs a new DelimiterPair with the specified delimiters.
	 *
	 * @param pairDelimiter   The delimiter that separates different input-result pairs
	 * @param resultDelimiter The delimiter that separates input from result within a pair
	 */
	public DelimiterPair(String pairDelimiter, String resultDelimiter) {
		this.pairDelimiter = pairDelimiter;
		this.resultDelimiter = resultDelimiter;
	}

	/**
	 * Provides a factory method that returns the system default delimiters.
	 * Defaults are: pair delimiter = ";", result delimiter = ":".
	 *
	 * @return A DelimiterPair instance with default delimiter values
	 */
	public static DelimiterPair defaultDelimiters() {
		return new DelimiterPair(";", ":");
	}

	/**
	 * Returns the pair delimiter used to separate different input-result pairs.
	 * This delimiter appears between complete pairs in the output.
	 *
	 * @return The pair delimiter string
	 */
	public String getPairDelimiter() {
		return pairDelimiter;
	}

	/**
	 * Returns the result delimiter used to separate input from result within a pair.
	 * This delimiter appears between the input value and its corresponding result.
	 *
	 * @return The result delimiter string
	 */
	public String getResultDelimiter() {
		return resultDelimiter;
	}
}