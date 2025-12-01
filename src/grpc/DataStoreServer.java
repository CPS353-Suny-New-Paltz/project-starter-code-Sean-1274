package grpc;

import proto.DataReadRequest;
import proto.DataReadResponse;
import proto.DataWriteRequest;
import proto.DataWriteResponse;
import proto.DataStreamRequest;
import proto.DataStreamResponse;
import proto.DataStoreServiceGrpc;
import project.datastoreapi.EmptyDataStoreAPI;
import project.datastoreapi.DataStoreAPI;
import project.datastoreapi.BasicDataReadRequest;
import project.datastoreapi.BasicDataWriteRequest;
import project.datastoreapi.DataFormat;
import project.datastoreapi.DataStreamMode;
import project.datastoreapi.RequestStatus;
import project.datastoreapi.BasicDataReadResponse;
import project.datastoreapi.BasicDataWriteResponse;
import project.datastoreapi.BasicDataStreamResponse;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class DataStoreServer extends DataStoreServiceGrpc.DataStoreServiceImplBase {
    private final DataStoreAPI dataStore;

    public DataStoreServer(DataStoreAPI dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void readData(DataReadRequest request, StreamObserver<DataReadResponse> responseObserver) {
        try {
            project.datastoreapi.DataReadRequest javaRequest = 
                new BasicDataReadRequest(request.getSource(), DataFormat.valueOf(request.getFormat()));
            project.datastoreapi.DataReadResponse javaResponse = dataStore.readData(javaRequest);
            
            DataReadResponse grpcResponse = DataReadResponse.newBuilder()
                .setStatus(javaResponse.getStatus().toString())
                .setMessage(javaResponse.getMessage())
                .addAllData(javaArraysToList(javaResponse.getData()))
                .build();
            
            responseObserver.onNext(grpcResponse);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void writeData(DataWriteRequest request, StreamObserver<DataWriteResponse> responseObserver) {
        try {
            project.datastoreapi.DataWriteRequest javaRequest = 
                new BasicDataWriteRequest(request.getDestination(), DataFormat.TEXT, request.getData());
            project.datastoreapi.DataWriteResponse javaResponse = dataStore.writeData(javaRequest);
            
            DataWriteResponse grpcResponse = DataWriteResponse.newBuilder()
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
    public void configureStream(DataStreamRequest request, StreamObserver<DataStreamResponse> responseObserver) {
        try {
            // Create an anonymous implementation of DataStreamRequest
            project.datastoreapi.DataStreamRequest javaRequest = 
                new project.datastoreapi.DataStreamRequest() {
                    @Override
                    public DataStreamMode getMode() {
                        return DataStreamMode.valueOf(request.getMode());
                    }
                    
                    @Override
                    public int getBufferSize() {
                        return request.getBufferSize();
                    }
                    
                    @Override
                    public DataFormat getDataFormat() {
                        return DataFormat.valueOf(request.getDataFormat());
                    }
                };
            
            project.datastoreapi.DataStreamResponse javaResponse = dataStore.configureStream(javaRequest);
            
            DataStreamResponse grpcResponse = DataStreamResponse.newBuilder()
                .setStatus(javaResponse.getStatus().toString())
                .setMessage(javaResponse.getMessage())
                .setMode(javaResponse.getAppliedMode().toString())  
                .setBufferSize(javaResponse.getAppliedBufferSize()) 
                .build();
            
            responseObserver.onNext(grpcResponse);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
    private List<Integer> javaArraysToList(int[] data) {
        List<Integer> list = new ArrayList<>();
        for (int value : data) {
            list.add(value);
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        DataStoreAPI dataStore = new EmptyDataStoreAPI();
        
        int port = 50052;
        Server server = ServerBuilder.forPort(port)
            .addService(new DataStoreServer(dataStore))
            .build()
            .start();

        System.out.println("Data Store Server started on port " + port);
        server.awaitTermination();
    }
}