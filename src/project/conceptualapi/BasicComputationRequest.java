package project.conceptualapi;

/**
 * Concrete implementation of ComputationRequest.
 */
public class BasicComputationRequest implements ComputationRequest {
    private final int input;
    private final ComputationMode mode;

    public BasicComputationRequest(int input, ComputationMode mode) {
        this.input = input;
        this.mode = mode;
    }

    @Override
    public int getInput() {
        return input;
    }

    public ComputationMode getMode() {
        return mode;
    }
}
