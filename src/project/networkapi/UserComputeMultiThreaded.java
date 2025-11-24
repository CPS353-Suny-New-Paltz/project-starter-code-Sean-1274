package project.networkapi;

import project.conceptualapi.ComputeEngineAPI;
import project.conceptualapi.ComputationRequest;
import project.conceptualapi.BasicComputationRequest;
import project.conceptualapi.ComputationResponse;
import project.conceptualapi.ComputationMode;
import project.datastoreapi.DataStoreAPI;
import project.datastoreapi.DataReadRequest;
import project.datastoreapi.DataWriteRequest;
import project.datastoreapi.BasicDataReadRequest;
import project.datastoreapi.BasicDataWriteRequest;
import project.datastoreapi.DataFormat;
import project.datastoreapi.DataReadResponse;
import project.datastoreapi.DataWriteResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Multi-threaded implementation of UserComputeAPI that runs factorial 
 * calculations in parallel using a fixed thread pool.
 */
public class UserComputeMultiThreaded implements UserComputeAPI {

	private final ComputeEngineAPI computeEngine;
	private final DataStoreAPI dataStore;
	private final ExecutorService executor;

	// Job tracking for status monitoring
	private final Map<String, JobInfo> jobTracker;

	// Current configuration
	private String currentInputSource;
	private String currentOutputDestination;
	private String currentDelimiters = ",";
	private DelimiterMode currentDelimiterMode = DelimiterMode.DEFAULT;

	// Thread pool configuration
	private static final int MAX_THREADS = 4; // Reasonable upper bound
	private final ThreadLocal<String> threadOutputDestination = new ThreadLocal<>();

	public UserComputeMultiThreaded(ComputeEngineAPI computeEngine, DataStoreAPI dataStore) {
		this.computeEngine = computeEngine;
		this.dataStore = dataStore;
		this.jobTracker = new HashMap<>();
		// Create fixed thread pool with upper bound
		this.executor = Executors.newFixedThreadPool(MAX_THREADS);
	}

	@Override
	public InputResponse setInputSource(InputRequest request) {
		try {
			if (request == null || request.getSource() == null || request.getSource().trim().isEmpty()) {
				return new BasicInputResponse(RequestStatus.REJECTED, 
						"Input source cannot be null or empty");
			}

			String source = request.getSource().trim();

			if (source.length() > 255) {
				return new BasicInputResponse(RequestStatus.REJECTED,
						"Input source path too long");
			}

			this.currentInputSource = source;

			System.out.println("Input source configured: " + source);
			return new BasicInputResponse(RequestStatus.ACCEPTED,
					"Input source successfully configured: " + source);

		} catch (Exception e) {
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

			if (destination.length() > 255) {
				return new BasicOutputResponse(RequestStatus.REJECTED,
						"Output destination path too long");
			}
		    this.threadOutputDestination.set(destination);
			this.currentOutputDestination = destination;


			System.out.println("Output destination configured: " + destination);
			return new BasicOutputResponse(RequestStatus.ACCEPTED,
					"Output destination successfully configured: " + destination);

		} catch (Exception e) {
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
			System.err.println("Unexpected error in checkJobCompletion: " + e.getMessage());
			return new BasicJobStatusResponse(CompletionStatus.JOB_FAILED,
					"Internal error checking job status", 0, RequestStatus.REJECTED);
		}
	}

