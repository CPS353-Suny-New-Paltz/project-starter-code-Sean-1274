package project.conceptualapi;
import project.annotations.ConceptualAPI;

/** 
 * API between compute engine components: 
 * - User: Computation Component (does actual processing)
 * - Provider: Initialization Component (handles setup and I/O)
 */
@ConceptualAPI
public interface ComputeEngineAPI {
    
    /** Initialize computation job and return initialization response */
    JobInitResponse initializeComputation(JobConfig config);
    
    /** Read input data and return data response */
    DataResponse readInputData();
    
    /** Write processed results and return write confirmation */
    WriteResponse writeProcessedResults(ProcessedData results);
    
    /** Get job status and return status information */
    JobStatusResponse getComputationStatus();
    
    /** Finalize job and return completion response */
    CompletionResponse finalizeComputation();
}