package project.datastoreapi;

/** Response from data write operation */
public interface DataWriteResponse {
    /** Get operation status */
    RequestStatus getStatus();
    
    /** Get status message */
    String getMessage();
}