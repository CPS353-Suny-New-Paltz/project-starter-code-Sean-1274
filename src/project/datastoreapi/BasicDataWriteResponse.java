package project.datastoreapi;

// Basic implementation of DataWriteResponse
public class BasicDataWriteResponse implements DataWriteResponse {
    private final RequestStatus status;
    private final String message;

    public BasicDataWriteResponse(RequestStatus status, String message) {
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