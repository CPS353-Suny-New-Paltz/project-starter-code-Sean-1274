

import project.networkapi.OutputRequest;
import java.util.List;
import java.util.ArrayList;

/**
 * Test-only in-memory implementation of OutputRequest
 * Contains a writable List<String> for integration testing
 */
public class MemoryOutputConfig implements OutputRequest {
    private final List<String> outputData;
    private final String destinationName;

    public MemoryOutputConfig(List<String> outputData, String destinationName) {
        this.outputData = outputData; // Direct reference - meant to be written to
        this.destinationName = destinationName;
    }

    public MemoryOutputConfig() {
        this(new ArrayList<>(), "memory_output");
    }

    @Override
    public String getDestination() {
        return destinationName;
    }

    public List<String> getOutputData() {
        return outputData;
    }

    public void clearOutput() {
        outputData.clear();
    }
}