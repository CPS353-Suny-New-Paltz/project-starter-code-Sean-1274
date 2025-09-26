package project.datastoreapi;

// Basic implementation of DataStreamResponse
public class BasicDataStreamResponse implements DataStreamResponse {
    private final RequestStatus status;
    private final String message;
    private final DataStreamMode appliedMode;
    private final int appliedBufferSize;

    public BasicDataStreamResponse(RequestStatus status, String message, 
                                 DataStreamMode appliedMode, int appliedBufferSize) {
        this.status = status;
        this.message = message;
        this.appliedMode = appliedMode;
        this.appliedBufferSize = appliedBufferSize;
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
    public DataStreamMode getAppliedMode() {
        return appliedMode;
    }

    @Override
    public int getAppliedBufferSize() {
        return appliedBufferSize;
    }
}