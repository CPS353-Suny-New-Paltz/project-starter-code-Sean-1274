package project.conceptualapi;

/** Overall computation job status */
public enum ComputationStatus {
    INITIALIZING,       // Job is being initialized
    PROCESSING,         // Actively processing data
    PAUSED,             // Processing paused
    COMPLETED,          // Processing completed successfully
    FAILED,             // Processing failed
    FINALIZING          // Final cleanup in progress
}