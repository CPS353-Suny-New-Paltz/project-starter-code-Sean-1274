package project.checkpointtests;


// Your actual implementations
import project.conceptualapi.EmptyComputeEngineAPI;
import project.datastoreapi.EmptyDataStoreAPI;
import project.networkapi.EmptyUserComputeAPI;
import project.networkapi.JobStatusResponse;
import project.networkapi.BasicInputRequest;
import project.networkapi.BasicOutputRequest;
import project.networkapi.BasicDelimiterRequest;
import project.networkapi.DelimiterMode;

public class ManualTestingFramework {
    
    public static final String INPUT = "manualTestInput.txt";
    public static final String OUTPUT = "manualTestOutput.txt";

    public static void main(String[] args) {
        // TODO 1: Instantiate real implementations of all 3 APIs
       
        EmptyDataStoreAPI dataStore = new EmptyDataStoreAPI();
        
        // Create ComputeEngineAPI 
        EmptyComputeEngineAPI computeEngine = new EmptyComputeEngineAPI();
        
        // Create UserComputeAPI with the ComputeEngineAPI and DataStoreAPI
        EmptyUserComputeAPI userComputeAPI = new EmptyUserComputeAPI(computeEngine, dataStore);

        // TODO 2: Run a computation with input file and output file, delimiter ','
        try {
            // Configure input source
            userComputeAPI.setInputSource(new BasicInputRequest(INPUT));
            
            // Configure output destination  
            userComputeAPI.setOutputDestination(new BasicOutputRequest(OUTPUT));
            
            // Configure delimiters (comma as specified)
            userComputeAPI.configureDelimiters(new BasicDelimiterRequest(",", DelimiterMode.CUSTOM));
            
            // Start the computation
            JobStatusResponse result = userComputeAPI.startComputation();
            
            System.out.println("Computation completed! Status: " + result.getStatus());
            
        } catch (Exception e) {
            System.err.println("Error during computation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}