package project.processapi;

import java.util.List;

/**
 * Response object for data read operations performed by the data storage system.
 * Contains both the raw data read from the input source and comprehensive metadata
 * about the read operation. This response provides the compute engine with the
 * input data needed for computation along with operational context.
 * 
 * The data is provided as a List<Integer> as a placeholder for future streaming
 * implementation, allowing the API to evolve from batch to streaming processing.
 */
public class DataReadResponse {
	private final String message;
	private final String sourceLocation;
	private final List<Integer> data;

	/**
	 * Constructs a new DataReadResponse with complete operation details and read data.
	 *
	 * @param message        Status or informational message about the read operation.
	 *                       Typically indicates success, failure, or data characteristics.
	 * @param sourceLocation The location from which data was read (e.g., file path,
	 *                       database connection string, network endpoint).
	 * @param data           The actual data read from the source, provided as a list
	 *                       of integers. This serves as placeholder data until
	 *                       streaming implementation is complete.
	 */
	public DataReadResponse(String message, String sourceLocation, List<Integer> data) {
		this.message = message;
		this.sourceLocation = sourceLocation;
		this.data = data;
	}

	/**
	 * Returns the status message for this read operation.
	 * This message provides human-readable feedback about the operation outcome
	 * and any relevant details about the data retrieval process.
	 *
	 * @return The status message describing the read operation result
	 */
	public String getMessage() { 
		return message; 
	}

	/**
	 * Returns the source location from which data was read.
	 * This identifies the input source that provided the data for computation.
	 *
	 * @return The source location as a string representation
	 */
	public String getSourceLocation() { 
		return sourceLocation; 
	}

	/**
	 * Returns the data read from the input source.
	 * The data is provided as a list of integers, serving as a temporary
	 * implementation until streaming data processing is implemented.
	 * This data represents the input values for computation.
	 *
	 * @return The list of integer data read from the source
	 */
	public List<Integer> getData() { 
		return data; 
	}
}