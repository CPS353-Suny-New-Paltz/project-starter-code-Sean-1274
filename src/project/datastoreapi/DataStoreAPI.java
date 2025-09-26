package project.datastoreapi;
import project.annotations.ProcessAPI;

/** API for data storage operations between compute engine and storage system */
@ProcessAPI
public interface DataStoreAPI {
    /** Read integer data from specified source */
    DataReadResponse readData(DataReadRequest request);
    
    /** Write integer data to specified destination */
    DataWriteResponse writeData(DataWriteRequest request);
    
    /** Configure data streaming parameters */
    DataStreamResponse configureStream(DataStreamRequest request);
}