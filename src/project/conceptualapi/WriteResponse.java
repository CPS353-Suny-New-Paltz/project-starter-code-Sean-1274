package project.conceptualapi;

/** Response from write operation */
public interface WriteResponse {
    /** Get write operation status */
    OperationStatus getStatus();
    
    /** Get status message */
    String getMessage();
    
    /** Get number of units written */
    int getUnitsWritten();
}