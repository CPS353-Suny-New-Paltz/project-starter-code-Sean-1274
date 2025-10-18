import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

// Specific imports instead of .*
import project.datastoreapi.DataStoreAPI;
import project.datastoreapi.EmptyDataStoreAPI;
import project.datastoreapi.DataReadRequest;
import project.datastoreapi.DataReadResponse;
import project.datastoreapi.DataWriteRequest;
import project.datastoreapi.DataWriteResponse;
import project.datastoreapi.DataStreamRequest;
import project.datastoreapi.DataStreamResponse;
import project.datastoreapi.RequestStatus;
import project.datastoreapi.DataFormat;
import project.datastoreapi.DataStreamMode;
import project.conceptualapi.ComputeEngineAPI;

class TestDataStoreAPI {

    @Mock
    private ComputeEngineAPI mockComputeEngine;

    private EmptyDataStoreAPI dataStoreAPI;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataStoreAPI = new EmptyDataStoreAPI(mockComputeEngine);
    }

    @Test
    void testReadDataWithIntegerArrayFormat() {
        // Arrange - Test reading integer array data
        DataReadRequest mockRequest = mock(DataReadRequest.class);
        when(mockRequest.getSource()).thenReturn("test_data_array.txt");
        when(mockRequest.getFormat()).thenReturn(DataFormat.INTEGER_ARRAY);

        // Act
        DataReadResponse response = dataStoreAPI.readData(mockRequest);

        // Assert - Check for specific read operation results
        assertNotNull(response, "Response should not be null");
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(), 
                    "Valid read request should be accepted");
        assertNotNull(response.getData(), "Data array should not be null");
        assertTrue(response.getData().length > 0, 
                  "Should read actual data (not empty array)");
        assertTrue(response.getMessage().toLowerCase().contains("read") ||
                  response.getMessage().toLowerCase().contains("loaded"),
                 "Message should confirm data was read");
    }

    @Test
    void testReadDataWithSpecificContent() {
        // Arrange - Test reading specific known data content
        DataReadRequest mockRequest = mock(DataReadRequest.class);
        when(mockRequest.getSource()).thenReturn("known_data_source.txt");
        when(mockRequest.getFormat()).thenReturn(DataFormat.INTEGER_ARRAY);

        // Act
        DataReadResponse response = dataStoreAPI.readData(mockRequest);

        // Assert - Check that specific expected data is returned
        assertEquals(RequestStatus.ACCEPTED, response.getStatus());
        
        // For known test data [1, 10, 25], verify the exact array content
        int[] expectedData = {1, 10, 25};
        assertArrayEquals(expectedData, response.getData(),
                         "Should return exact known data content [1, 10, 25]");
    }

    @Test
    void testWriteDataWithConfirmation() {
        // Arrange - Test data write operation
        DataWriteRequest mockRequest = mock(DataWriteRequest.class);
        when(mockRequest.getDestination()).thenReturn("output_results.txt");
        when(mockRequest.getFormat()).thenReturn(DataFormat.INTEGER_ARRAY);

        // Act
        DataWriteResponse response = dataStoreAPI.writeData(mockRequest);

        // Assert - Check for specific write confirmation
        assertNotNull(response);
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(),
                    "Valid write request should be accepted");
        assertTrue(response.getMessage().toLowerCase().contains("written") ||
                  response.getMessage().toLowerCase().contains("saved") ||
                  response.getMessage().toLowerCase().contains("stored"),
                 "Message should confirm data was written");
    }

    @Test
    void testConfigureStreamWithBatchMode() {
        // Arrange - Test stream configuration with batch mode
        DataStreamRequest mockRequest = mock(DataStreamRequest.class);
        when(mockRequest.getMode()).thenReturn(DataStreamMode.BATCH);
        when(mockRequest.getBufferSize()).thenReturn(2048); // Specific buffer size
        when(mockRequest.getDataFormat()).thenReturn(DataFormat.INTEGER_ARRAY);

        // Act
        DataStreamResponse response = dataStoreAPI.configureStream(mockRequest);

        // Assert - Check for exact stream configuration results
        assertNotNull(response);
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(),
                    "Valid stream configuration should be accepted");
        assertEquals(DataStreamMode.BATCH, response.getAppliedMode(),
                    "Applied mode should match requested BATCH mode");
        assertEquals(2048, response.getAppliedBufferSize(),
                    "Applied buffer size should match requested 2048");
        assertTrue(response.getMessage().toLowerCase().contains("configured") ||
                  response.getMessage().toLowerCase().contains("stream"),
                 "Message should confirm stream configuration");
    }

    @Test
    void testConfigureStreamWithStreamMode() {
        // Arrange - Test stream configuration with streaming mode
        DataStreamRequest mockRequest = mock(DataStreamRequest.class);
        when(mockRequest.getMode()).thenReturn(DataStreamMode.STREAM);
        when(mockRequest.getBufferSize()).thenReturn(1024);
        when(mockRequest.getDataFormat()).thenReturn(DataFormat.TEXT);

        // Act
        DataStreamResponse response = dataStoreAPI.configureStream(mockRequest);

        // Assert - Check for streaming mode configuration
        assertEquals(RequestStatus.ACCEPTED, response.getStatus());
        assertEquals(DataStreamMode.STREAM, response.getAppliedMode(),
                    "Applied mode should match requested STREAM mode");
        assertTrue(response.getAppliedBufferSize() > 0, 
                  "Buffer size should be positive");
    }
}