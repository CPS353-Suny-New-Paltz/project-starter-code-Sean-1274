package project.conceptualapi;

import project.annotations.ConceptualAPI;

/**
 * Conceptual API between the job I/O handler and the computation engine.
 */
@ConceptualAPI
public interface ComputeEngineAPI {
    ComputationResponse compute(ComputationRequest request);
}
