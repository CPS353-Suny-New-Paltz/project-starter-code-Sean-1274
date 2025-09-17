package project.networkapi;

// Simple implementation of InputRequest interface
public class BasicInputRequest implements InputRequest {
    private final String source;  // URI/path to input source (file path, database connection string, URL, etc.)

    public BasicInputRequest(String source) {
        this.source = source;
    }

    @Override
    public String getSource() {
        return source;
    }
}
