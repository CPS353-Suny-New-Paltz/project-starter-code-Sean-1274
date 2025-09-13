package project.processapi;

import project.annotations.ProcessAPIPrototype;
import java.util.List;
import java.util.ArrayList;

public class DataStorageAPIPrototype implements DataStorageAPI {
    
    @Override
    @ProcessAPIPrototype
    public DataReadResponse readData(DataReadRequest readRequest) {
        // Mock data - in real implementation, this would read from actual source
        List<Integer> mockData = List.of(5, 10, 15, 20);
        
        return new DataReadResponse(
            "Prototype: Successfully read mock data from " + readRequest.getInputSource().getLocation(),
            readRequest.getInputSource().getLocation(),
            mockData
        );
    }
    
    @Override
    @ProcessAPIPrototype
    public DataWriteResponse writeData(DataWriteRequest writeRequest) {
        // Simulate writing
        String formattedOutput = String.join(
            writeRequest.getDelimiters().getPairDelimiter(), 
            writeRequest.getResults()
        );
        
        return new DataWriteResponse(
            "Prototype: Successfully wrote mock data to " + writeRequest.getOutputSource().getLocation(),
            writeRequest.getOutputSource().getLocation(),
            formattedOutput,
            writeRequest.getResults().size()
        );
    }
}