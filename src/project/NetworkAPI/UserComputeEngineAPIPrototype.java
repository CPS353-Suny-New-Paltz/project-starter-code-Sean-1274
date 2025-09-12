package project.NetworkAPI;

import project.annotations.NetworkAPIPrototype;

/**
 * Prototype implementation of the UserComputeEngineAPI.
 * 
 * Purpose:
 * - Demonstrates how the API could be used.
 * - Does not run real computation (e.g., no factorial calculation).
 * - Returns a "dummy" JobResponse with metadata so developers can test flow.
 */
public class UserComputeEngineAPIPrototype implements UserComputeEngineAPI {

	@Override 
	@NetworkAPIPrototype
	public JobResponse submitJob(InputSource inputSource, 
			OutputSource outputSource, 
			DelimiterPair delimiterPair) {

		// If no custom delimiters provided, fall back to system defaults
		if (delimiterPair == null) {
			delimiterPair = DelimiterPair.defaultDelimiters();
		}

		// Return a "mock" response — this would later be replaced
		// with actual computation results
		return new JobResponse(
				"Prototype job submitted successfully",
				inputSource.toString(),
				outputSource.toString(),
				delimiterPair
				);
	}
}

