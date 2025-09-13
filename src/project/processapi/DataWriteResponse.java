package project.processapi;

/**
 * Response object for data write operations performed by the data storage system.
 * Contains comprehensive metadata about the write operation, including status,
 * destination information, formatted output, and quantitative results.
 * 
 * This response provides feedback to the compute engine about the success and
 * details of the write operation to the specified output destination.
 */
public class DataWriteResponse {
    private final String message;
    private final String destinationLocation;
    private final String formattedOutput;
    private final int itemsWritten;
    
    /**
     * Constructs a new DataWriteResponse with complete operation details.
     *
     * @param message             Status or informational message about the write operation.
     *                            Typically indicates success, failure, or other operational details.
     * @param destinationLocation The location where data was written (e.g., file path, 
     *                            database connection string, network endpoint).
     * @param formattedOutput     The actual formatted output that was written, using the
     *                            specified delimiters for structure.
     * @param itemsWritten        The number of individual items or records successfully written.
     *                            This provides quantitative feedback about the operation scale.
     */
    public DataWriteResponse(String message, String destinationLocation, 
                           String formattedOutput, int itemsWritten) {
        this.message = message;
        this.destinationLocation = destinationLocation;
        this.formattedOutput = formattedOutput;
        this.itemsWritten = itemsWritten;
    }
    
    /**
     * Returns the status message for this write operation.
     * This message provides human-readable feedback about the operation outcome.
     *
     * @return The status message describing the write operation result
     */
    public String getMessage() { 
        return message; 
    }
    
    /**
     * Returns the destination location where data was written.
     * This identifies the output target that received the computation results.
     *
     * @return The destination location as a string representation
     */
    public String getDestinationLocation() {
        return destinationLocation; 
    }
    
    /**
     * Returns the formatted output that was written to the destination.
     * This represents the actual data content that was stored, formatted
     * according to the specified delimiter rules.
     *
     * @return The formatted output string that was written
     */
    public String getFormattedOutput() { 
        return formattedOutput; 
    }
    
    /**
     * Returns the number of items successfully written.
     * This quantitative measure helps verify the operation completeness
     * and can be used for validation and logging purposes.
     *
     * @return The count of items written during the operation
     */
    public int getItemsWritten() { 
        return itemsWritten; 
    }
}