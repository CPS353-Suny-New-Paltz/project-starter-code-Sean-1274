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
        
        // Choose input type
        System.out.println("Choose input type:");
        System.out.println("1. File upload");
        System.out.println("2. Manual number input");
        System.out.print("Enter choice (1 or 2): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        String inputSource;
        if (choice == 1) {
            // File upload
            System.out.print("Enter input file path: ");
            inputSource = scanner.nextLine();
        } else {
            // Manual number input
            System.out.print("Enter numbers separated by spaces: ");
            String numbers = scanner.nextLine();
            // Create a temporary file with the numbers
            inputSource = createTempFileWithNumbers(numbers);
        }
        
        // Set input source
        api.setInputSource(new BasicInputRequest(inputSource));
        
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
        System.out.println("Result: " + response.getMessage());
        
        scanner.close();
    }
    
    private static String createTempFileWithNumbers(String numbers) {
        try {
            // Create a temporary file
            java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("compute_input", ".txt");
            
            // Split numbers by spaces and write one per line
            String[] numberArray = numbers.split("\\s+");
            java.util.List<String> lines = new java.util.ArrayList<>();
            for (String number : numberArray) {
                lines.add(number.trim());
            }
            
            // Write to temporary file
            java.nio.file.Files.write(tempFile, lines);
            return tempFile.toString();
            
        } catch (Exception e) {
            System.err.println("Error creating temporary file: " + e.getMessage());
            // Fallback - use a default file name
            return "manual_input.txt";
        }
    }
}