package project.networkapi;

/** Basic implementation */
public class BasicJobStatusRequest implements JobStatusRequest {
    private final String jobIdentifier;

    public BasicJobStatusRequest(String jobIdentifier) {
        this.jobIdentifier = jobIdentifier;
    }

    @Override
    public String getJobIdentifier() {
        return jobIdentifier;
    }
}