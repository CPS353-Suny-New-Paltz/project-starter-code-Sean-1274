package project.checkpointtests;


// Your actual implementations
import project.conceptualapi.EmptyComputeEngineAPI;
import project.datastoreapi.EmptyDataStoreAPI;
import project.networkapi.EmptyUserComputeAPI;
import project.networkapi.BasicInputRequest;
import project.networkapi.BasicOutputRequest;
import project.networkapi.BasicDelimiterRequest;
import project.networkapi.DelimiterMode;

public class ManualTestingFramework {
    
    public static final String INPUT = "manualTestInput.txt";
    public static final String OUTPUT = "manualTestOutput.txt";

    public static void main(String[] args) {
        // TODO 1: Instantiate real implementations of all 3 APIs
        // Create DataStoreAPI first (it's the dependency for ComputeEngineAPI)
        EmptyDataStoreAPI dataStore = new EmptyDataStoreAPI(null);
        
        // Create ComputeEngineAPI with the DataStoreAPI
        EmptyComputeEngineAPI computeEngine = new EmptyComputeEngineAPI(null, dataStore);
        
        // Create UserComputeAPI with the ComputeEngineAPI
        EmptyUserComputeAPI userComputeAPI = new EmptyUserComputeAPI(computeEngine);

        // TODO 2: Run a computation with input file and output file, delimiter ','
        try {
            // Configure input source
            userComputeAPI.setInputSource(new BasicInputRequest(INPUT));
            
            // Configure output destination  
            userComputeAPI.setOutputDestination(new BasicOutputRequest(OUTPUT));
            
            // Configure delimiters (comma as specified)
            userComputeAPI.configureDelimiters(new BasicDelimiterRequest(",", DelimiterMode.CUSTOM));
            
            // Start the computation
            userComputeAPI.startComputation();
            
            System.out.println("Computation completed successfully!");
            System.out.println("Input: " + INPUT);
            System.out.println("Output: " + OUTPUT);
            System.out.println("Delimiter: ','");
            
        } catch (Exception e) {
            System.err.println("Error during computation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}