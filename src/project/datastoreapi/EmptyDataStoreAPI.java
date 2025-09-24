package project.datastoreapi;

import project.conceptualapi.ComputeEngineAPI; 

// Empty implementation of DataStoreAPI for Checkpoint 3
// bidirectional dependency with Compute Engine
public class EmptyDataStoreAPI implements DataStoreAPI {

    // Data Store needs to call back to Compute Engine (for notifications, progress, etc.)
    private final ComputeEngineAPI computeEngine;

    public EmptyDataStoreAPI(ComputeEngineAPI computeEngine) {
        this.computeEngine = computeEngine;
    }

    @Override
    public DataReadResponse readData(DataReadRequest request) {
        // Empty implementation - can potentially callback to computeEngine
        return new BasicDataReadResponse(
            RequestStatus.REJECTED, 
            "Empty implementation - not yet functional", 
            new int[0]
        );
    }

    @Override
    public DataWriteResponse writeData(DataWriteRequest request) {
        // Empty implementation
        return new BasicDataWriteResponse(
            RequestStatus.REJECTED, 
            "Empty implementation - not yet functional"
        );
    }

    @Override
    public DataStreamResponse configureStream(DataStreamRequest request) {
        // Empty implementation
        return new BasicDataStreamResponse(
            RequestStatus.REJECTED,
            "Empty implementation - not yet functional",
            DataStreamMode.BATCH,
            0
        );
    }
}