package project.datastoreapi;

/** Request to configure data streaming parameters */
public interface DataStreamRequest {
    /** Get streaming mode (batch or stream) */
    DataStreamMode getMode();
    
    /** Get buffer size for streaming */
    int getBufferSize();
    
    /** Get format of streamed data */
    DataFormat getDataFormat();
}