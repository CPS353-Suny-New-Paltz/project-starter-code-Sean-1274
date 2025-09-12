package project.NetworkAPI;

/**
 * Response object for a submitted job.
 * 
 * Contains:
 * - Message about job submission (status info)
 * - Input source (where input came from)
 * - Output source (where output is stored)
 * - Delimiters used for formatting
 */
public class JobResponse {
	private final String message;
	private final String inputSource;
	private final String outputSource;
	private final DelimiterPair delimiters;

	public JobResponse(String message, String inputSource, String outputSource, DelimiterPair delimiters) {
		this.message = message;
		this.inputSource = inputSource;
		this.outputSource = outputSource;
		this.delimiters = delimiters;
	}

	public String getMessage() { return message; }
	public String getInputSource() { return inputSource; }
	public String getOutputSource() { return outputSource; }
	public DelimiterPair getDelimiters() { return delimiters; }
}

