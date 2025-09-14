package project.networkapi;


/**
 * Represents a complete job submission request to the compute engine.
 * This class encapsulates all the parameters required to submit a computation job
 * in a single, cohesive object. Using a JobRequest simplifies the API method signature
 * and makes it easier to extend with additional parameters in the future.
 * 
 * Contains:
 * - InputSource: The source of data for computation
 * - OutputSource: The destination for computation results
 * - DelimiterPair: Custom delimiters for formatting output (optional, can be null)
 */
public class JobRequest {
	private final InputSource inputSource;
	private final OutputSource outputSource;
	private final DelimiterPair delimiterPair;

	/**
	 * Constructs a new JobRequest with the specified parameters.
	 *
	 * @param inputSource   The source from which input data will be read
	 * @param outputSource  The destination where output results will be written
	 * @param delimiterPair The delimiters to use for formatting output (can be null for defaults)
	 */
	public JobRequest(InputSource inputSource, OutputSource outputSource, DelimiterPair delimiterPair) {
		this.inputSource = inputSource;
		this.outputSource = outputSource;
		this.delimiterPair = delimiterPair;
	}

	/**
	 * Returns the input source for this job request.
	 *
	 * @return The InputSource specifying where to read data from
	 */
	public InputSource getInputSource() {
		return inputSource;
	}

	/**
	 * Returns the output source for this job request.
	 *
	 * @return The OutputSource specifying where to write results
	 */
	public OutputSource getOutputSource() {
		return outputSource;
	}

	/**
	 * Returns the delimiter pair for this job request.
	 * May return null if no custom delimiters were specified.
	 *
	 * @return The DelimiterPair for formatting, or null if not specified
	 */
	public DelimiterPair getDelimiterPair() {
		return delimiterPair;
	}

	/**
	 * Returns the delimiters to be used for this job, falling back to system defaults
	 * if no custom delimiters were provided. This is a convenience method that ensures
	 * we always have a valid DelimiterPair instance.
	 *
	 * @return The DelimiterPair to use (either custom or default)
	 */
	public DelimiterPair getDelimitersOrDefault() {
		return delimiterPair != null ? delimiterPair : DelimiterPair.defaultDelimiters();
	}
}

