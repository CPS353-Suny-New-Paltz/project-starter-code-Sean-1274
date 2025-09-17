package project.datastoreapi;

/** Response from data read operation */
public interface DataReadResponse {
    /** Get operation status */
    RequestStatus getStatus();
    
    /** Get status message */
    String getMessage();
    
    /** Get read integer data (array wrapper) */
    int[] getData();
}