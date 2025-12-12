package project.networkapi;

/**
 * Summary information about a computation job
 */
public class JobSummary {
    private final String jobId;
    private final CompletionStatus status;
    private final int progress;
    private final String outputDestination;
    private final String message;

    public JobSummary(String jobId, CompletionStatus status, int progress, 
                     String outputDestination, String message) {
        this.jobId = jobId;
        this.status = status;
        this.progress = progress;
        this.outputDestination = outputDestination;
        this.message = message;
    }

    // Getters
    public String getJobId() { 
        return jobId; 
    }
    public CompletionStatus getStatus() { 
        return status; 
    }
    public int getProgress() { 
        return progress; 
    }
    public String getOutputDestination() { 
        return outputDestination; 
    }
    public String getMessage() { 
        return message; 
    }
}