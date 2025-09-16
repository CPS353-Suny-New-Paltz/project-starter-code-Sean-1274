package project.networkapi;

public interface InputResponse {
    RequestStatus getStatus();   // ACCEPTED or REJECTED
    String getMessage();
}
