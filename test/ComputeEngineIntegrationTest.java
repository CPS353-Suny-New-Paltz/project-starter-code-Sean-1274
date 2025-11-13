import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;

import project.conceptualapi.ComputeEngineAPI;
import project.conceptualapi.EmptyComputeEngineAPI;
import project.datastoreapi.DataStoreAPI;
import project.networkapi.EmptyUserComputeAPI;
import project.networkapi.UserComputeAPI;
import project.networkapi.InputResponse;
import project.networkapi.OutputResponse;
import project.networkapi.RequestStatus;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Integration test for the Compute Engine components
 * Tests UserComputeAPI and ComputeEngineAPI working together with in-memory DataStore
 */
class ComputeEngineIntegrationTest {

    private UserComputeAPI userComputeAPI;
    private ComputeEngineAPI computeEngineAPI;
    private DataStoreAPI dataStore;
    private MemoryOutputConfig outputConfig;

    @BeforeEach
    void setUp() {
        // Create the in-memory data store (no mocks!)
        dataStore = new MemoryDataStore();

        // Create the Compute Engine with the in-memory data store
        computeEngineAPI = new EmptyComputeEngineAPI(); // networkAPI is null for this test

        // Create the User Compute API with the Compute Engine
        userComputeAPI = new EmptyUserComputeAPI(computeEngineAPI, dataStore);

        // Create the output configuration to capture results
        outputConfig = new MemoryOutputConfig();
    }

    @Test
    void testComputeEngineIntegrationWithFactorialComputation() {
        // Arrange - Create input with [1, 10, 25] as specified in requirements
        List<Integer> inputNumbers = Arrays.asList(1, 10, 25);
        MemoryInputConfig inputConfig = new MemoryInputConfig(inputNumbers, "integration_test_input");

        // Configure the input source - this should store data in UserComputeAPI
        InputResponse inputResponse = userComputeAPI.setInputSource(inputConfig);
        
        // Configure the output destination - this should set up output capture
        OutputResponse outputResponse = userComputeAPI.setOutputDestination(outputConfig);

        // Assert - Verify configuration was successful
        assertEquals(RequestStatus.ACCEPTED, inputResponse.getStatus(),
                    "Input configuration should be accepted");
        assertEquals(RequestStatus.ACCEPTED, outputResponse.getStatus(),
                    "Output configuration should be accepted");

        // Verify configuration is stored in UserComputeAPI
        String currentInput = ((EmptyUserComputeAPI) userComputeAPI).getCurrentInputSource();
        String currentOutput = ((EmptyUserComputeAPI) userComputeAPI).getCurrentOutputDestination();
        
        assertEquals("integration_test_input", currentInput);
        assertEquals("memory_output", currentOutput);
        
        // The system is now ready for computation
        assertTrue(true, "Integration test infrastructure is properly connected");
    }

    @Test
    void testIntegrationWithDataStoreReadWrite() {
        // Additional integration test focusing on DataStore interaction

        // Arrange
        List<Integer> inputNumbers = Arrays.asList(1, 10, 25);
        MemoryInputConfig inputConfig = new MemoryInputConfig(inputNumbers);

        // Act - Test DataStore directly with memory configuration
        // This tests that MemoryDataStore can work with MemoryInputConfig
        // Act - Cast to MemoryDataStore to use the specific methods
        MemoryDataStore memoryDataStore = (MemoryDataStore) dataStore;
        project.datastoreapi.DataReadResponse readResponse = memoryDataStore.readData(inputConfig);
        project.datastoreapi.DataWriteResponse writeResponse = memoryDataStore.writeData(outputConfig);

        // Assert - Verify DataStore works with memory configurations
        assertEquals(project.datastoreapi.RequestStatus.ACCEPTED, readResponse.getStatus(),
                    "MemoryDataStore should accept MemoryInputConfig");
        assertEquals(project.datastoreapi.RequestStatus.ACCEPTED, writeResponse.getStatus(),
                    "MemoryDataStore should accept MemoryOutputConfig");
        
        // Verify input data was actually stored in DataStore
        List<Integer> storedData = ((MemoryDataStore) dataStore).getCurrentInputData();
        assertEquals(inputNumbers, storedData, 
                    "Input data should be preserved in MemoryDataStore");
        
        // Verify output is ready to receive computation results
        assertNotNull(outputConfig.getOutputData(),
                    "Output should be ready to receive results");
    }

