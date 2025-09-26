package project.networkapi;

// Basic implementation of JobStatusResponse
public class BasicJobStatusResponse implements JobStatusResponse {
    private final CompletionStatus status;
    private final String message;
    private final int progress;
    private final RequestStatus requestStatus;

    public BasicJobStatusResponse(CompletionStatus status, String message, int progress, RequestStatus requestStatus) {
        this.status = status;
        this.message = message;
        this.progress = progress;
        this.requestStatus = requestStatus;
    }

    @Override
    public CompletionStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public RequestStatus getRequestStatus() {
        return requestStatus;
    }
}