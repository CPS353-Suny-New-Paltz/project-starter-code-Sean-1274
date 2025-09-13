package project.processapi;

/**
 * Represents a generalized destination for output data for the data storage system.
 * This is a separate copy from the networkapi package to avoid cross-package dependencies.
 */
public interface OutputSource {
    String getLocation();
}