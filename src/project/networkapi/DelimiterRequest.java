package project.networkapi;

public interface DelimiterRequest {
    String getDelimiters();
    DelimiterMode getMode(); // DEFAULT or CUSTOM
}
