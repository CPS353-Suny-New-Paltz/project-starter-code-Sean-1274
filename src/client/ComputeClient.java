package client;

import grpc.GrpcUserComputeAPI;
import project.networkapi.UserComputeAPI;
import project.networkapi.BasicInputRequest;
import project.networkapi.BasicOutputRequest;
import project.networkapi.BasicDelimiterRequest;
import project.networkapi.DelimiterMode;

import java.util.Scanner;

public class ComputeClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 50051;
        
        UserComputeAPI api = new GrpcUserComputeAPI(host, port);
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Compute Engine Client ===");
        
        // Get input configuration
        System.out.print("Enter input file path: ");
        String inputFile = scanner.nextLine();
        api.setInputSource(new BasicInputRequest(inputFile));
        
        // Get output configuration  
        System.out.print("Enter output file path: ");
        String outputFile = scanner.nextLine();
        api.setOutputDestination(new BasicOutputRequest(outputFile));
        
        // Get delimiter configuration
        System.out.print("Enter delimiter (press Enter for default ','): ");
        String delimiter = scanner.nextLine();
        if (delimiter.isEmpty()) {
            api.configureDelimiters(new BasicDelimiterRequest(",", DelimiterMode.DEFAULT));
        } else {
            api.configureDelimiters(new BasicDelimiterRequest(delimiter, DelimiterMode.CUSTOM));
        }
        
        // Start computation
        System.out.println("Starting computation...");
        var response = api.startComputation();
        System.out.println("Job started: " + response.getMessage());
        
        scanner.close();
    }
}
