package project.conceptualapi;

/** Configuration for initializing a computation job */
public interface JobConfig {
    /** Get input source specification */
    String getInputSource();
    
    /** Get output destination specification */
    String getOutputDestination();
    
    /** Get computation parameters */
    String getParameters();
    
    /** Get data processing unit size */
    int getProcessingUnitSize();
}