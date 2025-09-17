package project.networkapi;
import project.annotations.NetworkAPI;

@NetworkAPI  // Indicates this interface defines a network API contract for remote invocation
public interface UserComputeAPI {

    // Configures the data input source (file, database, stream, etc.)
    // Returns response containing acceptance status and any relevant messages
    InputResponse setInputSource(InputRequest request);

    // Configures the data output destination for computation results
    // Returns response indicating if output configuration was successful
    OutputResponse setOutputDestination(OutputRequest request);

    // Defines delimiter characters for data parsing (comma, tab, custom characters)
    // Mode parameter determines whether to use system defaults or custom delimiters
    // Returns response with actually applied delimiters and configuration status
    DelimiterResponse configureDelimiters(DelimiterRequest request);
}