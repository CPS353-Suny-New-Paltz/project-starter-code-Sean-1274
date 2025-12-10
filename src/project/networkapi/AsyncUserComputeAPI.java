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

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;

/**
 * Enhanced multi-threaded implementation with async/polling support.
 * Users can submit jobs asynchronously and poll for status/results.
 */
public class AsyncUserComputeAPI implements UserComputeAPI {

	private final ComputeEngineAPI computeEngine;
	private final DataStoreAPI dataStore;
	private final ExecutorService executor;
	private final ExecutorService asyncExecutor;

	// Enhanced job tracking with async support
	private final Map<String, JobInfo> jobTracker;

	// Current configuration
	private String currentInputSource;
	private String currentOutputDestination;
	private String currentDelimiters = ",";
	private DelimiterMode currentDelimiterMode = DelimiterMode.DEFAULT;

	// Thread pool configuration
	private static final int MAX_THREADS = 4;
	private static final int ASYNC_POOL_SIZE = 10;

	public AsyncUserComputeAPI(ComputeEngineAPI computeEngine, DataStoreAPI dataStore) {
		this.computeEngine = computeEngine;
		this.dataStore = dataStore;
		this.jobTracker = new ConcurrentHashMap<>();
		this.executor = Executors.newFixedThreadPool(MAX_THREADS);
		this.asyncExecutor = Executors.newFixedThreadPool(ASYNC_POOL_SIZE);
	}

	// Existing methods (setInputSource, setOutputDestination, configureDelimiters) remain the same...

