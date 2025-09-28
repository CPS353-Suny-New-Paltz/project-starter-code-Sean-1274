
import project.networkapi.InputRequest;
import java.util.List;
import java.util.ArrayList;

/**
 * Test-only in-memory implementation of InputRequest
 * Contains a List<Integer> for integration testing
 */
public class MemoryInputConfig implements InputRequest {
// Stores the input numbers that will be processed (e.g., [1, 10, 25])
    private final List<Integer> inputData;
    private final String sourceName;
/**
 * Main constructor that creates a MemoryInputConfig with specific data and name
 * @param inputData the list of integers to use as input
 * @param sourceName the name identifying this input source
 */
    public MemoryInputConfig(List<Integer> inputData, String sourceName) {
        this.inputData = new ArrayList<>(inputData); // Defensive copy
        this.sourceName = sourceName;
    }

 /**
  * Convenience constructor that uses a default source name
  * @param inputData the list of integers to use as input
  */
    public MemoryInputConfig(List<Integer> inputData) {
        this(inputData, "memory_input");
    }
 /**
  * Implementation of the InputRequest interface method
  * @return the source name/identifier for this input
  */
    @Override
    public String getSource() {
        return sourceName;
    }


    public List<Integer> getInputData() {
        return new ArrayList<>(inputData); // Return copy to prevent modification
    }
}
