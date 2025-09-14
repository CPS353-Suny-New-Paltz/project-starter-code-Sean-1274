package project.networkapi;

/**
 * Represents a job request submitted by the user.
 * 
 * Contains metadata about:
 * - Where input comes from (InputSource)
 * - Where output should go (OutputSource)
 * - How results should be formatted (DelimiterPair)
 */
public class JobRequest {
	private final InputSource inputSource;
	private final OutputSource outputSource;
	private final DelimiterPair delimiters;

	public JobRequest(InputSource inputSource,
			OutputSource outputSource,
			DelimiterPair delimiters) {
		this.inputSource = inputSource;
		this.outputSource = outputSource;
		// If no delimiters provided, fall back to defaults
		this.delimiters = (delimiters != null)
				? delimiters
						: DelimiterPair.defaultDelimiters();
	}

	public InputSource getInputSource() { 
		return inputSource; 
	}
	public OutputSource getOutputSource() { 
		return outputSource; 
	}
	public DelimiterPair getDelimiters() { 
		return delimiters; 
	}

	@Override
	public String toString() {
		return "JobRequest{" +
				"input=" + inputSource.getLocation() +
				", output=" + outputSource.getLocation() +
				", delimiters=" + delimiters.getPairDelimiter() +
				"|" + delimiters.getResultDelimiter() +
				'}';
	}
}
