package grpc;

import project.networkapi.BasicAsyncJobResponse;
import project.networkapi.BasicDelimiterResponse;
import project.networkapi.BasicInputResponse;
import project.networkapi.BasicJobListResponse;
import project.networkapi.BasicJobResultResponse;
import project.networkapi.BasicJobStatusResponse;
import project.networkapi.BasicOutputResponse;
import project.networkapi.CompletionStatus;
import project.networkapi.DelimiterRequest;
import project.networkapi.DelimiterResponse;
import project.networkapi.InputRequest;
import project.networkapi.InputResponse;
import project.networkapi.JobStatusRequest;
import project.networkapi.JobStatusResponse;
import project.networkapi.OutputRequest;
import project.networkapi.OutputResponse;
import project.networkapi.UserComputeAPI;

import proto.AsyncStartRequest;
import proto.AsyncJobResponse;
import proto.JobResultResponse;
import proto.JobListResponse;
import proto.ListJobsRequest;

import proto.BasicResponse;
import proto.ComputeServiceGrpc;
import proto.DelimiterConfigRequest;
import proto.InputSourceRequest;
import proto.OutputDestinationRequest;
import proto.StartRequest;

import java.util.ArrayList;
import java.util.List;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcUserComputeAPI implements UserComputeAPI {
	private final ManagedChannel channel;
	private final ComputeServiceGrpc.ComputeServiceBlockingStub blockingStub;

	public GrpcUserComputeAPI(String host, int port) {
		this.channel = ManagedChannelBuilder.forAddress(host, port)
				.usePlaintext()
				.build();
		this.blockingStub = ComputeServiceGrpc.newBlockingStub(channel);
	}

	// Add a shutdown method
	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
	}

	@Override
	public InputResponse setInputSource(InputRequest request) {
		InputSourceRequest grpcRequest = InputSourceRequest.newBuilder()
				.setSource(request.getSource())
				.build();

		BasicResponse grpcResponse = blockingStub.setInputSource(grpcRequest);

		project.networkapi.RequestStatus status = project.networkapi.RequestStatus.valueOf(grpcResponse.getStatus());
		return new BasicInputResponse(status, grpcResponse.getMessage());
	}

	@Override
	public OutputResponse setOutputDestination(OutputRequest request) {
		OutputDestinationRequest grpcRequest = OutputDestinationRequest.newBuilder()
				.setDestination(request.getDestination())
				.build();

		BasicResponse grpcResponse = blockingStub.setOutputDestination(grpcRequest);

		project.networkapi.RequestStatus status = project.networkapi.RequestStatus.valueOf(grpcResponse.getStatus());
		return new BasicOutputResponse(status, grpcResponse.getMessage());
	}

	@Override
	public DelimiterResponse configureDelimiters(DelimiterRequest request) {
		DelimiterConfigRequest grpcRequest = DelimiterConfigRequest.newBuilder()
				.setDelimiters(request.getDelimiters())
				.setMode(request.getMode().toString())
				.build();

		proto.DelimiterResponse grpcResponse = blockingStub.configureDelimiters(grpcRequest);

		project.networkapi.RequestStatus status = project.networkapi.RequestStatus.valueOf(grpcResponse.getStatus());
		return new BasicDelimiterResponse(grpcResponse.getAppliedDelimiters(), status, grpcResponse.getMessage());
	}

	@Override
	public JobStatusResponse checkJobCompletion(JobStatusRequest request) {
		proto.JobStatusRequest grpcRequest = proto.JobStatusRequest.newBuilder()
				.setJobIdentifier(request.getJobIdentifier())
				.build();

		proto.JobStatusResponse grpcResponse = blockingStub.checkJobCompletion(grpcRequest);

		CompletionStatus completionStatus = CompletionStatus.valueOf(grpcResponse.getCompletionStatus());
		project.networkapi.RequestStatus requestStatus = project.networkapi.RequestStatus.valueOf(grpcResponse.getRequestStatus());
		return new BasicJobStatusResponse(completionStatus, grpcResponse.getMessage(), 
				grpcResponse.getProgress(), requestStatus);
	}

	@Override
	public JobStatusResponse startComputation() {
		StartRequest grpcRequest = StartRequest.newBuilder().build();

		proto.JobStatusResponse grpcResponse = blockingStub.startComputation(grpcRequest);

		CompletionStatus completionStatus = CompletionStatus.valueOf(grpcResponse.getCompletionStatus());
		project.networkapi.RequestStatus requestStatus = project.networkapi.RequestStatus.valueOf(grpcResponse.getRequestStatus());
		return new BasicJobStatusResponse(completionStatus, grpcResponse.getMessage(), 
				grpcResponse.getProgress(), requestStatus);
	}

	// NEW: Add async methods (these won't be in the UserComputeAPI interface)
	public project.networkapi.AsyncJobResponse submitComputationAsync() {
		AsyncStartRequest grpcRequest = AsyncStartRequest.newBuilder().build();

		AsyncJobResponse grpcResponse = blockingStub.submitComputationAsync(grpcRequest);

		project.networkapi.RequestStatus status = project.networkapi.RequestStatus.valueOf(grpcResponse.getStatus());
		return new BasicAsyncJobResponse(status, grpcResponse.getJobId(), grpcResponse.getMessage());
	}

	public project.networkapi.JobResultResponse getJobResult(project.networkapi.JobStatusRequest request) {
		proto.JobStatusRequest grpcRequest = proto.JobStatusRequest.newBuilder()
				.setJobIdentifier(request.getJobIdentifier())
				.build();

		JobResultResponse grpcResponse = blockingStub.getJobResult(grpcRequest);

		project.networkapi.RequestStatus status = project.networkapi.RequestStatus.valueOf(grpcResponse.getStatus());
		return new BasicJobResultResponse(status, grpcResponse.getResultData(), grpcResponse.getMessage());
	}

	public project.networkapi.JobStatusResponse cancelJob(project.networkapi.JobStatusRequest request) {
		proto.JobStatusRequest grpcRequest = proto.JobStatusRequest.newBuilder()
				.setJobIdentifier(request.getJobIdentifier())
				.build();

		proto.JobStatusResponse grpcResponse = blockingStub.cancelJob(grpcRequest);

		CompletionStatus completionStatus = CompletionStatus.valueOf(grpcResponse.getCompletionStatus());
		project.networkapi.RequestStatus requestStatus = project.networkapi.RequestStatus.valueOf(grpcResponse.getRequestStatus());
		return new BasicJobStatusResponse(completionStatus, grpcResponse.getMessage(), 
				grpcResponse.getProgress(), requestStatus);
	}

	public project.networkapi.JobListResponse listJobs() {
		ListJobsRequest grpcRequest = ListJobsRequest.newBuilder()
				.setIncludeCompleted(true)
				.setIncludeFailed(true)
				.setIncludeRunning(true)
				.build();

		JobListResponse grpcResponse = blockingStub.listJobs(grpcRequest);

		project.networkapi.RequestStatus status = project.networkapi.RequestStatus.valueOf(grpcResponse.getStatus());

		// Convert proto JobSummary to Java JobSummary
		List<project.networkapi.JobSummary> summaries = new ArrayList<>();
		for (proto.JobSummary protoSummary : grpcResponse.getJobsList()) {
			project.networkapi.JobSummary javaSummary = new project.networkapi.JobSummary(
					protoSummary.getJobId(),
					CompletionStatus.valueOf(protoSummary.getStatus()),
					protoSummary.getProgress(),
					protoSummary.getOutputDestination(),
					protoSummary.getMessage()
					);
			summaries.add(javaSummary);
		}

		return new BasicJobListResponse(status, summaries, grpcResponse.getMessage());
	}
}
