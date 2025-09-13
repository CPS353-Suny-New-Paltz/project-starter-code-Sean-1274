package project.processapi;

/**
 * Request object for reading data from an input source.
 * Encapsulates all parameters needed for a read operation.
 * This provides a clean interface that can evolve as the API changes.
 */
public class DataReadRequest {
    private final InputSource inputSource;
    
    /**
     * Constructs a new DataReadRequest.
     *
     * @param inputSource The source from which to read data
     */
    public DataReadRequest(InputSource inputSource) {
        this.inputSource = inputSource;
    }
    
    /**
     * Returns the input source for this read request.
     *
     * @return The InputSource specifying where to read data from
     */
    public InputSource getInputSource() {
        return inputSource;
    }
}