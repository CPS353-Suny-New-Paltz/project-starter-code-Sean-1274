package project.networkapi;

/** Job completion status */
public enum CompletionStatus {
    JOB_RUNNING,     // Job is still processing
    JOB_COMPLETED,   // Job finished successfully
    JOB_FAILED,      // Job failed with errors
    JOB_NOT_FOUND    // Job ID does not exist
}