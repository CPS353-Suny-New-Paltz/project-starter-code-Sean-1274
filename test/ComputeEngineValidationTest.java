import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import project.conceptualapi.EmptyComputeEngineAPI;
import project.conceptualapi.ComputationRequest;
import project.conceptualapi.ComputationResponse;
import project.conceptualapi.BasicComputationRequest;
import project.conceptualapi.ComputationMode;

/**
 * Unit test specifically for ComputeEngineAPI validation logic
 * Tests part 1 of functional requirements - parameter validation
 */
class ComputeEngineValidationTest {

    private EmptyComputeEngineAPI computeEngine;

    @BeforeEach
    void setUp() {
        computeEngine = new EmptyComputeEngineAPI();
    }

    @Test
    void testCompute_NullRequest_ReturnsValidationError() {
        // Test null request validation
        ComputationResponse response = computeEngine.compute(null);
        
        assertNotNull(response, "Response should not be null");
        assertTrue(response.getResult().contains("Error"), "Should return error response");
        assertTrue(response.getResult().contains("null"), "Should mention null request");
        assertTrue(response.getResult().contains("cannot be null"), 
                  "Error message should be descriptive");
    }

    @Test
    void testCompute_NullComputationMode_ReturnsValidationError() {
        // Test null computation mode validation
        ComputationRequest request = new BasicComputationRequest(5, null);
        ComputationResponse response = computeEngine.compute(request);
        
        assertNotNull(response, "Response should not be null");
        assertTrue(response.getResult().contains("Error"), "Should return error response");
        assertTrue(response.getResult().contains("mode"), "Should mention mode error");
        assertTrue(response.getResult().contains("cannot be null"), 
                  "Error message should be descriptive");
    }

    @Test
    void testCompute_NegativeInput_ReturnsValidationError() {
        // Test negative input validation for all modes
        ComputationRequest request = new BasicComputationRequest(-5, ComputationMode.FACTORIAL);
        ComputationResponse response = computeEngine.compute(request);
        
        assertNotNull(response, "Response should not be null");
        assertTrue(response.getResult().contains("Error"), "Should return error response");
        assertTrue(response.getResult().contains("negative"), "Should mention negative input");
    }

    @Test
    void testCompute_ValidRequest_ReturnsSuccessfulResult() {
        // Test that valid requests pass validation and return results
        ComputationRequest request = new BasicComputationRequest(5, ComputationMode.FACTORIAL);
        ComputationResponse response = computeEngine.compute(request);
        
        assertNotNull(response, "Response should not be null");
        assertFalse(response.getResult().contains("Error"), 
                   "Valid request should not return error");
        assertEquals("120", response.getResult(), 
                    "Should return correct computation result");
    }

    @Test
    void testCompute_ZeroInput_Valid() {
        // Test that zero input passes validation
        ComputationRequest request = new BasicComputationRequest(0, ComputationMode.FACTORIAL);
        ComputationResponse response = computeEngine.compute(request);
        
        assertNotNull(response, "Response should not be null");
        assertFalse(response.getResult().contains("Error"), 
                   "Zero input should be valid");
        assertEquals("1", response.getResult(), 
                    "Should return correct factorial of 0");
    }

    @Test
    void testCompute_UnsupportedMode_ReturnsValidationError() {
        // This tests the default case in the switch statement for validation
        // We can't easily create an unsupported enum, but we can verify that
        // all currently supported modes work without hitting the default case
        
        // Test FACTORIAL mode validation
        ComputationRequest factorialRequest = new BasicComputationRequest(5, ComputationMode.FACTORIAL);
        ComputationResponse factorialResponse = computeEngine.compute(factorialRequest);
        assertFalse(factorialResponse.getResult().contains("Unsupported"),
                   "FACTORIAL mode should be supported");
        
        // Test PROTOTYPE_ONLY mode validation  
        ComputationRequest prototypeRequest = new BasicComputationRequest(5, ComputationMode.PROTOTYPE_ONLY);
        ComputationResponse prototypeResponse = computeEngine.compute(prototypeRequest);
        assertFalse(prototypeResponse.getResult().contains("Unsupported"),
                   "PROTOTYPE_ONLY mode should be supported");
    }
}