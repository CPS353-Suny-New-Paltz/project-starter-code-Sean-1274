package project.datastoreapi;

/** Response from stream configuration operation */
public interface DataStreamResponse {
    /** Get operation status */
    RequestStatus getStatus();
    
    /** Get status message */
    String getMessage();
    
    /** Get applied streaming mode */
    DataStreamMode getAppliedMode();
    
    /** Get applied buffer size */
    int getAppliedBufferSize();
}