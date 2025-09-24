package project.networkapi;

// Basic implementation of DelimiterResponse
public class BasicDelimiterResponse implements DelimiterResponse {
    private final String appliedDelimiters;
    private final RequestStatus status;
    private final String message;

    public BasicDelimiterResponse(String appliedDelimiters, RequestStatus status, String message) {
        this.appliedDelimiters = appliedDelimiters;
        this.status = status;
        this.message = message;
    }

    @Override
    public String getAppliedDelimiters() {
        return appliedDelimiters;
    }

    @Override
    public RequestStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}