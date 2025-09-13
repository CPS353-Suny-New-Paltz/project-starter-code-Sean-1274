package project.processapi;

/**
 * Defines delimiters for formatting results for the data storage system.
 * This is a separate copy from the networkapi package.
 */
public class DelimiterPair {
    private final String pairDelimiter;
    private final String resultDelimiter;

    public DelimiterPair(String pairDelimiter, String resultDelimiter) {
        this.pairDelimiter = pairDelimiter;
        this.resultDelimiter = resultDelimiter;
    }

    public static DelimiterPair defaultDelimiters() {
        return new DelimiterPair(";", ":");
    }

    public String getPairDelimiter() {
        return pairDelimiter;
    }

    public String getResultDelimiter() {
        return resultDelimiter;
    }
}