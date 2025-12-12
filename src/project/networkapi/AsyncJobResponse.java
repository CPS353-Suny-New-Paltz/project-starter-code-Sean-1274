package project.networkapi;

public interface AsyncJobResponse {
    RequestStatus getStatus();
    String getJobId();
    String getMessage();
}