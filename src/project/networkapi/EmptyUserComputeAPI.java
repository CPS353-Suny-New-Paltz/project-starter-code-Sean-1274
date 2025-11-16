package project.networkapi;

import project.conceptualapi.ComputeEngineAPI;
import project.datastoreapi.DataStoreAPI;
import project.datastoreapi.DataReadRequest;
import project.datastoreapi.DataWriteRequest;
import project.datastoreapi.BasicDataReadRequest;
import project.datastoreapi.BasicDataWriteRequest;
import project.datastoreapi.DataFormat;
import project.datastoreapi.DataReadResponse;
import project.datastoreapi.DataWriteResponse;
import project.conceptualapi.ComputationRequest;
import project.conceptualapi.BasicComputationRequest;
import project.conceptualapi.ComputationResponse;
import project.conceptualapi.ComputationMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Complete implementation of UserComputeAPI that coordinates user requests
 * and communicates ONLY with the ComputeEngineAPI.
 * The ComputeEngineAPI will handle DataStoreAPI communication.
 */
public class EmptyUserComputeAPI implements UserComputeAPI {

    private final ComputeEngineAPI computeEngine;
    private final DataStoreAPI dataStore;
    
    // Job tracking for status monitoring
    private final Map<String, JobInfo> jobTracker;
    
    // Current configuration
    private String currentInputSource;
    private String currentOutputDestination;
    private String currentDelimiters = ",";
    private DelimiterMode currentDelimiterMode = DelimiterMode.DEFAULT;
    
    public EmptyUserComputeAPI(ComputeEngineAPI computeEngine, DataStoreAPI dataStore) {
        this.computeEngine = computeEngine;
        this.dataStore = dataStore;
        this.jobTracker = new HashMap<>();
    }

    @Override
    public InputResponse setInputSource(InputRequest request) {
        try {
            if (request == null || request.getSource() == null || request.getSource().trim().isEmpty()) {
                return new BasicInputResponse(RequestStatus.REJECTED, 
                    "Input source cannot be null or empty");
            }

            String source = request.getSource().trim();
            
            // Additional validation for unexpected issues
            if (source.length() > 255) {
                return new BasicInputResponse(RequestStatus.REJECTED,
                    "Input source path too long");
            }
            
            this.currentInputSource = source;
            
            System.out.println("Input source configured: " + source);
            return new BasicInputResponse(RequestStatus.ACCEPTED,
                "Input source successfully configured: " + source);

        } catch (Exception e) {
            // Catch any unexpected runtime exceptions
            System.err.println("Unexpected error in setInputSource: " + e.getMessage());
            return new BasicInputResponse(RequestStatus.REJECTED,
                "Internal error configuring input source");
        }
    }

    @Override
    public OutputResponse setOutputDestination(OutputRequest request) {
        try {
            if (request == null || request.getDestination() == null || request.getDestination().trim().isEmpty()) {
                return new BasicOutputResponse(RequestStatus.REJECTED,
                    "Output destination cannot be null or empty");
            }

            String destination = request.getDestination().trim();
            
            // Additional validation for unexpected issues
            if (destination.length() > 255) {
                return new BasicOutputResponse(RequestStatus.REJECTED,
                    "Output destination path too long");
            }
            
            this.currentOutputDestination = destination;
            
            System.out.println("Output destination configured: " + destination);
            return new BasicOutputResponse(RequestStatus.ACCEPTED,
                "Output destination successfully configured: " + destination);

        } catch (Exception e) {
            // Catch any unexpected runtime exceptions
            System.err.println("Unexpected error in setOutputDestination: " + e.getMessage());
            return new BasicOutputResponse(RequestStatus.REJECTED,
                "Internal error configuring output destination");
        }
    }

