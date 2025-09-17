package project.conceptualapi;

/**
 * Concrete implementation of ComputationResponse.
 */
public class BasicComputationResponse implements ComputationResponse {
    private final String result;

    public BasicComputationResponse(String result) {
        this.result = result;
    }

    @Override
    public String getResult() {
        return result;
    }
}
