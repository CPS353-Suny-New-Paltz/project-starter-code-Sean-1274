package project.conceptualapi;

import project.annotations.ConceptualAPIPrototype;

/**
 * Prototype implementation of the Conceptual API.
 */

public interface ComputeEngineAPIPrototype {
	@ConceptualAPIPrototype
    ComputationResponse runPrototype(ComputationRequest request);
}
