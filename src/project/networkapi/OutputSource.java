package project.networkapi;

/**
 * Represents a generic output destination.
 */
public interface OutputSource {
    String getLocation();  // e.g., file path, DB URI
}
