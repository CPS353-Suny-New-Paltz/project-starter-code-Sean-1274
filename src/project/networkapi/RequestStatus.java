package project.networkapi;

public enum RequestStatus {

    ACCEPTED,  // The request was successfully processed and configuration was applied
    REJECTED   // The request was invalid or could not be processed (invalid parameters, permissions, etc.)
}

