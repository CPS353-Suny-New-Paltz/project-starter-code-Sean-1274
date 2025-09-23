package project.networkapi;
import project.annotations.NetworkAPI;


    // Defines delimiter characters for data parsing (comma, tab, custom characters)
    // Mode parameter determines whether to use system defaults or custom delimiters
    // Returns response with actually applied delimiters and configuration status
@NetworkAPI  
public interface UserComputeAPI {

    // Configures the data input source (file, database, stream, etc.)
    // Returns response containing acceptance status and any relevant messages
    InputResponse setInputSource(InputRequest request);

    // Configures the data output destination for computation results
    // Returns response indicating if output configuration was successful
    OutputResponse setOutputDestination(OutputRequest request);

    DelimiterResponse configureDelimiters(DelimiterRequest request);

    
    // NEW: Job completion check with proper response object
    JobStatusResponse checkJobCompletion(JobStatusRequest request);

}