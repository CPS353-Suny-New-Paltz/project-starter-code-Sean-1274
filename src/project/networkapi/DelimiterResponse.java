package project.networkapi;

public interface DelimiterResponse {
    String getAppliedDelimiters();
    RequestStatus getStatus();
    String getMessage();
}
