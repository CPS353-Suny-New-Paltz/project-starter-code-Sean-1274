package project.datastoreapi;

/** Modes for data streaming */
public enum DataStreamMode {
    BATCH,      // Process data in batches (current implementation)
    STREAM      // Real-time streaming (future implementation)
}