	@Override
	public JobStatusResponse startComputation() {
		String jobId = "job_" + System.currentTimeMillis();
	     String outputDestination = this.threadOutputDestination.get();
	        if (outputDestination == null) {
	            outputDestination = this.currentOutputDestination; // capture current output destination
	        }
		System.out.println("DEBUG: Starting computation with jobId: " + jobId);
		System.out.println("DEBUG: Output destination for this job: " + outputDestination);

		try {
			// Validate that input and output are configured
			if (currentInputSource == null || currentOutputDestination == null) {
				System.err.println("DEBUG: Input or output not configured");
				System.err.println("DEBUG: Input source: " + currentInputSource);
				System.err.println("DEBUG: Output destination: " + outputDestination);

				JobInfo failedJob = new JobInfo(
						CompletionStatus.JOB_FAILED,
						"Input source and output destination must be configured before starting computation",
						0, outputDestination // store specific destination
						);

				jobTracker.put(jobId, failedJob);
				return new BasicJobStatusResponse(CompletionStatus.JOB_FAILED,
						"Input source and output destination must be configured before starting computation", 
						0, RequestStatus.REJECTED);
			}

			// Store initial job info with output destination
			jobTracker.put(jobId, new JobInfo(
					CompletionStatus.JOB_RUNNING,
					"Starting multi-threaded file-based computation: " + currentInputSource + " → " + outputDestination,
					25,
					outputDestination
					));

			System.out.println("DEBUG: Reading input from: " + currentInputSource);

			// 1. Read input data from file
			DataReadRequest readRequest = new BasicDataReadRequest(currentInputSource, DataFormat.INTEGER_ARRAY);
			DataReadResponse readResponse = dataStore.readData(readRequest);

			System.out.println("DEBUG: Read response status: " + readResponse.getStatus());
			System.out.println("DEBUG: Read response message: " + readResponse.getMessage());

			if (readResponse.getStatus() != project.datastoreapi.RequestStatus.ACCEPTED) {
				throw new RuntimeException("Failed to read input data: " + readResponse.getMessage());
			}

			int[] inputData = readResponse.getData();
			System.out.println("DEBUG: Read " + inputData.length + " numbers from input file");

			if (inputData.length == 0) {
				throw new RuntimeException("No data read from input file");
			}

			// update job progress
			jobTracker.put(jobId, new JobInfo(
					CompletionStatus.JOB_RUNNING,
					"Data read successfully, starting multi-threaded computation",
					50,
					outputDestination
					));

			// 2. Process each input number through computation engine using multiple threads
			List<Callable<String>> computationTasks = new ArrayList<>();
			for (int inputValue : inputData) {
				computationTasks.add(() -> {
					System.out.println("DEBUG: Computing factorial for: " + inputValue);
					ComputationRequest compRequest = new BasicComputationRequest(inputValue, ComputationMode.FACTORIAL);
					ComputationResponse compResponse = computeEngine.compute(compRequest);
					String result = inputValue + "=" + compResponse.getResult();
					System.out.println("DEBUG: Computed result: " + result);
					return result;
				});
			}

			System.out.println("DEBUG: Executing " + computationTasks.size() + " computation tasks");

			// Execute all tasks in parallel using thread pool
			List<Future<String>> computationResults = executor.invokeAll(computationTasks);

			// Collect results
			StringBuilder results = new StringBuilder();
			for (int i = 0; i < computationResults.size(); i++) {
				String result = computationResults.get(i).get();
				results.append(result);

				// Add delimiter between results (except for the last one)
				if (i < computationResults.size() - 1) {
					results.append(currentDelimiters);
				}
			}

			System.out.println("DEBUG: Final results string: " + results.toString());
			System.out.println("DEBUG: Writing to output file: " + outputDestination);

			// Update job progress
			jobTracker.put(jobId, new JobInfo(
					CompletionStatus.JOB_RUNNING,
					"Multi-threaded computation completed, writing results",
					75,
					outputDestination
					));

			// 3. Write results to output file
			String outputData = results.toString();
			DataWriteRequest writeRequest = new BasicDataWriteRequest(outputDestination, DataFormat.TEXT, outputData);
			DataWriteResponse writeResponse = dataStore.writeData(writeRequest);

			System.out.println("DEBUG: Write response status: " + writeResponse.getStatus());
			System.out.println("DEBUG: Write response message: " + writeResponse.getMessage());

			if (writeResponse.getStatus() != project.datastoreapi.RequestStatus.ACCEPTED) {
				throw new RuntimeException("Failed to write results: " + writeResponse.getMessage());
			}

			System.out.println("DEBUG: Computation completed successfully");

			// 4. Mark job as completed
			JobInfo completedJob = new JobInfo(
					CompletionStatus.JOB_COMPLETED,
					"Multi-threaded computation completed successfully. Processed " + inputData.length + " numbers",
					100, outputDestination
					);
			jobTracker.put(jobId, completedJob);

			return new BasicJobStatusResponse(
					CompletionStatus.JOB_COMPLETED,
					"Multi-threaded computation completed successfully for job: " + jobId + "->"
							+ outputDestination, 100,
							RequestStatus.ACCEPTED
					);

		} catch (Exception e) {
			System.err.println("DEBUG: Computation failed with exception: " + e.getMessage());
			e.printStackTrace();

			JobInfo failedJob = new JobInfo(
					CompletionStatus.JOB_FAILED,
					"Computation failed: " + e.getMessage(),
					100, outputDestination
					);
			jobTracker.put(jobId, failedJob);

			return new BasicJobStatusResponse(
					CompletionStatus.JOB_FAILED,
					"Computation failed for job: " + jobId + " → " + outputDestination + ": " + e.getMessage(),
					100,
					RequestStatus.ACCEPTED
					);
		}
	}


	/**
	 * Shuts down the thread pool when finished to clean up resources
	 */
	public void shutdown() {
		executor.shutdown();
	}

	/**
	 * Inner class for tracking job information
	 */
	private static class JobInfo {
		CompletionStatus status;
		String message;
		String outputDestination; // track which output destination this job writes to
		int progress;

		JobInfo(CompletionStatus status, String message, int progress, String outputDestination) {
			this.status = status;
			this.message = message;
			this.progress = progress;
			this.outputDestination = outputDestination;
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

	public int getMaxThreads() {
		return MAX_THREADS;
	}

}