package project.conceptualapi;

/** Data availability status */
public enum DataAvailability {
    AVAILABLE,          // Data is available for processing
    NO_MORE_DATA,       // No more data available (processing complete)
    DATA_PENDING,       // Data is being prepared, check back later
    DATA_ERROR          // Error occurred while preparing data
}