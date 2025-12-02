package grpc;

import project.datastoreapi.DataStoreAPI;
import project.datastoreapi.DataReadRequest;
import project.datastoreapi.DataWriteRequest;
import project.datastoreapi.DataStreamRequest;
import project.datastoreapi.DataReadResponse;
import project.datastoreapi.DataWriteResponse;
import project.datastoreapi.DataStreamResponse;
import project.datastoreapi.DataFormat;
import project.datastoreapi.DataStreamMode;
import project.datastoreapi.RequestStatus;
import project.datastoreapi.BasicDataReadResponse;
import project.datastoreapi.BasicDataWriteResponse;
import project.datastoreapi.BasicDataStreamResponse;
import project.datastoreapi.BasicDataWriteRequest;
import proto.DataStoreServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcDataStoreAPI implements DataStoreAPI {
    private final ManagedChannel channel;  // Store the channel
    private final DataStoreServiceGrpc.DataStoreServiceBlockingStub blockingStub;

    public GrpcDataStoreAPI(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = DataStoreServiceGrpc.newBlockingStub(channel);
    }
    
    // Add shutdown method matching GrpcUserComputeAPI pattern
    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
    }

    @Override
    public DataReadResponse readData(DataReadRequest request) {
        proto.DataReadRequest grpcRequest = proto.DataReadRequest.newBuilder()
                .setSource(request.getSource())
                .setFormat(request.getFormat().name())  // Use .name() instead of .toString()
                .build();
        
        proto.DataReadResponse grpcResponse = blockingStub.readData(grpcRequest);
        
        RequestStatus status = RequestStatus.valueOf(grpcResponse.getStatus());
        int[] data = grpcResponse.getDataList().stream().mapToInt(i -> i).toArray();
        return new BasicDataReadResponse(status, grpcResponse.getMessage(), data);
    }

    @Override
    public DataWriteResponse writeData(DataWriteRequest request) {
        // Handle BasicDataWriteRequest specifically for the data field
        String dataToWrite = "";
        if (request instanceof BasicDataWriteRequest) {
            BasicDataWriteRequest basicRequest = (BasicDataWriteRequest) request;
            dataToWrite = basicRequest.getData();
        }
        
        proto.DataWriteRequest grpcRequest = proto.DataWriteRequest.newBuilder()
                .setDestination(request.getDestination())
                .setFormat(request.getFormat().name())  // Use .name() instead of .toString()
                .setData(dataToWrite)
                .build();
        
        proto.DataWriteResponse grpcResponse = blockingStub.writeData(grpcRequest);
        
        RequestStatus status = RequestStatus.valueOf(grpcResponse.getStatus());
        return new BasicDataWriteResponse(status, grpcResponse.getMessage());
    }

    @Override
    public DataStreamResponse configureStream(DataStreamRequest request) {
        proto.DataStreamRequest grpcRequest = proto.DataStreamRequest.newBuilder()
                .setMode(request.getMode().toString())
                .setBufferSize(request.getBufferSize())
                .build();
        
        proto.DataStreamResponse grpcResponse = blockingStub.configureStream(grpcRequest);
        
        RequestStatus status = RequestStatus.valueOf(grpcResponse.getStatus());
        DataStreamMode mode = DataStreamMode.valueOf(grpcResponse.getMode());
        return new BasicDataStreamResponse(status, grpcResponse.getMessage(), mode, grpcResponse.getBufferSize());
    }
}