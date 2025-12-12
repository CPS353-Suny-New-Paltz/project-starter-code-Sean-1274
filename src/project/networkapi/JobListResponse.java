package project.networkapi;

import java.util.List;

public interface JobListResponse {
	RequestStatus getStatus();
	List<JobSummary> getJobs();
	String getMessage();
}
