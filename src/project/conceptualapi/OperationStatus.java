package project.conceptualapi;

/** Status codes for API operations */
public enum OperationStatus {
    SUCCESS,            // Operation completed successfully
    PARTIAL_SUCCESS,    // Operation partially completed
    FAILED,             // Operation failed
    IN_PROGRESS,        // Operation still in progress
    NOT_STARTED         // Operation not yet started
}