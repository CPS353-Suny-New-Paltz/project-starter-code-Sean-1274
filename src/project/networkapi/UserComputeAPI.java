package project.networkapi;
import project.annotations.NetworkAPI;

@NetworkAPI
public interface UserComputeAPI {

    InputResponse setInputSource(InputRequest request);

    OutputResponse setOutputDestination(OutputRequest request);

    DelimiterResponse configureDelimiters(DelimiterRequest request);
}