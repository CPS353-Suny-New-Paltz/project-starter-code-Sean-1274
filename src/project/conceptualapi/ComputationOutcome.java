package project.conceptualapi;

/** Final outcome of computation job */
public enum ComputationOutcome {
    SUCCESSFUL,         // Job completed successfully
    PARTIAL_SUCCESS,    // Job completed with some issues
    FAILED,             // Job failed completely
    CANCELLED,          // Job was cancelled by user
    TIMED_OUT           // Job timed out
}