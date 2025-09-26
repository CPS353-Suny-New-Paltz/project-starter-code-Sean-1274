package project.networkapi;

// Basic implementation of InputResponse
public class BasicInputResponse implements InputResponse {
    private final RequestStatus status;
    private final String message;

    public BasicInputResponse(RequestStatus status, String message) {
        this.status = status;
        this.message = message;
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