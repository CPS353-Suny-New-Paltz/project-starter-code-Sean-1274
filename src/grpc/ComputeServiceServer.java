package grpc;

import proto.BasicResponse;
import proto.ComputeServiceGrpc;
import proto.DelimiterConfigRequest;
import proto.InputSourceRequest;
import proto.JobStatusRequest;
import proto.OutputDestinationRequest;
import proto.StartRequest;
import project.networkapi.BasicDelimiterRequest;
import project.networkapi.BasicInputRequest;
import project.networkapi.BasicJobStatusRequest;
import project.networkapi.BasicOutputRequest;
import project.networkapi.DelimiterMode;
import project.conceptualapi.EmptyComputeEngineAPI;
import project.networkapi.AsyncUserComputeAPI;
import proto.AsyncStartRequest;
import proto.AsyncJobResponse;
import proto.JobResultResponse;
import proto.JobListResponse;
import proto.ListJobsRequest;
import proto.JobSummary;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class ComputeServiceServer extends ComputeServiceGrpc.ComputeServiceImplBase {
	private final AsyncUserComputeAPI userComputeAPI;

	public ComputeServiceServer(AsyncUserComputeAPI userComputeAPI) {
		this.userComputeAPI = userComputeAPI;
	}

	@Override
	public void setInputSource(InputSourceRequest request, 
			StreamObserver<BasicResponse> responseObserver) {
		try {
			project.networkapi.InputRequest javaRequest = new BasicInputRequest(request.getSource());
			project.networkapi.InputResponse javaResponse = userComputeAPI.setInputSource(javaRequest);

			BasicResponse grpcResponse = BasicResponse.newBuilder()
					.setStatus(javaResponse.getStatus().toString())
					.setMessage(javaResponse.getMessage())
					.build();

			responseObserver.onNext(grpcResponse);
			responseObserver.onCompleted();
		} catch (Exception e) {
			responseObserver.onError(e);
		}
	}

	@Override
	public void setOutputDestination(OutputDestinationRequest request,
			StreamObserver<BasicResponse> responseObserver) {
		try {
			project.networkapi.OutputRequest javaRequest = new BasicOutputRequest(request.getDestination());
			project.networkapi.OutputResponse javaResponse = userComputeAPI.setOutputDestination(javaRequest);

			BasicResponse grpcResponse = BasicResponse.newBuilder()
					.setStatus(javaResponse.getStatus().toString())
					.setMessage(javaResponse.getMessage())
					.build();

			responseObserver.onNext(grpcResponse);
			responseObserver.onCompleted();
		} catch (Exception e) {
			responseObserver.onError(e);
		}
	}

	@Override
	public void configureDelimiters(DelimiterConfigRequest request,
			StreamObserver<proto.DelimiterResponse> responseObserver) {
		try {
			DelimiterMode mode = DelimiterMode.valueOf(request.getMode());
			project.networkapi.DelimiterRequest javaRequest = new BasicDelimiterRequest(request.getDelimiters(), mode);
			project.networkapi.DelimiterResponse javaResponse = userComputeAPI.configureDelimiters(javaRequest);

			proto.DelimiterResponse grpcResponse = proto.DelimiterResponse.newBuilder()
					.setAppliedDelimiters(javaResponse.getAppliedDelimiters())
					.setStatus(javaResponse.getStatus().toString())
					.setMessage(javaResponse.getMessage())
					.build();

			responseObserver.onNext(grpcResponse);
			responseObserver.onCompleted();
		} catch (Exception e) {
			responseObserver.onError(e);
		}
	}

	@Override
	public void startComputation(StartRequest request,
			StreamObserver<proto.JobStatusResponse> responseObserver) {
		try {
			project.networkapi.JobStatusResponse javaResponse = userComputeAPI.startComputation();

			proto.JobStatusResponse grpcResponse = proto.JobStatusResponse.newBuilder()
					.setCompletionStatus(javaResponse.getStatus().toString())
					.setMessage(javaResponse.getMessage())
					.setProgress(javaResponse.getProgress())
					.setRequestStatus(javaResponse.getRequestStatus().toString())
					.build();

			responseObserver.onNext(grpcResponse);
			responseObserver.onCompleted();
		} catch (Exception e) {
			responseObserver.onError(e);
		}
	}

	@Override
	public void checkJobCompletion(JobStatusRequest request,
			StreamObserver<proto.JobStatusResponse> responseObserver) {
		try {
			project.networkapi.JobStatusRequest javaRequest = new BasicJobStatusRequest(request.getJobIdentifier());
			project.networkapi.JobStatusResponse javaResponse = userComputeAPI.checkJobCompletion(javaRequest);

			proto.JobStatusResponse grpcResponse = proto.JobStatusResponse.newBuilder()
					.setCompletionStatus(javaResponse.getStatus().toString())
					.setMessage(javaResponse.getMessage())
					.setProgress(javaResponse.getProgress())
					.setRequestStatus(javaResponse.getRequestStatus().toString())
					.build();

			responseObserver.onNext(grpcResponse);
			responseObserver.onCompleted();
		} catch (Exception e) {
			responseObserver.onError(e);
		}
	}

	  // NEW: Async job submission
    @Override
    public void submitComputationAsync(AsyncStartRequest request,
            StreamObserver<AsyncJobResponse> responseObserver) {
        try {
            project.networkapi.AsyncJobResponse javaResponse = userComputeAPI.submitComputationAsync();
            
            AsyncJobResponse grpcResponse = AsyncJobResponse.newBuilder()
                    .setJobId(javaResponse.getJobId())
                    .setMessage(javaResponse.getMessage())
                    .setStatus(javaResponse.getStatus().toString())
                    .build();
            
            responseObserver.onNext(grpcResponse);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    // NEW: Get job result
    @Override
    public void getJobResult(proto.JobStatusRequest request,
            StreamObserver<JobResultResponse> responseObserver) {
        try {
            project.networkapi.JobStatusRequest javaRequest = new BasicJobStatusRequest(request.getJobIdentifier());
            project.networkapi.JobResultResponse javaResponse = userComputeAPI.getJobResult(javaRequest);
            
            JobResultResponse grpcResponse = JobResultResponse.newBuilder()
                    .setResultData(javaResponse.getResultData() != null ? javaResponse.getResultData() : "")
                    .setMessage(javaResponse.getMessage())
                    .setStatus(javaResponse.getStatus().toString())
                    .build();
            
            responseObserver.onNext(grpcResponse);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    // NEW: Cancel job
    @Override
    public void cancelJob(proto.JobStatusRequest request,
            StreamObserver<proto.JobStatusResponse> responseObserver) {
        try {
            project.networkapi.JobStatusRequest javaRequest = new BasicJobStatusRequest(request.getJobIdentifier());
            project.networkapi.JobStatusResponse javaResponse = userComputeAPI.cancelJob(javaRequest);
            
            proto.JobStatusResponse grpcResponse = proto.JobStatusResponse.newBuilder()
                    .setCompletionStatus(javaResponse.getStatus().toString())
                    .setMessage(javaResponse.getMessage())
                    .setProgress(javaResponse.getProgress())
                    .setRequestStatus(javaResponse.getRequestStatus().toString())
                    .build();
            
            responseObserver.onNext(grpcResponse);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    // NEW: List all jobs
    @Override
    public void listJobs(ListJobsRequest request,
            StreamObserver<JobListResponse> responseObserver) {
        try {
            project.networkapi.JobListResponse javaResponse = userComputeAPI.listJobs();
            
            // Convert Java JobSummary to proto JobSummary
            JobListResponse.Builder builder = JobListResponse.newBuilder();
            for (project.networkapi.JobSummary javaSummary : javaResponse.getJobs()) {
                JobSummary protoSummary = JobSummary.newBuilder()
                        .setJobId(javaSummary.getJobId())
                        .setStatus(javaSummary.getStatus().toString())
                        .setProgress(javaSummary.getProgress())
                        .setOutputDestination(javaSummary.getOutputDestination())
                        .setMessage(javaSummary.getMessage())
                        .build();
                builder.addJobs(protoSummary);
            }
            
            JobListResponse grpcResponse = builder
                    .setMessage(javaResponse.getMessage())
                    .setStatus(javaResponse.getStatus().toString())
                    .build();
            
            responseObserver.onNext(grpcResponse);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    public static void main(String[] args) throws Exception {
        // Create existing components
        EmptyComputeEngineAPI computeEngine = new EmptyComputeEngineAPI();
        GrpcDataStoreAPI dataStore = new GrpcDataStoreAPI("localhost", 50052);
        
        try {
            // Use AsyncUserComputeAPI instead of EmptyUserComputeAPI
            AsyncUserComputeAPI userComputeAPI = new AsyncUserComputeAPI(computeEngine, dataStore);
            
            int port = 50051;
            Server server = ServerBuilder.forPort(port)
                    .addService(new ComputeServiceServer(userComputeAPI))
                    .build()
                    .start();
            
            System.out.println("Compute Service Server started on port " + port);
            
            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down async user compute API...");
                userComputeAPI.shutdown();
            }));
            
            server.awaitTermination();
        } finally {
            // Always shut down the dataStore connection
            dataStore.shutdown();
        }
    }

}