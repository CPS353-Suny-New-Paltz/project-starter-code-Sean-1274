import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import project.conceptualapi.ComputeEngineAPI;
import project.conceptualapi.EmptyComputeEngineAPI;
import project.conceptualapi.ComputationRequest;
import project.conceptualapi.ComputationResponse;
import project.conceptualapi.BasicComputationRequest;
import project.conceptualapi.ComputationMode;
import project.networkapi.UserComputeAPI;
import project.datastoreapi.DataStoreAPI;

/**
 * Additional test covering edge cases and boundary conditions in ComputeEngineAPI
 * Fulfills Checkpoint 4 Requirement #5: "add (at least) one additional test based on analyzing your implementation"
 */
class TestComputeEngineEdgeCases {

    @Mock
    private UserComputeAPI mockNetworkAPI;
    
    @Mock
    private DataStoreAPI mockDataStoreAPI;

    private EmptyComputeEngineAPI computeEngineAPI;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        computeEngineAPI = new EmptyComputeEngineAPI(mockNetworkAPI, mockDataStoreAPI);
    }

    @Test
    void testComputeFactorialWithNegativeInput() {
        // Arrange - Test negative number (should be rejected)
        ComputationRequest request = new BasicComputationRequest(-5, ComputationMode.FACTORIAL);

        // Act
        ComputationResponse response = computeEngineAPI.compute(request);

        // Assert - Should return error for negative input
        assertTrue(response.getResult().contains("Error"),
                  "Negative input should return error message");
        assertTrue(response.getResult().contains("negative"),
                  "Error message should mention negative numbers");
    }

    @Test
    void testComputeFactorialWithVeryLargeInput() {
        // Arrange - Test input beyond the 1000 limit
        ComputationRequest request = new BasicComputationRequest(1001, ComputationMode.FACTORIAL);

        // Act
        ComputationResponse response = computeEngineAPI.compute(request);

        // Assert - Should return error for very large input
        assertTrue(response.getResult().contains("Error"),
                  "Very large input should return error message");
        assertTrue(response.getResult().contains("too large"),
                  "Error message should indicate input is too large");
    }

    @Test
    void testComputeFactorialWithBoundaryInput() {
        // Arrange - Test the exact boundary (1000 should work, 1001 should fail)
        ComputationRequest boundaryRequest = new BasicComputationRequest(1000, ComputationMode.FACTORIAL);
        ComputationRequest overflowRequest = new BasicComputationRequest(1001, ComputationMode.FACTORIAL);

        // Act
        ComputationResponse boundaryResponse = computeEngineAPI.compute(boundaryRequest);
        ComputationResponse overflowResponse = computeEngineAPI.compute(overflowRequest);

        // Assert - Boundary should work, overflow should fail
        assertTrue(!boundaryResponse.getResult().contains("Error"),
                  "Input of 1000 should be accepted (boundary case)");
        assertTrue(overflowResponse.getResult().contains("Error"),
                  "Input of 1001 should be rejected");
    }

    @Test
    void testComputeWithNullRequest() {
        // Arrange - Test null request handling
        ComputationRequest nullRequest = null;

        // Act
        ComputationResponse response = computeEngineAPI.compute(nullRequest);

        // Assert - Should handle null gracefully
        assertTrue(response.getResult().contains("Error"),
                  "Null request should return error message");
        assertTrue(response.getResult().contains("null"),
                  "Error message should mention null request");
    }

    @Test
    void testComputeWithUnsupportedMode() {
        // Arrange - This would test if someone adds a new ComputationMode but forgets to implement it
        // Since we can't easily create an unsupported enum value, we'll test the default case logic
        // by ensuring our current modes are all handled
        
        ComputationRequest factorialRequest = new BasicComputationRequest(5, ComputationMode.FACTORIAL);
        ComputationRequest prototypeRequest = new BasicComputationRequest(5, ComputationMode.PROTOTYPE_ONLY);

        // Act
        ComputationResponse factorialResponse = computeEngineAPI.compute(factorialRequest);
        ComputationResponse prototypeResponse = computeEngineAPI.compute(prototypeRequest);

        // Assert - Both supported modes should work without falling to default case
        assertTrue(!factorialResponse.getResult().contains("Unsupported"),
                  "FACTORIAL mode should be supported");
        assertTrue(!prototypeResponse.getResult().contains("Unsupported"),
                  "PROTOTYPE_ONLY mode should be supported");
    }

    @Test
    void testComputeFactorialWithMaxIntInput() {
        // Arrange - Test with Integer.MAX_VALUE (way beyond the 1000 limit)
        ComputationRequest request = new BasicComputationRequest(Integer.MAX_VALUE, ComputationMode.FACTORIAL);

        // Act
        ComputationResponse response = computeEngineAPI.compute(request);

        // Assert - Should be rejected due to size limit
        assertTrue(response.getResult().contains("Error"),
                  "Integer.MAX_VALUE input should return error");
        assertTrue(response.getResult().contains("too large"),
                  "Error should indicate input is too large");
    }
}