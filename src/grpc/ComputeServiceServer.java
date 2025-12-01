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
import project.networkapi.BasicInputResponse;
import project.networkapi.BasicJobStatusRequest;
import project.networkapi.BasicJobStatusResponse;
import project.networkapi.BasicOutputRequest;
import project.networkapi.BasicOutputResponse;
import project.networkapi.DelimiterMode;
import project.networkapi.EmptyUserComputeAPI;
import project.networkapi.UserComputeAPI;
import project.conceptualapi.EmptyComputeEngineAPI;
import project.datastoreapi.EmptyDataStoreAPI;
import project.datastoreapi.DataStoreAPI;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class ComputeServiceServer extends ComputeServiceGrpc.ComputeServiceImplBase {
    private final UserComputeAPI userComputeAPI;

    public ComputeServiceServer(UserComputeAPI userComputeAPI) {
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

    public static void main(String[] args) throws Exception {
        // Create your existing components
        EmptyComputeEngineAPI computeEngine = new EmptyComputeEngineAPI();
        DataStoreAPI dataStore = new GrpcDataStoreAPI("localhost", 50052);
        UserComputeAPI userComputeAPI = new EmptyUserComputeAPI(computeEngine, dataStore);

        int port = 50051;
        Server server = ServerBuilder.forPort(port)
            .addService(new ComputeServiceServer(userComputeAPI))
            .build()
            .start();

        System.out.println("Compute Service Server started on port " + port);
        server.awaitTermination();
    }
}