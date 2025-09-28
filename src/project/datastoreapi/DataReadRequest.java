package project.datastoreapi;

/** Request to read integer data from a storage source */
public interface DataReadRequest {
    /** Get source location (file path, DB URI, etc.) */
    String getSource();
    
    /** Get data format for reading */
    DataFormat getFormat();
}