package project.processapi;

import java.util.List;

/**
 * Response object for data read operations.
 * Contains the read data and operation metadata.
 */
public class DataReadResponse {
    private final String message;
    private final String sourceLocation;
    private final List<Integer> data;
    
    public DataReadResponse(String message, String sourceLocation, List<Integer> data) {
        this.message = message;
        this.sourceLocation = sourceLocation;
        this.data = data;
    }
    
    public String getMessage() { return message; }
    public String getSourceLocation() { return sourceLocation; }
    public List<Integer> getData() { return data; }
}