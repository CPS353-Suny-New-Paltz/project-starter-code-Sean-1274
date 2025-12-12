package project.networkapi;

public class BasicAsyncJobResponse implements AsyncJobResponse {
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
