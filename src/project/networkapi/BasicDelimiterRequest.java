package project.networkapi;

public class BasicDelimiterRequest implements DelimiterRequest {
    private final String delimiters;
    private final DelimiterMode mode;

    public BasicDelimiterRequest(String delimiters, DelimiterMode mode) {
        this.delimiters = delimiters;
        this.mode = mode;
    }

    @Override
    public String getDelimiters() {
        return delimiters;
    }

    @Override
    public DelimiterMode getMode() {
        return mode;
    }
}