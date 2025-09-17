package project.datastoreapi;

/** Basic implementation of data read request */
public class BasicDataReadRequest implements DataReadRequest {
    private final String source;
    private final DataFormat format;

    /** Create read request for specific source and format */
    public BasicDataReadRequest(String source, DataFormat format) {
        this.source = source;
        this.format = format;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public DataFormat getFormat() {
        return format;
    }
}