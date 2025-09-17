package project.conceptualapi;

/** Response containing job status information */
public interface JobStatusResponse {
    /** Get job ID */
    String getJobId();
    
    /** Get current progress percentage */
    int getProgressPercentage();
    
    /** Get number of data units processed */
    int getDataUnitsProcessed();
    
    /** Get computation status */
    ComputationStatus getComputationStatus();
    
    /** Get status message */
    String getStatusMessage();
}