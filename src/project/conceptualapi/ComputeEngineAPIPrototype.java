package project.conceptualapi;
import project.annotations.ConceptualAPIPrototype;

/** Prototype for testing ComputeEngineAPI */
public class ComputeEngineAPIPrototype {

    /** 
     * Test method demonstrating API usage from Computation Component perspective
     */
    @ConceptualAPIPrototype
    public void runPrototype(ComputeEngineAPI api) {
        // Initialize computation
        JobConfig config = new BasicJobConfig("input.txt", "output.txt", "mode=fast", 100);
        JobInitResponse initResponse = api.initializeComputation(config);
        
        System.out.println("Computation initialized: " + initResponse.getMessage());
        
        // Process data until complete
        while (true) {
            DataResponse dataResponse = api.readInputData();
            if (dataResponse == null || dataResponse.getAvailability() == DataAvailability.NO_MORE_DATA) break;
            
            if (dataResponse.getAvailability() == DataAvailability.AVAILABLE) {
                // Process the data
                int[] processedResults = processData(dataResponse.getData());
                
                // Create processed data container
                ProcessedData processedData = new ProcessedData() {
                    @Override
                    public int[] getResults() { return processedResults; }
                    
                    @Override
                    public int getDataUnitIndex() { return dataResponse.getCurrentUnitIndex(); }
                    
                    @Override
                    public String getProcessingMetadata() { return "processed_unit_" + dataResponse.getCurrentUnitIndex(); }
                };
                
                // Write results
                WriteResponse writeResponse = api.writeProcessedResults(processedData);
                System.out.println("Write operation: " + writeResponse.getMessage() + " - Status: " + writeResponse.getStatus());
            }
            
            // Check status
            JobStatusResponse status = api.getComputationStatus();
            System.out.println("Progress: " + status.getProgressPercentage() + "% - Status: " + status.getComputationStatus());
        }
        
        // Finalize computation
        CompletionResponse completion = api.finalizeComputation();
        System.out.println("Computation completed: " + completion.getMessage() + " - Outcome: " + completion.getOutcome());
    }
    
    /** Simulate data processing */
    private int[] processData(int[] data) {
        int[] results = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            results[i] = data[i] * 2; // Simple transformation
        }
        return results;
    }
}