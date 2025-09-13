package project.processapi;

import project.annotations.ProcessAPI;

/**
 * API between the data storage system and the compute engine.
 */
@ProcessAPI
public interface DataStorageAPI {
    
    /**
     * Reads data from the specified input source.
     *
     * @param readRequest The data read request containing input source
     * @return DataReadResponse containing the read data and metadata
     */
    DataReadResponse readData(DataReadRequest readRequest);
    
    /**
     * Writes data to the specified output source.
     *
     * @param writeRequest The data write request containing output source, 
     *                     results, and delimiters
     * @return DataWriteResponse containing write status and metadata
     */
    DataWriteResponse writeData(DataWriteRequest writeRequest);
}