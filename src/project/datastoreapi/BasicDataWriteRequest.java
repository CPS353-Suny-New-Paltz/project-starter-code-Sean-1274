package project.datastoreapi;


/** Basic implementation of data write request */
public class BasicDataWriteRequest implements DataWriteRequest {
    private final String destination;
    private final DataFormat format;

    /** Create write request for specific destination and format */
    public BasicDataWriteRequest(String destination, DataFormat format) {
        this.destination = destination;
        this.format = format;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    @Override
    public DataFormat getFormat() {
        return format;
    }
}