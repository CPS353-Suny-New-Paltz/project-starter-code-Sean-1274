package project.NetworkAPI;

/**
 * Represents a generalized destination for output data.
 * 
 * Examples:
 * - A local file path (e.g., "C:/data/output.txt")
 * - A database connection
 * - A console or API sink
 */
public interface OutputSource {
	String getLocation(); // Return location as a string
}

