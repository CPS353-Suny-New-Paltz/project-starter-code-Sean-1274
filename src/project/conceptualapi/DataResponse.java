package project.conceptualapi;

/** Response containing input data for computation */
public interface DataResponse {
    /** Get data array for processing */
    int[] getData();
    
    /** Get data availability status */
    DataAvailability getAvailability();
    
    /** Get current data unit index */
    int getCurrentUnitIndex();
    
    /** Get total data units processed so far */
    int getUnitsProcessed();
}