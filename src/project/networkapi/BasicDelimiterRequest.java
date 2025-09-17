package project.networkapi;

// Concrete implementation of DelimiterRequest for basic delimiter configuration
public class BasicDelimiterRequest implements DelimiterRequest {
    private final String delimiters;  // String containing delimiter characters (e.g., ",", "\t", ";")
    private final DelimiterMode mode; // Specifies whether to use DEFAULT system delimiters or CUSTOM user-provided ones


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