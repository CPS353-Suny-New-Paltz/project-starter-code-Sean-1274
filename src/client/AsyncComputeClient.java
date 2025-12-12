package client;

import grpc.GrpcUserComputeAPI;
import project.networkapi.BasicInputRequest;
import project.networkapi.BasicOutputRequest;
import project.networkapi.CompletionStatus;
import project.networkapi.BasicDelimiterRequest;
import project.networkapi.BasicJobStatusRequest;
import project.networkapi.DelimiterMode;

import java.util.Scanner;

public class AsyncComputeClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 50051;
        
        GrpcUserComputeAPI api = new GrpcUserComputeAPI(host, port);
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Async Compute Engine Client ===");
        
        try {
            // Configuration phase
            System.out.println("Choose input type:");
            System.out.println("1. File upload");
            System.out.println("2. Manual number input");
            System.out.print("Enter choice (1 or 2): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            String inputSource;
            if (choice == 1) {
                System.out.print("Enter input file path: ");
                inputSource = scanner.nextLine();
            } else {
                System.out.print("Enter numbers separated by spaces: ");
                String numbers = scanner.nextLine();
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
            
            // Async job submission
            System.out.println("\n1. Submit job asynchronously");
            System.out.println("2. Use synchronous mode");
            System.out.print("Enter choice (1 or 2): ");
            int asyncChoice = scanner.nextInt();
            scanner.nextLine();
            
            if (asyncChoice == 1) {
                // Async mode
                System.out.println("Submitting job asynchronously...");
                var asyncResponse = api.submitComputationAsync();
                String jobId = asyncResponse.getJobId();
                System.out.println("Job submitted! Job ID: " + jobId);
                
                // Polling options
                System.out.println("\nWhat would you like to do?");
                System.out.println("1. Poll for status");
                System.out.println("2. List all jobs");
                System.out.println("3. Cancel this job");
                System.out.println("4. Exit");
                
                while (true) {
                    System.out.print("\nEnter choice: ");
                    int pollChoice = scanner.nextInt();
                    scanner.nextLine();
                    
                    switch (pollChoice) {
                        case 1:
                            // Poll for status
                            var status = api.checkJobCompletion(new BasicJobStatusRequest(jobId));
                            System.out.println("Job Status: " + status.getStatus());
                            System.out.println("Progress: " + status.getProgress() + "%");
                            System.out.println("Message: " + status.getMessage());
                            
                            // Get result if completed
                            if (status.getStatus() == CompletionStatus.JOB_COMPLETED) {
                                var result = api.getJobResult(new BasicJobStatusRequest(jobId));
                                System.out.println("Result: " + result.getResultData());
                            }
                            break;
                            
                        case 2:
                            // List all jobs
                            var jobList = api.listJobs();
                            System.out.println("Total jobs: " + jobList.getJobs().size());
                            for (var job : jobList.getJobs()) {
                                System.out.println("ID: " + job.getJobId() + 
                                                 ", Status: " + job.getStatus() + 
                                                 ", Progress: " + job.getProgress() + "%");
                            }
                            break;
                            
                        case 3:
                            // Cancel job
                            var cancelResponse = api.cancelJob(new BasicJobStatusRequest(jobId));
                            System.out.println("Cancel response: " + cancelResponse.getMessage());
                            break;
                            
                        case 4:
                            System.out.println("Exiting...");
                            return;
                            
                        default:
                            System.out.println("Invalid choice");
                    }
                }
            } else {
                // Synchronous mode (original behavior)
                System.out.println("Starting computation synchronously...");
                var response = api.startComputation();
                System.out.println("Result: " + response.getMessage());
            }
            
        } finally {
            try {
                api.shutdown();
            } catch (InterruptedException e) {
                System.err.println("Error shutting down client: " + e.getMessage());
            }
            scanner.close();
        }
    }
    
    private static String createTempFileWithNumbers(String numbers) {
        try {
            java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("compute_input", ".txt");
            String[] numberArray = numbers.split("\\s+");
            java.util.List<String> lines = new java.util.ArrayList<>();
            for (String number : numberArray) {
                lines.add(number.trim());
            }
            java.nio.file.Files.write(tempFile, lines);
            return tempFile.toString();
        } catch (Exception e) {
            System.err.println("Error creating temporary file: " + e.getMessage());
            return "manual_input.txt";
        }
    }
}