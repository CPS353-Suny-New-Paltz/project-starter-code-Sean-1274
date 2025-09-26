package project.networkapi;


// Simple implementation of OutputRequest interface
public class BasicOutputRequest implements OutputRequest {
    private final String destination;  // URI/path to output destination (file path, database table, etc.)


    public BasicOutputRequest(String destination) {
        this.destination = destination;
    }

    @Override
    public String getDestination() {
        return destination;
    }
}