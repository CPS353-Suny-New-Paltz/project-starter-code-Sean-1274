package project.networkapi;

/**
 * Response object for a submitted job.
 */
public class JobResponse {
    private final String message;

    public JobResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}