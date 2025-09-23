package project.networkapi;

/** Request to check job status */
public interface JobStatusRequest {
    String getJobIdentifier();  // Unique ID for the job
}