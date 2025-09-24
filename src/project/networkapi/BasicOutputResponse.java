package project.networkapi;

// Basic implementation of OutputResponse
public class BasicOutputResponse implements OutputResponse {
    private final RequestStatus status;
    private final String message;

    public BasicOutputResponse(RequestStatus status, String message) {
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