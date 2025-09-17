package project.conceptualapi;

/** Container for processed computation results */
public interface ProcessedData {
    /** Get processed results array */
    int[] getResults();
    
    /** Get data unit index for these results */
    int getDataUnitIndex();
    
    /** Get processing metadata or tags */
    String getProcessingMetadata();
}