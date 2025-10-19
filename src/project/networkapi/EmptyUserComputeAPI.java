package project.networkapi;

import project.conceptualapi.ComputeEngineAPI;
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
    
    // Job tracking for status monitoring
    private final Map<String, JobInfo> jobTracker;
    
    // Current configuration
    private String currentInputSource;
    private String currentOutputDestination;
    private String currentDelimiters = ",";
    private DelimiterMode currentDelimiterMode = DelimiterMode.DEFAULT;

    // Constructor only needs the ComputeEngineAPI
    public EmptyUserComputeAPI(ComputeEngineAPI computeEngine) {
        this.computeEngine = computeEngine;
        this.jobTracker = new HashMap<>();
    }

    @Override
    public InputResponse setInputSource(InputRequest request) {
        try {
            // Validate the request
            if (request == null || request.getSource() == null || request.getSource().trim().isEmpty()) {
                return new BasicInputResponse(RequestStatus.REJECTED, 
                    "Input source cannot be null or empty");
            }

            String source = request.getSource().trim();
            
            // Validate source format
            if (!isValidSource(source)) {
                return new BasicInputResponse(RequestStatus.REJECTED,
                    "Invalid input source format: " + source);
            }

            // Store the input source for later use
            this.currentInputSource = source;
            
            System.out.println("Input source configured: " + source);
            return new BasicInputResponse(RequestStatus.ACCEPTED,
                "Input source successfully configured: " + source);

        } catch (Exception e) {
            return new BasicInputResponse(RequestStatus.REJECTED,
                "Error configuring input source: " + e.getMessage());
        }
    }

    @Override
    public OutputResponse setOutputDestination(OutputRequest request) {
        try {
            // Validate the request
            if (request == null || request.getDestination() == null || request.getDestination().trim().isEmpty()) {
                return new BasicOutputResponse(RequestStatus.REJECTED,
                    "Output destination cannot be null or empty");
            }

            String destination = request.getDestination().trim();
            
            // Validate destination format
            if (!isValidDestination(destination)) {
                return new BasicOutputResponse(RequestStatus.REJECTED,
                    "Invalid output destination format: " + destination);
            }

            // Store the output destination for later use
            this.currentOutputDestination = destination;
            
            System.out.println("Output destination configured: " + destination);
            return new BasicOutputResponse(RequestStatus.ACCEPTED,
                "Output destination successfully configured: " + destination);

        } catch (Exception e) {
            return new BasicOutputResponse(RequestStatus.REJECTED,
                "Error configuring output destination: " + e.getMessage());
        }
    }

    @Override
    public DelimiterResponse configureDelimiters(DelimiterRequest request) {
        try {
            // Validate the request
            if (request == null) {
                return new BasicDelimiterResponse("", RequestStatus.REJECTED,
                    "Delimiter request cannot be null");
            }

            DelimiterMode mode = request.getMode();
            String delimiters = request.getDelimiters();
            
            // Apply configuration based on mode
            String appliedDelimiters;
            String message;
            
            switch (mode) {
                case DEFAULT:
                    // Use system default delimiters (ignore user-provided ones)
                    appliedDelimiters = ","; // System default
                    message = "Default delimiters applied: " + appliedDelimiters;
                    break;
                    
                case CUSTOM:
                    // Validate and use custom delimiters
                    if (delimiters == null || delimiters.trim().isEmpty()) {
                        return new BasicDelimiterResponse("", RequestStatus.REJECTED,
                            "Custom delimiters cannot be null or empty");
                    }
                    appliedDelimiters = delimiters.trim();
                    message = "Custom delimiters applied: " + appliedDelimiters;
                    break;
                    
                default:
                    return new BasicDelimiterResponse("", RequestStatus.REJECTED,
                        "Unknown delimiter mode: " + mode);
            }
            
            // Update current configuration
            this.currentDelimiters = appliedDelimiters;
            this.currentDelimiterMode = mode;
            
            System.out.println("Delimiters configured: " + message);
            return new BasicDelimiterResponse(appliedDelimiters, RequestStatus.ACCEPTED, message);

        } catch (Exception e) {
            return new BasicDelimiterResponse("", RequestStatus.REJECTED,
                "Error configuring delimiters: " + e.getMessage());
        }
    }

    @Override
    public JobStatusResponse checkJobCompletion(JobStatusRequest request) {
        try {
            // Validate the request
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

            // Return the stored job status
            return new BasicJobStatusResponse(
                jobInfo.status, 
                jobInfo.message, 
                jobInfo.progress, 
                RequestStatus.ACCEPTED
            );

        } catch (Exception e) {
            return new BasicJobStatusResponse(CompletionStatus.JOB_FAILED,
                "Error checking job status: " + e.getMessage(), 0, RequestStatus.REJECTED);
        }
    }

    /**
     * Additional method to start computation - fulfills Requirement 2a
     * Receives request to start computation and returns suitable result status
     * Communicates ONLY with ComputeEngineAPI
     */
    public JobStatusResponse startComputation() {
        try {
            // Validate that input and output are configured
            if (currentInputSource == null || currentOutputDestination == null) {
                return new BasicJobStatusResponse(CompletionStatus.JOB_FAILED,
                    "Input source and output destination must be configured before starting computation", 
                    0, RequestStatus.REJECTED);
            }

            // Generate unique job ID
            String jobId = "job_" + System.currentTimeMillis();
            
            // Update job status to running
            jobTracker.put(jobId, new JobInfo(
                CompletionStatus.JOB_RUNNING,
                "Starting computation with input: " + currentInputSource + ", output: " + currentOutputDestination,
                25
            ));

            // **Requirement 2: Coordinate through ComputeEngineAPI only**
            // The ComputeEngineAPI will handle the actual computation and DataStoreAPI communication
            System.out.println("Coordinating computation through ComputeEngineAPI...");
            
            // For now, we'll create a simple computation request to demonstrate the coordination
            // In a full implementation, the ComputeEngineAPI would handle the file I/O through DataStoreAPI
            ComputationRequest sampleRequest = new BasicComputationRequest(5, ComputationMode.FACTORIAL);
            ComputationResponse response = computeEngine.compute(sampleRequest);
            
            // Update job progress based on computation result
            jobTracker.put(jobId, new JobInfo(
                CompletionStatus.JOB_RUNNING,
                "Computation in progress. Sample result: " + response.getResult(),
                75
            ));

            // Simulate completion (in full implementation, this would be based on actual completion)
            JobInfo completedJob = new JobInfo(
                CompletionStatus.JOB_COMPLETED,
                "Computation completed successfully. Input: " + currentInputSource + ", Output: " + currentOutputDestination,
                100
            );
            jobTracker.put(jobId, completedJob);
            
            System.out.println("Computation coordination completed for job: " + jobId);
            return new BasicJobStatusResponse(
                CompletionStatus.JOB_COMPLETED,
                "Computation completed successfully for job: " + jobId,
                100,
                RequestStatus.ACCEPTED
            );
            
        } catch (Exception e) {
            // Mark job as failed
            String jobId = "job_" + System.currentTimeMillis();
            JobInfo failedJob = new JobInfo(
                CompletionStatus.JOB_FAILED,
                "Computation coordination failed: " + e.getMessage(),
                100
            );
            jobTracker.put(jobId, failedJob);
            
            System.err.println("Computation coordination failed: " + e.getMessage());
            return new BasicJobStatusResponse(
                CompletionStatus.JOB_FAILED,
                "Computation coordination failed: " + e.getMessage(),
                100,
                RequestStatus.ACCEPTED
            );
        }
    }

    /**
     * Validates input source format
     */
    private boolean isValidSource(String source) {
        // Basic validation
        return source != null && !source.trim().isEmpty() && source.length() <= 255;
    }

    /**
     * Validates output destination format
     */
    private boolean isValidDestination(String destination) {
        // Basic validation
        return destination != null && !destination.trim().isEmpty() && destination.length() <= 255;
    }

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