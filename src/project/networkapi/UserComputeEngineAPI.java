package project.networkapi;

import project.annotations.NetworkAPI;

/**
 * API between the user and the compute engine.
 * 
 * Responsibilities:
 * - Accepts user requests for computation.
 * - User specifies where input comes from (e.g., file, DB, network).
 * - User specifies where output should go (e.g., file, DB, console).
 * - User may specify custom delimiters for formatting results.
 * - If delimiters are not provided, defaults are used.
 * 
 * This API does NOT do computation itself — it just defines how jobs
 * are submitted to the compute engine.
 */
@NetworkAPI
public interface UserComputeEngineAPI {

	/**
	 * Submit a job to the compute engine.
	 *
	 * @param inputSource   The source of input (generalized: local file, database, 
	 *                      API, etc. — anything that is used to provide data).
	 * @param outputSource  The destination for output (generalized: file, database,
	 *                      console, etc. — anything that is used to store results).
	 * @param delimiterPair Delimiters for output formatting. If null, defaults are used.
	 *                      Example: pairDelimiter = ";" , resultDelimiter = ":".
	 * @return JobResponse  A response with job metadata (status, sources, delimiters).
	 */
	JobResponse submitJob(InputSource inputSource, 
			OutputSource outputSource, 
			DelimiterPair delimiterPair);
}

