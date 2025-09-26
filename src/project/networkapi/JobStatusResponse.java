package project.networkapi;

/** Response for job status check */
public interface JobStatusResponse {
    CompletionStatus getStatus();      // Job completion status
    String getMessage();               // Additional status message
    int getProgress();                 // Progress percentage (0-100)
    RequestStatus getRequestStatus();  // Whether status request was ACCEPTED/REJECTED
}