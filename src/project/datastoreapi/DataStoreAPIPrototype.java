package project.datastoreapi;
import project.annotations.ProcessAPIPrototype;

/** Prototype class for testing DataStoreAPI */
public class DataStoreAPIPrototype {

    /** Test method demonstrating API usage with sample requests */
    @ProcessAPIPrototype
    public void runPrototype(DataStoreAPI api) {
        // Create sample requests
        DataReadRequest readReq = new BasicDataReadRequest("input_data.txt", DataFormat.INTEGER_ARRAY);
        DataWriteRequest writeReq = new BasicDataWriteRequest("output_data.txt", DataFormat.INTEGER_ARRAY, "sample output data");
        DataStreamRequest streamReq = new BasicDataStreamRequest(DataStreamMode.BATCH, 1024, DataFormat.INTEGER_ARRAY);

        // Execute API calls
        DataReadResponse readRes = api.readData(readReq);
        DataWriteResponse writeRes = api.writeData(writeReq);
        DataStreamResponse streamRes = api.configureStream(streamReq);

        // Display results
        System.out.println("Read Response: " + readRes.getStatus() + " - " + readRes.getMessage());
        System.out.println("Write Response: " + writeRes.getStatus() + " - " + writeRes.getMessage());
        System.out.println("Stream Response: " + streamRes.getStatus() + " - Mode: " + streamRes.getAppliedMode());
    }
}