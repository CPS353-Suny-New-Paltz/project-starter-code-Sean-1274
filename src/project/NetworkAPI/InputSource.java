package project.NetworkAPI;

/**
 * Represents a generalized source of input data.
 * 
 * Examples:
 * - A local file path (e.g., "C:/data/input.txt")
 */
public interface InputSource {
	String getLocation(); // Return location as a string
}

