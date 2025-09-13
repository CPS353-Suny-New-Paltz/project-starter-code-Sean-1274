package project.processapi;

/**
 * Represents a generalized source of input data for the data storage system.
 * This is a separate copy from the networkapi package to avoid cross-package dependencies.
 */
public interface InputSource {
    String getLocation();
}