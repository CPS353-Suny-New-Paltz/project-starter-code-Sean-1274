package project.networkapi;

import java.util.List;

public class BasicJobListResponse implements JobListResponse {
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
