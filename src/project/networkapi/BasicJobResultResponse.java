package project.networkapi;

public class BasicJobResultResponse implements JobResultResponse {
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
