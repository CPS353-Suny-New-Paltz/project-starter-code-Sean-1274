package project.networkapi;

/**
 * Represents delimiters for formatting results.
 */
public class DelimiterPair {
    private final String pairDelimiter;
    private final String resultDelimiter;

    public DelimiterPair(String pairDelimiter, String resultDelimiter) {
        this.pairDelimiter = pairDelimiter;
        this.resultDelimiter = resultDelimiter;
    }

    // Defaults if user doesn’t specify
    public static DelimiterPair defaultDelimiters() {
        return new DelimiterPair(";", ":");
    }

    public String getPairDelimiter() { return pairDelimiter; }
    public String getResultDelimiter() { return resultDelimiter; }
}
