package project.datastoreapi;

/** Request to write integer data to a storage destination */
public interface DataWriteRequest {
    /** Get destination location */
    String getDestination();
    
    /** Get data format for writing */
    DataFormat getFormat();
    
    /** Get the data to be written */
    String getData();
}