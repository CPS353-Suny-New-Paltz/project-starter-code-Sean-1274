package project.conceptualapi;

import project.networkapi.UserComputeAPI;    // For callbacks to Network API
import project.datastoreapi.DataStoreAPI;   // For data operations

// Empty implementation of ComputeEngineAPI for Checkpoint 3
// Central component that coordinates between Network API and Data Store API
public class EmptyComputeEngineAPI implements ComputeEngineAPI {

    // Bidirectional communication with both other APIs
    private final UserComputeAPI networkAPI;
    private final DataStoreAPI dataStoreAPI;

    public EmptyComputeEngineAPI(UserComputeAPI networkAPI, DataStoreAPI dataStoreAPI) {
        this.networkAPI = networkAPI;
        this.dataStoreAPI = dataStoreAPI;
    }

    @Override
    public ComputationResponse compute(ComputationRequest request) {
        // Empty implementation - returns default response
        return new BasicComputationResponse("Empty implementation - not yet functional");
    }
}