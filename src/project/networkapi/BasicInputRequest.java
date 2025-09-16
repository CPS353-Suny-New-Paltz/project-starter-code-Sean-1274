package project.networkapi;

public class BasicInputRequest implements InputRequest {
    private final String source;

    public BasicInputRequest(String source) {
        this.source = source;
    }

    @Override
    public String getSource() {
        return source;
    }
}