    @Override
    public DelimiterResponse configureDelimiters(DelimiterRequest request) {
        try {
            if (request == null) {
                return new BasicDelimiterResponse("", RequestStatus.REJECTED,
                    "Delimiter request cannot be null");
            }

            DelimiterMode mode = request.getMode();
            String delimiters = request.getDelimiters();
            
            String appliedDelimiters;
            String message;
            
            switch (mode) {
                case DEFAULT:
                    appliedDelimiters = ",";
                    message = "Default delimiters applied: " + appliedDelimiters;
                    break;
                    
                case CUSTOM:
                    if (delimiters == null || delimiters.trim().isEmpty()) {
                        return new BasicDelimiterResponse("", RequestStatus.REJECTED,
                            "Custom delimiters cannot be null or empty");
                    }
                    appliedDelimiters = delimiters.trim();
                    
                    // Validate custom delimiters length
                    if (appliedDelimiters.length() > 10) {
                        return new BasicDelimiterResponse("", RequestStatus.REJECTED,
                            "Custom delimiters too long");
                    }
                    message = "Custom delimiters applied: " + appliedDelimiters;
                    break;
                    
                default:
                    return new BasicDelimiterResponse("", RequestStatus.REJECTED,
                        "Unknown delimiter mode: " + mode);
            }
            
            this.currentDelimiters = appliedDelimiters;
            this.currentDelimiterMode = mode;
            
            System.out.println("Delimiters configured: " + message);
            return new BasicDelimiterResponse(appliedDelimiters, RequestStatus.ACCEPTED, message);

        } catch (Exception e) {
            // Catch any unexpected runtime exceptions
            System.err.println("Unexpected error in configureDelimiters: " + e.getMessage());
            return new BasicDelimiterResponse("", RequestStatus.REJECTED,
                "Internal error configuring delimiters");
        }
    }

    @Override
    public JobStatusResponse checkJobCompletion(JobStatusRequest request) {
        try {
            if (request == null || request.getJobIdentifier() == null || 
                request.getJobIdentifier().trim().isEmpty()) {
                return new BasicJobStatusResponse(CompletionStatus.JOB_NOT_FOUND,
                    "Job identifier cannot be null or empty", 0, RequestStatus.REJECTED);
            }

            String jobId = request.getJobIdentifier().trim();
            JobInfo jobInfo = jobTracker.get(jobId);
            
            if (jobInfo == null) {
                return new BasicJobStatusResponse(CompletionStatus.JOB_NOT_FOUND,
                    "Job not found: " + jobId, 0, RequestStatus.REJECTED);
            }

            return new BasicJobStatusResponse(
                jobInfo.status, 
                jobInfo.message, 
                jobInfo.progress, 
                RequestStatus.ACCEPTED
            );

        } catch (Exception e) {
            // Catch any unexpected runtime exceptions
            System.err.println("Unexpected error in checkJobCompletion: " + e.getMessage());
            return new BasicJobStatusResponse(CompletionStatus.JOB_FAILED,
                "Internal error checking job status", 0, RequestStatus.REJECTED);
        }
    }

