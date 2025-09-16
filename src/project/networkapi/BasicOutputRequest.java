package project.networkapi;

public class BasicOutputRequest implements OutputRequest {
    private final String destination;

    public BasicOutputRequest(String destination) {
        this.destination = destination;
    }

    @Override
    public String getDestination() {
        return destination;
    }
}