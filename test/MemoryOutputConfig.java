

import project.networkapi.OutputRequest;
import java.util.List;
import java.util.ArrayList;

/**
 * Test-only in-memory implementation of OutputRequest
 * Contains a writable List<String> for integration testing
 */
public class MemoryOutputConfig implements OutputRequest {
    // Stores the output results that will be written (e.g., ["1", "120", "3628800"])
    // This is a direct reference (not a copy) because we want tests to modify it
    private final List<String> outputData;
    private final String destinationName;
/**
 * Main constructor that creates a MemoryOutputConfig with specific data and name
 * @param outputData the list that will store output results (should be writable)
 * @param destinationName the name identifying this output destination
 */
    public MemoryOutputConfig(List<String> outputData, String destinationName) {
        this.outputData = outputData; // Direct reference - meant to be written to
        this.destinationName = destinationName;
    }
 /**
  * Convenience constructor that creates an empty output list with default name
  */
    public MemoryOutputConfig() {
        this(new ArrayList<>(), "memory_output");
    }

 // Implementation of the OutputRequest interface method
    @Override
    public String getDestination() {
        return destinationName;
    }
    
 /**
  * Test-specific helper method (NOT part of the OutputRequest interface)
  * Allows tests to access the output data list directly
  * @return the actual output data list (not a copy) so results can be added to it
  */
    public List<String> getOutputData() {
        return outputData;
    }
 /**
  * Test-specific helper method to clear all output data
  * Useful for resetting between test runs
  */
    public void clearOutput() {
        outputData.clear();
    }
}
