package project.networkapi;

import project.annotations.NetworkAPIPrototype;

public class UserComputeAPIPrototype {

    @NetworkAPIPrototype
    public void runPrototype(UserComputeAPI api) {
        // Example request objects
        InputRequest inputReq = new BasicInputRequest("default_input.txt");
        OutputRequest outputReq = new BasicOutputRequest("default_output.txt");
        DelimiterRequest delimReq = (DelimiterRequest) new BasicDelimiterRequest(",", DelimiterMode.DEFAULT);

        // Call API methods
        InputResponse inRes = api.setInputSource(inputReq);
        OutputResponse outRes = api.setOutputDestination(outputReq);
        DelimiterResponse delimRes = api.configureDelimiters(delimReq);

        // Print simulated responses
        System.out.println("Input Response: " + inRes.getStatus() + " - " + inRes.getMessage());
        System.out.println("Output Response: " + outRes.getStatus() + " - " + outRes.getMessage());
        System.out.println("Delimiter Response: " + delimRes.getStatus() 
                           + " - " + delimRes.getAppliedDelimiters());
    }
}
