package project.checkpointtests;

import java.io.File;

import project.networkapi.BasicDelimiterRequest;
import project.networkapi.BasicInputRequest;
import project.networkapi.BasicOutputRequest;
import project.networkapi.DelimiterMode;
import project.networkapi.UserComputeAPI;

public class TestUser {
    
    private final UserComputeAPI coordinator;

    public TestUser(UserComputeAPI coordinator) {
        this.coordinator = coordinator;
    }

    public void run(String outputPath) {
        char delimiter = ',';
        // FIX THE FILE PATH - add proper separator
        String inputPath = "test" + File.separator + "testInputFile.txt"; // Make sure this has the separator
        
        try {
            // Debug: print the actual path being used
            System.out.println("DEBUG TestUser: Input path = " + inputPath);
            File inputFile = new File(inputPath);
            System.out.println("DEBUG TestUser: File exists = " + inputFile.exists());
            System.out.println("DEBUG TestUser: Absolute path = " + inputFile.getAbsolutePath());
            
            // Configure input source
            coordinator.setInputSource(new BasicInputRequest(inputPath));
            
            // Configure output destination  
            coordinator.setOutputDestination(new BasicOutputRequest(outputPath));
            
            // Configure delimiters
            coordinator.configureDelimiters(new BasicDelimiterRequest(String.valueOf(delimiter), DelimiterMode.CUSTOM));
            
            // Start the computation
            coordinator.startComputation();
            
        } catch (Exception e) {
            System.err.println("Error during computation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}