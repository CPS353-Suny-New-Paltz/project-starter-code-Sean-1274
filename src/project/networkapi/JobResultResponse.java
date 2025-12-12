package project.networkapi;

public interface JobResultResponse {
		RequestStatus getStatus();
		String getResultData();
		String getMessage();
	}