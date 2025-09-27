
import project.networkapi.InputRequest;
import java.util.List;
import java.util.ArrayList;

/**
 * Test-only in-memory implementation of InputRequest
 * Contains a List<Integer> for integration testing
 */
public class MemoryInputConfig implements InputRequest {
    private final List<Integer> inputData;
    private final String sourceName;

    public MemoryInputConfig(List<Integer> inputData, String sourceName) {
        this.inputData = new ArrayList<>(inputData); // Defensive copy
        this.sourceName = sourceName;
    }

    public MemoryInputConfig(List<Integer> inputData) {
        this(inputData, "memory_input");
    }

    @Override
    public String getSource() {
        return sourceName;
    }

    public List<Integer> getInputData() {
        return new ArrayList<>(inputData); // Return copy to prevent modification
    }
}