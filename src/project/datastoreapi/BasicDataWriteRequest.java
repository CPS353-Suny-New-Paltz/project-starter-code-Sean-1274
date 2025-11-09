package project.datastoreapi;

/** Basic implementation of data write request */
public class BasicDataWriteRequest implements DataWriteRequest {
	private final String destination;
	private final DataFormat format;
	private final String data;

	/** Create write request for specific destination and format */
	public BasicDataWriteRequest(String destination, DataFormat format, String data) {
		this.destination = destination;
		this.format = format;
		this.data = data;
	}

	@Override
	public String getDestination() {
		return destination;
	}

	@Override
	public DataFormat getFormat() {
		return format;
	}

	// Add method to get the data
	@Override
	public String getData() {
		return data;
	}
}