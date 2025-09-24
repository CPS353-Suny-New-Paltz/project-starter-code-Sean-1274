package project.networkapi;

import project.conceptualapi.ComputeEngineAPI;

// Empty implementation of UserComputeAPI for Checkpoint 3
// Based on the system design: User -> Network API -> Compute Engine -> ...
public class EmptyUserComputeAPI implements UserComputeAPI {

    // The diagram shows Network API's direct dependency is the Compute Engine.
    private final ComputeEngineAPI computeEngine;

    // Constructor only needs the ComputeEngineAPI
    public EmptyUserComputeAPI(ComputeEngineAPI computeEngine) {
        this.computeEngine = computeEngine;
    }

    @Override
    public InputResponse setInputSource(InputRequest request) {
        // Empty implementation - returns a rejected response
        // In the future, this will delegate to computeEngine.setInputSource(request) or similar
        return new BasicInputResponse(RequestStatus.REJECTED, "Empty implementation - not yet functional");
    }

    @Override
    public OutputResponse setOutputDestination(OutputRequest request) {
        // Empty implementation - returns a rejected response
        return new BasicOutputResponse(RequestStatus.REJECTED, "Empty implementation - not yet functional");
    }

    @Override
    public DelimiterResponse configureDelimiters(DelimiterRequest request) {
        // Empty implementation - returns a rejected response
        return new BasicDelimiterResponse("", RequestStatus.REJECTED, "Empty implementation - not yet functional");
    }

    @Override
    public JobStatusResponse checkJobCompletion(JobStatusRequest request) {
        // Empty implementation - returns JOB_NOT_FOUND status
        // In the future, this will delegate to computeEngine.checkJobCompletion(request)
        return new BasicJobStatusResponse(CompletionStatus.JOB_NOT_FOUND, 
                                         "Empty implementation - not yet functional", 
                                         0, 
                                         RequestStatus.REJECTED);
    }
}