package project.networkapi;

public interface DelimiterResponse {

    String getAppliedDelimiters();  // Returns the actual delimiters that were set (may differ from requested)
    RequestStatus getStatus();      // Indicates if the delimiter configuration was successful (ACCEPTED) or failed (REJECTED)
    String getMessage();            // Provides detailed message about the operation result (error details or success confirmation)

}
