import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Specific imports instead of .*
import project.conceptualapi.ComputeEngineAPI;
import project.conceptualapi.EmptyComputeEngineAPI;
import project.conceptualapi.ComputationRequest;
import project.conceptualapi.ComputationResponse;
import project.conceptualapi.BasicComputationRequest;
import project.conceptualapi.ComputationMode;
import project.networkapi.UserComputeAPI;
import project.datastoreapi.DataStoreAPI;

class TestComputeEngineAPI {

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
    void testComputeFactorialOfFive() {
        // Arrange - Test specific factorial computation: 5! = 120
        ComputationRequest request = new BasicComputationRequest(5, ComputationMode.FACTORIAL);

        // Act
        ComputationResponse response = computeEngineAPI.compute(request);

        // Assert - Check for exact factorial result
        assertNotNull(response, "Response should not be null");
        assertEquals("120", response.getResult(),
                    "Factorial of 5 should be 120");
        assertFalse(response.getResult().contains("not yet functional"),
                   "Implementation should return actual computation result");
    }

    @Test
    void testComputeFactorialOfTen() {
        // Arrange - Test factorial of 10: 10! = 3628800
        ComputationRequest request = new BasicComputationRequest(10, ComputationMode.FACTORIAL);

        // Act
        ComputationResponse response = computeEngineAPI.compute(request);

        // Assert - Check for exact factorial result
        assertEquals("3628800", response.getResult(),
                    "Factorial of 10 should be 3628800");
    }

    @Test
    void testComputeFactorialOfZero() {
        // Arrange - Test edge case: 0! = 1
        ComputationRequest request = new BasicComputationRequest(0, ComputationMode.FACTORIAL);

        // Act
        ComputationResponse response = computeEngineAPI.compute(request);

        // Assert - Check that factorial of 0 returns 1
        assertEquals("1", response.getResult(),
                    "Factorial of 0 should be 1");
    }

    @Test
    void testComputeFactorialOfOne() {
        // Arrange - Test edge case: 1! = 1
        ComputationRequest request = new BasicComputationRequest(1, ComputationMode.FACTORIAL);

        // Act
        ComputationResponse response = computeEngineAPI.compute(request);

        // Assert - Check that factorial of 1 returns 1
        assertEquals("1", response.getResult(),
                    "Factorial of 1 should be 1");
    }

    @Test
    void testComputeWithLargeNumber() {
        // Arrange - Test factorial of 25 (very large number)
        ComputationRequest request = new BasicComputationRequest(25, ComputationMode.FACTORIAL);

        // Act
        ComputationResponse response = computeEngineAPI.compute(request);

        // Assert - Check for exact large factorial result
        assertEquals("15511210043330985984000000", response.getResult(),
                    "Factorial of 25 should be 15511210043330985984000000");
    }

    @Test
    void testComputeWithPrototypeMode() {
        // Arrange - Test PROTOTYPE_ONLY mode (should return different result format)
        ComputationRequest request = new BasicComputationRequest(5, ComputationMode.PROTOTYPE_ONLY);

        // Act
        ComputationResponse response = computeEngineAPI.compute(request);

        // Assert - Check for prototype-specific response format
        assertNotNull(response.getResult());
        assertTrue(response.getResult().toLowerCase().contains("prototype") ||
                  response.getResult().toLowerCase().contains("test"),
                 "Prototype mode should return test/prototype specific result");
    }
}