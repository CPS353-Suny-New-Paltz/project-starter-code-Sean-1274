import project.datastoreapi.DataStoreAPI;
import project.datastoreapi.DataReadRequest;
import project.datastoreapi.DataReadResponse;
import project.datastoreapi.DataWriteRequest;
import project.datastoreapi.DataWriteResponse;
import project.datastoreapi.DataStreamRequest;
import project.datastoreapi.DataStreamResponse;
import project.datastoreapi.RequestStatus;
import project.datastoreapi.DataStreamMode;

import java.util.List;
import java.util.ArrayList;

/**
 * Test-only in-memory implementation of DataStoreAPI
 * Works with MemoryInputConfig and MemoryOutputConfig for integration testing
 */
public class MemoryDataStore implements DataStoreAPI {
    private List<Integer> currentInputData;
    private List<String> currentOutputData;

    // no ComputeEngine dependency for integration tests
    public MemoryDataStore() {
        this.currentInputData = new ArrayList<>();
        this.currentOutputData = new ArrayList<>();
    }
    
	@Override
	public DataReadResponse readData(DataReadRequest request) {
		// Check if this is our special test input (MemoryInputConfig)
		if (request instanceof MemoryInputConfig) {
			MemoryInputConfig memoryInput = (MemoryInputConfig) request;  // Cast to test type
			this.currentInputData = memoryInput.getInputData();           // Store the input numbers

			// Convert List<Integer> to int[] because that's what the response expects
			int[] dataArray = currentInputData.stream().mapToInt(i -> i).toArray();

			// Return success response with the data
			return new project.datastoreapi.BasicDataReadResponse(
					RequestStatus.ACCEPTED, 
					"Successfully read " + currentInputData.size() + " integers from memory",
					dataArray
					);
		}

		// If it's not our test type, return error
		return new project.datastoreapi.BasicDataReadResponse(
				RequestStatus.REJECTED,
				"MemoryDataStore only supports MemoryInputConfig",
				new int[0]  // Empty array as data
				);
	}
	@Override
	public DataWriteResponse writeData(DataWriteRequest request) {
		// Check if this is our special test output (MemoryOutputConfig)
		if (request instanceof MemoryOutputConfig) {
			MemoryOutputConfig memoryOutput = (MemoryOutputConfig) request;  // Cast to test type
			this.currentOutputData = memoryOutput.getOutputData();           // Store reference to output list

			// Return success (in real implementation, this would write actual data)
			return new project.datastoreapi.BasicDataWriteResponse(
					RequestStatus.ACCEPTED,
					"Output configured for memory storage with capacity: " + currentOutputData.size()
					);
		}

		// If it's not our test type, return error
		return new project.datastoreapi.BasicDataWriteResponse(
				RequestStatus.REJECTED,
				"MemoryDataStore only supports MemoryOutputConfig"
				);
	}
	@Override
	public DataStreamResponse configureStream(DataStreamRequest request) {
		// Simple implementation that just echoes back the request settings
		return new project.datastoreapi.BasicDataStreamResponse(
				RequestStatus.ACCEPTED,
				"Stream configured for in-memory testing",
				DataStreamMode.BATCH,      // Use batch mode for testing
				request.getBufferSize()    // Use whatever buffer size was requested
				);
	}

	// Let tests see what input data is currently stored
	public List<Integer> getCurrentInputData() {
		return new ArrayList<>(currentInputData);  // Return copy to prevent modification
	}

	// Let tests see what output data has been written  
	public List<String> getCurrentOutputData() {
		return new ArrayList<>(currentOutputData); // Return copy to prevent modification
	}

	// Let tests pre-set output data if needed
	public void setOutputData(List<String> outputData) {
		this.currentOutputData = new ArrayList<>(outputData);  // Store a copy
	}
}