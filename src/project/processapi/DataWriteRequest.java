package project.processapi;

import java.util.List;

/**
 * Request object for writing data to an output source.
 * Encapsulates all parameters needed for a write operation.
 * This provides a clean interface that can evolve as the API changes.
 */
public class DataWriteRequest {
    private final OutputSource outputSource;
    private final List<String> results;
    private final DelimiterPair delimiters;
    
    /**
     * Constructs a new DataWriteRequest.
     *
     * @param outputSource The destination where data should be written
     * @param results The computation results to write
     * @param delimiters The delimiters to use for formatting output
     */
    public DataWriteRequest(OutputSource outputSource, List<String> results, DelimiterPair delimiters) {
        this.outputSource = outputSource;
        this.results = results;
        this.delimiters = delimiters;
    }
    
    /**
     * Returns the output source for this write request.
     *
     * @return The OutputSource specifying where to write data
     */
    public OutputSource getOutputSource() {
        return outputSource;
    }
    
    /**
     * Returns the results to be written.
     *
     * @return The list of computation results
     */
    public List<String> getResults() {
        return results;
    }
    
    /**
     * Returns the delimiters for formatting output.
     *
     * @return The DelimiterPair for formatting results
     */
    public DelimiterPair getDelimiters() {
        return delimiters;
    }
}