    public JobStatusResponse startComputation() {
        String jobId = "job_" + System.currentTimeMillis();
        
        try {
            // Validate that input and output are configured
            if (currentInputSource == null || currentOutputDestination == null) {
                JobInfo failedJob = new JobInfo(
                    CompletionStatus.JOB_FAILED,
                    "Input source and output destination must be configured before starting computation",
                    0
                );
                jobTracker.put(jobId, failedJob);
                return new BasicJobStatusResponse(CompletionStatus.JOB_FAILED,
                    "Input source and output destination must be configured before starting computation", 
                    0, RequestStatus.REJECTED);
            }

            jobTracker.put(jobId, new JobInfo(
                CompletionStatus.JOB_RUNNING,
                "Starting file-based computation: " + currentInputSource + " → " + currentOutputDestination,
                25
            ));

            // 1. Read input data from file
            DataReadRequest readRequest = new BasicDataReadRequest(currentInputSource, DataFormat.INTEGER_ARRAY);
            DataReadResponse readResponse = dataStore.readData(readRequest);
            
            if (readResponse.getStatus() != project.datastoreapi.RequestStatus.ACCEPTED) {
                throw new RuntimeException("Failed to read input data: " + readResponse.getMessage());
            }

            jobTracker.put(jobId, new JobInfo(
                CompletionStatus.JOB_RUNNING,
                "Data read successfully, starting computation",
                50
            ));

            // 2. Process each input number through computation engine
            int[] inputData = readResponse.getData();
            StringBuilder results = new StringBuilder();
            
            for (int i = 0; i < inputData.length; i++) {
                ComputationRequest compRequest = new BasicComputationRequest(inputData[i], ComputationMode.FACTORIAL);
                ComputationResponse compResponse = computeEngine.compute(compRequest);
                
                // Format like "5=120" using the configured delimiter
                results.append(inputData[i])
                      .append("=")
                      .append(compResponse.getResult());
                
                // Add comma between results (except for the last one)
                if (i < inputData.length - 1) {
                    results.append(currentDelimiters);
                }
            }

            jobTracker.put(jobId, new JobInfo(
                CompletionStatus.JOB_RUNNING,
                "Computation completed, writing results",
                75
            ));

            // 3. Write results to output file
            String outputData = results.toString();
            DataWriteRequest writeRequest = new BasicDataWriteRequest(currentOutputDestination, DataFormat.TEXT, outputData);
            DataWriteResponse writeResponse = dataStore.writeData(writeRequest);
            
            if (writeResponse.getStatus() != project.datastoreapi.RequestStatus.ACCEPTED) {
                throw new RuntimeException("Failed to write results: " + writeResponse.getMessage());
            }

            // 4. Mark job as completed
            JobInfo completedJob = new JobInfo(
                CompletionStatus.JOB_COMPLETED,
                "Computation completed successfully. Processed " + inputData.length + " numbers",
                100
            );
            jobTracker.put(jobId, completedJob);
            
            return new BasicJobStatusResponse(
                CompletionStatus.JOB_COMPLETED,
                "Computation completed successfully for job: " + jobId,
                100,
                RequestStatus.ACCEPTED
            );
            
        } catch (RuntimeException e) {
            // Expected exceptions - preserve original message
            JobInfo failedJob = new JobInfo(
                CompletionStatus.JOB_FAILED,
                "Computation failed: " + e.getMessage(),
                100
            );
            jobTracker.put(jobId, failedJob);
            
            return new BasicJobStatusResponse(
                CompletionStatus.JOB_FAILED,
                "Computation failed: " + e.getMessage(),
                100,
                RequestStatus.ACCEPTED
            );
            
        } catch (Exception e) {
            // Unexpected exceptions - generic error message
            System.err.println("Unexpected error in startComputation: " + e.getMessage());
            JobInfo failedJob = new JobInfo(
                CompletionStatus.JOB_FAILED,
                "Internal computation error",
                100
            );
            jobTracker.put(jobId, failedJob);
            
            return new BasicJobStatusResponse(
                CompletionStatus.JOB_FAILED,
                "Internal computation error",
                100,
                RequestStatus.ACCEPTED
            );
        }
    }

//    /**
//     * Validates input source format
//     */
//    private boolean isValidSource(String source) {
//        return source != null && !source.trim().isEmpty() && source.length() <= 255;
//    }
//
//    /**
//     * Validates output destination format
//     */
//    private boolean isValidDestination(String destination) {
//        return destination != null && !destination.trim().isEmpty() && destination.length() <= 255;
//    }

    /**
     * Inner class for tracking job information
     */
    private static class JobInfo {
        CompletionStatus status;
        String message;
        int progress;
        
        JobInfo(CompletionStatus status, String message, int progress) {
            this.status = status;
            this.message = message;
            this.progress = progress;
        }
    }

    // Getters for current configuration (useful for testing)
    public String getCurrentInputSource() {
        return currentInputSource;
    }

    public String getCurrentOutputDestination() {
        return currentOutputDestination;
    }

    public String getCurrentDelimiters() {
        return currentDelimiters;
    }

    public DelimiterMode getCurrentDelimiterMode() {
        return currentDelimiterMode;
    }
}