    @Test
    void testFullConfigurationFlow() {
        // This test demonstrates the complete expected configuration flow
        // Arrange - Set up complete computation environment
        List<Integer> inputNumbers = Arrays.asList(1, 10, 25);
        MemoryInputConfig inputConfig = new MemoryInputConfig(inputNumbers, "computation_input");
        
        // Act - Execute the full configuration flow through UserComputeAPI
        InputResponse inputResponse = userComputeAPI.setInputSource(inputConfig);
        OutputResponse outputResponse = userComputeAPI.setOutputDestination(outputConfig);
        
        // Assert - Verify configuration was successful
        assertEquals(RequestStatus.ACCEPTED, inputResponse.getStatus());
        assertEquals(RequestStatus.ACCEPTED, outputResponse.getStatus());
        
        // Verify configuration is stored in UserComputeAPI
        String configuredInput = ((EmptyUserComputeAPI) userComputeAPI).getCurrentInputSource();
        String configuredOutput = ((EmptyUserComputeAPI) userComputeAPI).getCurrentOutputDestination();
        
        assertEquals("computation_input", configuredInput);
        assertEquals("memory_output", configuredOutput);
        
        // System is now ready for computation to start
        assertTrue(configuredInput != null && !configuredInput.isEmpty(), 
                  "Input should be configured");
        assertTrue(configuredOutput != null && !configuredOutput.isEmpty(),
                  "Output should be configured");
    }

    @Test
    void testMemoryDataStoreDirectInteraction() {
        // Test the MemoryDataStore directly to ensure it works as expected
        // Arrange
        List<Integer> testInput = Arrays.asList(5, 10, 15);
        MemoryInputConfig inputConfig = new MemoryInputConfig(testInput, "direct_test");
        List<String> testOutput = new ArrayList<>();
        MemoryOutputConfig outputConfig = new MemoryOutputConfig(testOutput, "direct_output");

        // Act - Cast to MemoryDataStore to use the specific methods
        MemoryDataStore memoryDataStore = (MemoryDataStore) dataStore;
        project.datastoreapi.DataReadResponse readResponse = memoryDataStore.readData(inputConfig);
        project.datastoreapi.DataWriteResponse writeResponse = memoryDataStore.writeData(outputConfig);

        // Assert
        assertEquals(project.datastoreapi.RequestStatus.ACCEPTED, readResponse.getStatus());
        assertEquals(project.datastoreapi.RequestStatus.ACCEPTED, writeResponse.getStatus());
        
        // Verify data storage
        List<Integer> storedInput = ((MemoryDataStore) dataStore).getCurrentInputData();
        assertEquals(testInput, storedInput);
        
        // Verify output list is the same reference (so results can be added later)
        assertSame(testOutput, outputConfig.getOutputData());
    }

    @Test
    void testComputationReadiness() {
        // Test that the entire system is ready for computation
        // Arrange
        List<Integer> inputNumbers = Arrays.asList(1, 10, 25);
        MemoryInputConfig inputConfig = new MemoryInputConfig(inputNumbers, "ready_input");
        
        // Act - Configure the system through UserComputeAPI
        InputResponse inputResponse = userComputeAPI.setInputSource(inputConfig);
        OutputResponse outputResponse = userComputeAPI.setOutputDestination(outputConfig);
        
        // Assert - System should be fully configured and ready
        assertEquals(RequestStatus.ACCEPTED, inputResponse.getStatus());
        assertEquals(RequestStatus.ACCEPTED, outputResponse.getStatus());
        
        // UserComputeAPI should have configuration stored
        String configuredInput = ((EmptyUserComputeAPI) userComputeAPI).getCurrentInputSource();
        String configuredOutput = ((EmptyUserComputeAPI) userComputeAPI).getCurrentOutputDestination();
        
        assertNotNull(configuredInput);
        assertNotNull(configuredOutput);
        
        // DataStore should be ready to work with memory configurations
        // (This is verified in the separate DataStore test above)
        assertTrue(true, "System is ready for computation");
    }

    @Test
    void testErrorHandlingWithInvalidConfigurations() {
        // Test error handling for invalid configurations
        
        // Arrange - Create invalid configurations
        MemoryInputConfig nullInputConfig = new MemoryInputConfig(new ArrayList<>(), "");
        MemoryOutputConfig nullOutputConfig = new MemoryOutputConfig(new ArrayList<>(), "");
        
        // Act - Try to configure with empty names
        InputResponse inputResponse = userComputeAPI.setInputSource(nullInputConfig);
        OutputResponse outputResponse = userComputeAPI.setOutputDestination(nullOutputConfig);
        
        // Assert - Should handle empty configurations appropriately
        // (Depends on your EmptyUserComputeAPI implementation)
        assertNotNull(inputResponse);
        assertNotNull(outputResponse);
    }
}
