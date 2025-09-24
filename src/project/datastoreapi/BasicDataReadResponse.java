package project.datastoreapi;

// Basic implementation of DataReadResponse
public class BasicDataReadResponse implements DataReadResponse {
    private final RequestStatus status;
    private final String message;
    private final int[] data;

    public BasicDataReadResponse(RequestStatus status, String message, int[] data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    @Override
    public RequestStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int[] getData() {
        return data;
    }
}