package project.conceptualapi;

import project.annotations.ConceptualAPIPrototype;
import java.util.List;
import java.util.ArrayList;

/**
 * Prototype implementation of the ComputationAPI.
 * This provides mock factorial calculations for testing the API flow without
 * implementing the actual mathematical algorithms yet.
 * 
 * Purpose:
 * - Demonstrates the API contract between orchestrator and computation components
 * - Provides placeholder implementation for testing the computation flow
 * - Returns mock factorial results for development and testing
 */
public class ComputationAPIPrototype implements ComputationAPI {

	/**
	 * Prototype implementation for computation operations.
	 * Returns mock factorial results instead of actual calculations.
	 * In a real implementation, this would perform actual factorial computations.
	 *
	 * @param computationRequest The computation request containing input values
	 * @return ComputationResponse with mock factorial results
	 */
	@Override
	@ConceptualAPIPrototype
	public ComputationResponse compute(ComputationRequest computationRequest) {
		// Mock computation - in real implementation, this would calculate actual factorials
		List<String> mockResults = new ArrayList<>();
		for (Integer input : computationRequest.getInputData()) {
			mockResults.add(input + "! = " + "mock_result_for_" + input);
		}

		return new ComputationResponse(
				"Prototype: Successfully computed mock factorials",
				computationRequest.getInputData().size(),
				mockResults
				);
	}
}