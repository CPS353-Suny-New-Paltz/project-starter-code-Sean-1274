package project.networkapi;

import project.annotations.NetworkAPIPrototype;


// Prototype class demonstrating how to use the UserComputeAPI
public class UserComputeAPIPrototype {

    @NetworkAPIPrototype 
    public void runPrototype(UserComputeAPI api) {
        // Create example configuration requests with default values

        InputRequest inputReq = new BasicInputRequest("default_input.txt");
        OutputRequest outputReq = new BasicOutputRequest("default_output.txt");
        DelimiterRequest delimReq = (DelimiterRequest) new BasicDelimiterRequest(",", DelimiterMode.DEFAULT);


        // Execute API calls to configure the computation environment
     
        InputResponse inRes = api.setInputSource(inputReq);
        OutputResponse outRes = api.setOutputDestination(outputReq);
        DelimiterResponse delimRes = api.configureDelimiters(delimReq);


        // Display results to verify configuration was successful
        System.out.println("Input Response: " + inRes.getStatus() + " - " + inRes.getMessage());
        System.out.println("Output Response: " + outRes.getStatus() + " - " + outRes.getMessage());
        System.out.println("Delimiter Response: " + delimRes.getStatus() 
                           + " - " + delimRes.getAppliedDelimiters());
    }
}
