package project.networkapi;

public interface OutputResponse {
    RequestStatus getStatus();  // Indicates if output destination configuration was ACCEPTED or REJECTED
    String getMessage();        // Provides additional information (error message or success confirmation)
}