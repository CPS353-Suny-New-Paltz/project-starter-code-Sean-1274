package project.conceptualapi;

import project.annotations.ConceptualAPI;

/**
 * API between the orchestrator component and the computation component within the compute engine.
 * The orchestrator (user of this API) handles job initialization, data reading/writing, and
 * delegates the actual computation to this API. The computation component performs the
 * mathematical operations (e.g., factorial calculations).
 * 
 * Responsibilities:
 * - Accept computation requests from the orchestrator
 * - Perform mathematical computations on input data
 * - Return computation results for the orchestrator to handle output
 */
@ConceptualAPI
public interface ComputationAPI {

	/**
	 * Performs computation on a set of input values.
	 * The orchestrator calls this method to delegate actual mathematical operations
	 * after reading input data and before writing results.
	 *
	 * @param computationRequest The computation request containing input values to process
	 * @return ComputationResponse containing the results of all computations
	 */
	ComputationResponse compute(ComputationRequest computationRequest);
}