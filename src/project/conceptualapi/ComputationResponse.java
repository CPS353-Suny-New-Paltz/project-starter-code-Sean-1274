package project.conceptualapi;

import java.util.List;

/**
 * Response object for computation operations.
 * Contains the results of mathematical computations performed by the computation component.
 * The orchestrator uses these results to write output via the data storage system.
 */
public class ComputationResponse {
	private final String message;
	private final int itemsComputed;
	private final List<String> results;

	/**
	 * Constructs a new ComputationResponse with computation results.
	 *
	 * @param message       Status message about the computation operation
	 * @param itemsComputed The number of items successfully computed
	 * @param results       The computation results, typically formatted strings
	 *                      representing input-output pairs (e.g., "5! = 120")
	 */
	public ComputationResponse(String message, int itemsComputed, List<String> results) {
		this.message = message;
		this.itemsComputed = itemsComputed;
		this.results = results;
	}

	/**
	 * Returns the status message for this computation operation.
	 *
	 * @return The status message describing computation results
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the number of items successfully computed.
	 *
	 * @return The count of computed items
	 */
	public int getItemsComputed() {
		return itemsComputed;
	}

	/**
	 * Returns the computation results.
	 * Each result string typically represents an input-output pair formatted
	 * for output storage (e.g., "5:120" or "5! = 120").
	 *
	 * @return The list of computation results
	 */
	public List<String> getResults() {
		return results;
	}
}