package project.conceptualapi;

/** Basic implementation of job configuration */
public class BasicJobConfig implements JobConfig {
    private final String inputSource;
    private final String outputDestination;
    private final String parameters;
    private final int processingUnitSize;

    /** Create job configuration with specified parameters */
    public BasicJobConfig(String inputSource, String outputDestination, 
                         String parameters, int processingUnitSize) {
        this.inputSource = inputSource;
        this.outputDestination = outputDestination;
        this.parameters = parameters;
        this.processingUnitSize = processingUnitSize;
    }

    @Override
    public String getInputSource() {
        return inputSource;
    }

    @Override
    public String getOutputDestination() {
        return outputDestination;
    }

    @Override
    public String getParameters() {
        return parameters;
    }

    @Override
    public int getProcessingUnitSize() {
        return processingUnitSize;
    }
}