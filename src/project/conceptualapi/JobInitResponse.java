package project.conceptualapi;

/** Response to job initialization request */
public interface JobInitResponse {
    /** Get job ID for tracking */
    String getJobId();
    
    /** Get initialization status */
    OperationStatus getStatus();
    
    /** Get status message */
    String getMessage();
    
    /** Get estimated total data units */
    int getTotalDataUnits();
}