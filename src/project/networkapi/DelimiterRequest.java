package project.networkapi;

public interface DelimiterRequest {

    String getDelimiters();          // Returns the delimiter characters requested by the user
    DelimiterMode getMode();         // Returns whether DEFAULT (system) or CUSTOM (user-defined) mode is requested
}

