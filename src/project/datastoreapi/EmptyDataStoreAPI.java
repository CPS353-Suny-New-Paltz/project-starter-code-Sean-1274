package project.datastoreapi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based implementation of DataStoreAPI that can read and write to user-specified files.
 * Matches the functionality of DataStorageAPIIm - reads one integer per line and writes formatted results.
 */
public class EmptyDataStoreAPI implements DataStoreAPI {

    public EmptyDataStoreAPI() {
        // No parameters needed for basic file operations
    }

    @Override
    public DataReadResponse readData(DataReadRequest request) {
        try {
            List<Integer> data = new ArrayList<>();
            
            if (request == null) {
                return new BasicDataReadResponse(
                    RequestStatus.REJECTED, 
                    "Request cannot be null", 
                    new int[0]
                );
            }
            
            if (request.getSource() == null || request.getSource().isEmpty()) {
                return new BasicDataReadResponse(
                    RequestStatus.REJECTED,
                    "Source cannot be null or empty", 
                    new int[0]
                );
            }
            
            // Add file extension validation
            String source = request.getSource();
            if (!source.toLowerCase().endsWith(".txt")) {
                return new BasicDataReadResponse(
                    RequestStatus.REJECTED,
                    "Only .txt files are supported", 
                    new int[0]
                );
            }
            
            // Add basic path validation
            if (source.contains("..")) {
                return new BasicDataReadResponse(
                    RequestStatus.REJECTED,
                    "Invalid file path", 
                    new int[0]
                );
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(request.getSource()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        data.add(Integer.parseInt(line));
                    }
                }
                
                // Convert List<Integer> to int[]
                int[] dataArray = data.stream().mapToInt(i -> i).toArray();
                return new BasicDataReadResponse(
                    RequestStatus.ACCEPTED,
                    "Successfully read " + dataArray.length + " integers",
                    dataArray
                );
                
            } catch (IOException e) {
                // Expected exception - file I/O issues
                return new BasicDataReadResponse(
                    RequestStatus.REJECTED,
                    "Error reading file: " + e.getMessage(), 
                    new int[0]
                );
            } catch (NumberFormatException e) {
                // Expected exception - invalid data format
                return new BasicDataReadResponse(
                    RequestStatus.REJECTED,
                    "Invalid integer format in file", 
                    new int[0]
                );
            }
        } catch (Exception e) {
            // Unexpected exceptions
            System.err.println("Unexpected error in readData: " + e.getMessage());
            return new BasicDataReadResponse(
                RequestStatus.REJECTED,
                "Internal error reading data", 
                new int[0]
            );
        }
    }

    @Override
    public DataWriteResponse writeData(DataWriteRequest request) {
        try {
            if (request == null) {
                return new BasicDataWriteResponse(
                    RequestStatus.REJECTED, 
                    "Request cannot be null"
                );
            }
            
            if (request.getDestination() == null || request.getDestination().isEmpty()) {
                return new BasicDataWriteResponse(
                    RequestStatus.REJECTED,
                    "Destination cannot be null or empty"
                );
            }

            // Check if this is a BasicDataWriteRequest with data
            if (!(request instanceof BasicDataWriteRequest)) {
                return new BasicDataWriteResponse(
                    RequestStatus.REJECTED,
                    "Only BasicDataWriteRequest with data is supported"
                );
            }

            BasicDataWriteRequest basicRequest = (BasicDataWriteRequest) request;
            String dataToWrite = basicRequest.getData();
            
            if (dataToWrite == null) {
                return new BasicDataWriteResponse(
                    RequestStatus.REJECTED,
                    "No data provided to write"
                );
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(request.getDestination()))) {
                writer.write(dataToWrite);
                return new BasicDataWriteResponse(
                    RequestStatus.ACCEPTED,
                    "Successfully wrote data to file: " + request.getDestination()
                );
            } catch (IOException e) {
                // Expected exception - file I/O issues
                return new BasicDataWriteResponse(
                    RequestStatus.REJECTED,
                    "Error writing file: " + e.getMessage()
                );
            }
        } catch (Exception e) {
            // Unexpected exceptions
            System.err.println("Unexpected error in writeData: " + e.getMessage());
            return new BasicDataWriteResponse(
                RequestStatus.REJECTED,
                "Internal error writing data"
            );
        }
    }

    @Override
    public DataStreamResponse configureStream(DataStreamRequest request) {
        try {
            // Simplified stream configuration for basic file operations
            if (request == null) {
                return new BasicDataStreamResponse(
                    RequestStatus.REJECTED,
                    "Request cannot be null", 
                    DataStreamMode.BATCH, 
                    0
                );
            }

            DataStreamMode mode = request.getMode();
            int bufferSize = request.getBufferSize();

            if (mode == null) {
                return new BasicDataStreamResponse(
                    RequestStatus.REJECTED,
                    "Stream mode cannot be null", 
                    DataStreamMode.BATCH, 
                    0
                );
            }

            if (bufferSize <= 0) {
                return new BasicDataStreamResponse(
                    RequestStatus.REJECTED,
                    "Buffer size must be positive", 
                    mode, 
                    0
                );
            }

            return new BasicDataStreamResponse(
                RequestStatus.ACCEPTED,
                "Stream configuration applied successfully", 
                mode, 
                bufferSize
            );

        } catch (Exception e) {
            // Catch any unexpected runtime exceptions
            System.err.println("Unexpected error in configureStream: " + e.getMessage());
            return new BasicDataStreamResponse(
                RequestStatus.REJECTED,
                "Internal error configuring stream", 
                DataStreamMode.BATCH, 
                0
            );
        }
    }
}