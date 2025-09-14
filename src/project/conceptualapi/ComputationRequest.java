package project.conceptualapi;

import java.util.List;

/**
 * Request object for computation operations.
 * Encapsulates the input data that needs to be processed by the computation component.
 * The orchestrator creates this request after reading data from the storage system.
 */
public class ComputationRequest {
	private final List<Integer> inputData;

	/**
	 * Constructs a new ComputationRequest with the input data to process.
	 *
	 * @param inputData The list of integer values requiring computation (e.g., factorials)
	 */
	public ComputationRequest(List<Integer> inputData) {
		this.inputData = inputData;
	}

	/**
	 * Returns the input data requiring computation.
	 * This data typically comes from the data storage system via the orchestrator.
	 *
	 * @return The list of integer values to be computed
	 */
	public List<Integer> getInputData() {
		return inputData;
	}
}