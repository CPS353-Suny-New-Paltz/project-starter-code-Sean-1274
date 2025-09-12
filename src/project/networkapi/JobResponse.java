package project.networkapi;

/**
 * Response object returned after submitting a job to the compute engine.
 * This class provides feedback about the job submission process and contains
 * metadata about how the job was configured. In a real implementation, this would
 * also include computation results and execution details.
 * 
 * Contains:
 * - Status message indicating submission success/failure
 * - Information about the input source used
 * - Information about the output destination configured
 * - The delimiters that will be used for formatting results
 */
public class JobResponse {
	private final String message;
	private final String inputSource;
	private final String outputSource;
	private final DelimiterPair delimiters;

	/**
	 * Constructs a new JobResponse with the specified details.
	 *
	 * @param message       Status or informational message about the job submission
	 * @param inputSource   String representation of the input source
	 * @param outputSource  String representation of the output destination
	 * @param delimiters    The delimiters that will be used for formatting output
	 */
	public JobResponse(String message, String inputSource, String outputSource, DelimiterPair delimiters) {
		this.message = message;
		this.inputSource = inputSource;
		this.outputSource = outputSource;
		this.delimiters = delimiters;
	}

	/**
	 * Returns the status message for this job response.
	 * This typically indicates whether the job was submitted successfully or
	 * provides other informational details about the submission process.
	 *
	 * @return The status message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns information about the input source used for this job.
	 * This is a string representation that helps identify where the input data
	 * will be read from (e.g., file path, database connection string).
	 *
	 * @return String representation of the input source
	 */
	public String getInputSource() {
		return inputSource;
	}

	/**
	 * Returns information about the output destination for this job.
	 * This helps identify where the computation results will be written
	 * (e.g., file path, database table, console output).
	 *
	 * @return String representation of the output destination
	 */
	public String getOutputSource() {
		return outputSource;
	}

	/**
	 * Returns the delimiters that will be used to format the computation results.
	 * These determine how the output will be structured and separated.
	 *
	 * @return The DelimiterPair containing the formatting delimiters
	 */
	public DelimiterPair getDelimiters() {
		return delimiters;
	}
}