	@Override
	public InputResponse setInputSource(InputRequest request) {
		// Keep existing implementation
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
		// Keep existing implementation
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
		// Keep existing implementation
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

	/**
	 * NEW: Asynchronous job submission
	 * Returns immediately with a job ID for polling
	 */
	public AsyncJobResponse submitComputationAsync() {
		String jobId = "async_job_" + System.currentTimeMillis() + "_" + System.nanoTime();

		try {
			// Validate configuration
			if (currentInputSource == null || currentOutputDestination == null) {
				JobInfo failedJob = new JobInfo(
						CompletionStatus.JOB_FAILED,
						"Input source and output destination must be configured",
						0,
						currentOutputDestination,
						null
						);
				jobTracker.put(jobId, failedJob);

				return new BasicAsyncJobResponse(
						RequestStatus.REJECTED,
						jobId,
						"Configuration incomplete: input source or output destination not set"
						);
			}

			// Create initial job entry
			JobInfo initialJob = new JobInfo(
					CompletionStatus.JOB_SUBMITTED,
					"Job submitted, waiting to start",
					0,
					currentOutputDestination,
					null
					);
			jobTracker.put(jobId, initialJob);

			// Submit computation task asynchronously
			CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
				try {
					// Update status to running
					jobTracker.put(jobId, new JobInfo(
							CompletionStatus.JOB_RUNNING,
							"Starting computation: " + currentInputSource + " → " + currentOutputDestination,
							25,
							currentOutputDestination,
							null
							));

					// Execute the computation (this could be the existing startComputation logic)
					executeComputation(jobId);

				} catch (Exception e) {
					System.err.println("Async computation failed: " + e.getMessage());
					jobTracker.put(jobId, new JobInfo(
							CompletionStatus.JOB_FAILED,
							"Computation failed: " + e.getMessage(),
							100,
							currentOutputDestination,
							null
							));
				}
			}, asyncExecutor);

			// Store the future for potential cancellation
			initialJob.future = future;

			System.out.println("Job submitted asynchronously: " + jobId);
			return new BasicAsyncJobResponse(
					RequestStatus.ACCEPTED,
					jobId,
					"Computation job submitted successfully. Use job ID to check status."
					);

		} catch (Exception e) {
			System.err.println("Error submitting async job: " + e.getMessage());
			return new BasicAsyncJobResponse(
					RequestStatus.REJECTED,
					jobId,
					"Failed to submit job: " + e.getMessage()
					);
		}
	}

	/**
	 * Execute the actual computation (extracted from original startComputation)
	 */
	private void executeComputation(String jobId) throws Exception {
		// 1. Read input data
		DataReadRequest readRequest = new BasicDataReadRequest(currentInputSource, DataFormat.INTEGER_ARRAY);
		DataReadResponse readResponse = dataStore.readData(readRequest);

		if (readResponse.getStatus() != project.datastoreapi.RequestStatus.ACCEPTED) {
			throw new RuntimeException("Failed to read input data: " + readResponse.getMessage());
		}

		int[] inputData = readResponse.getData();

		jobTracker.put(jobId, new JobInfo(
				CompletionStatus.JOB_RUNNING,
				"Data read successfully (" + inputData.length + " numbers), computing...",
				50,
				currentOutputDestination,
				null
				));

		// 2. Process in parallel
		List<Callable<String>> tasks = new ArrayList<>();
		for (int value : inputData) {
			tasks.add(() -> {
				ComputationRequest compRequest = new BasicComputationRequest(value, ComputationMode.FACTORIAL);
				ComputationResponse compResponse = computeEngine.compute(compRequest);
				return value + "=" + compResponse.getResult();
			});
		}

		List<Future<String>> results = executor.invokeAll(tasks);

		// 3. Collect results
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < results.size(); i++) {
			output.append(results.get(i).get());
			if (i < results.size() - 1) {
				output.append(currentDelimiters);
			}
		}

		jobTracker.put(jobId, new JobInfo(
				CompletionStatus.JOB_RUNNING,
				"Computation complete, writing results...",
				75,
				currentOutputDestination,
				output.toString()
				));

		// 4. Write results
		DataWriteRequest writeRequest = new BasicDataWriteRequest(
				currentOutputDestination, 
				DataFormat.TEXT, 
				output.toString()
				);
		DataWriteResponse writeResponse = dataStore.writeData(writeRequest);

		if (writeResponse.getStatus() != project.datastoreapi.RequestStatus.ACCEPTED) {
			throw new RuntimeException("Failed to write results: " + writeResponse.getMessage());
		}

		// 5. Mark as completed
		jobTracker.put(jobId, new JobInfo(
				CompletionStatus.JOB_COMPLETED,
				"Computation completed successfully. Processed " + inputData.length + " numbers.",
				100,
				currentOutputDestination,
				output.toString()
				));
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

	/**
	 * NEW: Get job result after completion
	 */
	public JobResultResponse getJobResult(JobStatusRequest request) {
		try {
			if (request == null || request.getJobIdentifier() == null || 
					request.getJobIdentifier().trim().isEmpty()) {
				return new BasicJobResultResponse(
						RequestStatus.REJECTED,
						null,
						"Job identifier cannot be null or empty"
						);
			}

			String jobId = request.getJobIdentifier().trim();
			JobInfo jobInfo = jobTracker.get(jobId);

			if (jobInfo == null) {
				return new BasicJobResultResponse(
						RequestStatus.REJECTED,
						null,
						"Job not found: " + jobId
						);
			}

			if (jobInfo.status != CompletionStatus.JOB_COMPLETED) {
				return new BasicJobResultResponse(
						RequestStatus.REJECTED,
						null,
						"Job is not completed. Current status: " + jobInfo.status
						);
			}

			return new BasicJobResultResponse(
					RequestStatus.ACCEPTED,
					jobInfo.resultData,
					"Job completed successfully"
					);

		} catch (Exception e) {
			System.err.println("Unexpected error in getJobResult: " + e.getMessage());
			return new BasicJobResultResponse(
					RequestStatus.REJECTED,
					null,
					"Internal error retrieving job result"
					);
		}
	}

	/**
	 * NEW: Cancel a running job
	 */
	public JobStatusResponse cancelJob(JobStatusRequest request) {
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

			if (jobInfo.status == CompletionStatus.JOB_COMPLETED || 
					jobInfo.status == CompletionStatus.JOB_FAILED) {
				return new BasicJobStatusResponse(jobInfo.status,
						"Job already in final state: " + jobInfo.status, 
						jobInfo.progress, 
						RequestStatus.ACCEPTED);
			}

			// Try to cancel if there's a future
			if (jobInfo.future != null && !jobInfo.future.isDone()) {
				jobInfo.future.cancel(true);
			}

			jobTracker.put(jobId, new JobInfo(
					CompletionStatus.JOB_CANCELLED,
					"Job cancelled by user",
					jobInfo.progress,
					jobInfo.outputDestination,
					null
					));

			return new BasicJobStatusResponse(
					CompletionStatus.JOB_CANCELLED,
					"Job cancelled successfully",
					jobInfo.progress,
					RequestStatus.ACCEPTED
					);

		} catch (Exception e) {
			System.err.println("Unexpected error in cancelJob: " + e.getMessage());
			return new BasicJobStatusResponse(CompletionStatus.JOB_FAILED,
					"Internal error cancelling job", 0, RequestStatus.REJECTED);
		}
	}

	/**
	 * NEW: List all jobs with their status
	 */
	public JobListResponse listJobs() {
		try {
			List<JobSummary> summaries = new ArrayList<>();

			for (Map.Entry<String, JobInfo> entry : jobTracker.entrySet()) {
				JobInfo info = entry.getValue();
				summaries.add(new JobSummary(
						entry.getKey(),
						info.status,
						info.progress,
						info.outputDestination,
						info.message
						));
			}

			return new BasicJobListResponse(
					RequestStatus.ACCEPTED,
					summaries,
					"Found " + summaries.size() + " job(s)"
					);

		} catch (Exception e) {
			System.err.println("Unexpected error in listJobs: " + e.getMessage());
			return new BasicJobListResponse(
					RequestStatus.REJECTED,
					new ArrayList<>(),
					"Internal error listing jobs"
					);
		}
	}

	@Override
	public JobStatusResponse startComputation() {
		// Keep original synchronous implementation for backward compatibility
		// Or you could refactor to use the async version internally
		AsyncJobResponse asyncResponse = submitComputationAsync();

		if (asyncResponse.getStatus() != RequestStatus.ACCEPTED) {
			return new BasicJobStatusResponse(
					CompletionStatus.JOB_FAILED,
					asyncResponse.getMessage(),
					0,
					RequestStatus.REJECTED
					);
		}

		// Poll until completion (simulating synchronous behavior)
		String jobId = asyncResponse.getJobId();
		while (true) {
			JobStatusResponse status = checkJobCompletion(new BasicJobStatusRequest(jobId));
			if (status.getStatus() == CompletionStatus.JOB_COMPLETED ||
					status.getStatus() == CompletionStatus.JOB_FAILED ||
					status.getStatus() == CompletionStatus.JOB_CANCELLED) {
				return status;
			}

			try {
				Thread.sleep(100); // Small delay between polls
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return new BasicJobStatusResponse(
						CompletionStatus.JOB_FAILED,
						"Computation interrupted",
						0,
						RequestStatus.REJECTED
						);
			}
		}
	}

	public void shutdown() {
		executor.shutdown();
		asyncExecutor.shutdown();
	}

	/**
	 * Enhanced JobInfo with async support
	 */
	private static class JobInfo {
		CompletionStatus status;
		String message;
		String outputDestination;
		String resultData; // Store result for retrieval
		int progress;
		CompletableFuture<Void> future; // For async cancellation

		JobInfo(CompletionStatus status, String message, int progress, 
				String outputDestination, String resultData) {
			this.status = status;
			this.message = message;
			this.progress = progress;
			this.outputDestination = outputDestination;
			this.resultData = resultData;
			this.future = null;
		}
	}

	// New response classes needed for async operations
	public interface AsyncJobResponse {
		RequestStatus getStatus();
		String getJobId();
		String getMessage();
	}

	public interface JobResultResponse {
		RequestStatus getStatus();
		String getResultData();
		String getMessage();
	}

	public interface JobListResponse {
		RequestStatus getStatus();
		List<JobSummary> getJobs();
		String getMessage();
	}

	public static class BasicAsyncJobResponse implements AsyncJobResponse {
		private final RequestStatus status;
		private final String jobId;
		private final String message;

		public BasicAsyncJobResponse(RequestStatus status, String jobId, String message) {
			this.status = status;
			this.jobId = jobId;
			this.message = message;
		}

		@Override 
		public RequestStatus getStatus() {
			return status; 
		}
		@Override 
		public String getJobId() { 
			return jobId; 
		}
		@Override 
		public String getMessage() { 
			return message;
		}
	}

	public static class BasicJobResultResponse implements JobResultResponse {
		private final RequestStatus status;
		private final String resultData;
		private final String message;

		public BasicJobResultResponse(RequestStatus status, String resultData, String message) {
			this.status = status;
			this.resultData = resultData;
			this.message = message;
		}

		@Override public RequestStatus getStatus() { 
			return status; 
		}
		@Override 
		public String getResultData() { 
			return resultData; 
		}
		@Override 
		public String getMessage() { 
			return message; 
		}
	}

	public static class BasicJobListResponse implements JobListResponse {
		private final RequestStatus status;
		private final List<JobSummary> jobs;
		private final String message;

		public BasicJobListResponse(RequestStatus status, List<JobSummary> jobs, String message) {
			this.status = status;
			this.jobs = jobs;
			this.message = message;
		}

		@Override 
		public RequestStatus getStatus() { 
			return status; 
		}
		@Override
		public List<JobSummary> getJobs() { 
			return jobs; 
		}
		@Override 
		public String getMessage() { 
			return message; 
		}
	}

	public static class JobSummary {
		private final String jobId;
		private final CompletionStatus status;
		private final int progress;
		private final String outputDestination;
		private final String message;

		public JobSummary(String jobId, CompletionStatus status, int progress, 
				String outputDestination, String message) {
			this.jobId = jobId;
			this.status = status;
			this.progress = progress;
			this.outputDestination = outputDestination;
			this.message = message;
		}

		public String getJobId() { 
			return jobId; 
		}
		public CompletionStatus getStatus() { 
			return status; 
		}
		public int getProgress() { 
			return progress; 
		}
		public String getOutputDestination() { 
			return outputDestination; 
		}
		public String getMessage() { 
			return message; 
		}
	}

	// Getters for current configuration
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