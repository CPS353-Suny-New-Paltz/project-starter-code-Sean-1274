package project.networkapi;

public interface InputResponse {

    RequestStatus getStatus();   // Returns whether input source configuration was ACCEPTED or REJECTED
    String getMessage();         // Contains detailed message (error information or success confirmation)
}

