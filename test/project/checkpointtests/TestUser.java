package project.checkpointtests;

import java.io.File;

// Import your actual NetworkAPI implementation
import project.networkapi.UserComputeAPI;
import project.networkapi.BasicInputRequest;
import project.networkapi.BasicOutputRequest;
import project.networkapi.BasicDelimiterRequest;
import project.networkapi.DelimiterMode;

public class TestUser {
	
	// TODO 3: change the type of this variable to the name you're using for your
	// @NetworkAPI interface; also update the parameter passed to the constructor
	private final UserComputeAPI coordinator;

	public TestUser(UserComputeAPI coordinator) {
		this.coordinator = coordinator;
	}

	public void run(String outputPath) {
		char delimiter = ';';
		String inputPath = "test" + File.separatorChar + "testInputFile.test";
		
		// TODO 4: Call the appropriate method(s) on the coordinator to get it to 
		// run the compute job specified by inputPath, outputPath, and delimiter
		
		try {
			// Configure input source
			coordinator.setInputSource(new BasicInputRequest(inputPath));
			
			// Configure output destination  
			coordinator.setOutputDestination(new BasicOutputRequest(outputPath));
			
			// Configure delimiters (semicolon as specified in the test)
			coordinator.configureDelimiters(new BasicDelimiterRequest(String.valueOf(delimiter), DelimiterMode.CUSTOM));
			
			// Start the computation
			coordinator.startComputation();
			
		} catch (Exception e) {
			System.err.println("Error during computation: " + e.getMessage());
			e.printStackTrace();
		}
	}

}