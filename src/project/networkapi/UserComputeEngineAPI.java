package project.networkapi;

import project.annotations.NetworkAPI;

/**
 * API interface between the user and the compute engine.
 * This API defines the contract for submitting computation jobs to the engine.
 * The interface is annotated with @NetworkAPI to indicate it's part of the
 * network-facing API surface that will be exposed to users.
 * 
 * Responsibilities:
 * - Provides a single method for job submission
 * - Accepts a complete JobRequest object containing all necessary parameters
 * - Returns a JobResponse with metadata about the submitted job
 * 
 * Note: This API does not perform computation itself - it serves as the
 * entry point for submitting jobs to the underlying compute engine.
 */
@NetworkAPI
public interface UserComputeEngineAPI {
	/**
	 * Submits a computation job to the compute engine.
	 * This is the primary method through which users request computations.
	 * The method accepts a JobRequest object containing all necessary
	 * configuration and returns a JobResponse with submission details.
	 *
	 * @param jobRequest The complete job request containing:
	 *                   - Input source specification
	 *                   - Output destination specification  
	 *                   - Optional custom delimiters for formatting
	 * @return JobResponse containing:
	 *                   - Status message about submission
	 *                   - Details about input/output sources
	 *                   - Delimiters that will be used
	 */
	JobResponse submitJob(JobRequest jobRequest);
}