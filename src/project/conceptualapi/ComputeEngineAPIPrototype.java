package project.conceptualapi;

import project.annotations.ConceptualAPIPrototype;

/**

 * Prototype class for testing ConceptualAPI.
 */
public class ComputeEngineAPIPrototype {

    /**
     * Prototype method demonstrating API usage with a sample request.
     */
    @ConceptualAPIPrototype
    public void runPrototype(ComputeEngineAPI api) {
        // Create a sample request
        ComputationRequest request = new BasicComputationRequest(5, ComputationMode.FACTORIAL);

        // Call the API
        ComputationResponse response = api.compute(request);

        // Display result
        System.out.println("Prototype Response: " + response.getResult());
    }
