package project.datastoreapi;

/** Basic implementation of stream configuration request */
public class BasicDataStreamRequest implements DataStreamRequest {
    private final DataStreamMode mode;
    private final int bufferSize;
    private final DataFormat dataFormat;

    /** Create stream request with mode, buffer size, and format */
    public BasicDataStreamRequest(DataStreamMode mode, int bufferSize, DataFormat dataFormat) {
        this.mode = mode;
        this.bufferSize = bufferSize;
        this.dataFormat = dataFormat;
    }

    @Override
    public DataStreamMode getMode() {
        return mode;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public DataFormat getDataFormat() {
        return dataFormat;
    }
}