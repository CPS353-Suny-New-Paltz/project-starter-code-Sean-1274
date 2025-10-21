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
        // Arrange - Test reading from a file that doesn't exist (should handle gracefully)
        DataReadRequest mockRequest = mock(DataReadRequest.class);
        when(mockRequest.getSource()).thenReturn("test_data_array.txt");
        when(mockRequest.getFormat()).thenReturn(DataFormat.INTEGER_ARRAY);

        // Act
        DataReadResponse response = dataStoreAPI.readData(mockRequest);

        // Assert - Check that it handles missing file properly
        assertNotNull(response, "Response should not be null");
        
        // Since the file doesn't exist, it should return REJECTED status
        if (response.getStatus() == RequestStatus.REJECTED) {
            assertTrue(response.getMessage().contains("File not found") ||
                      response.getMessage().contains("No valid integer data found"),
                     "Should indicate file not found or no data");
            assertEquals(0, response.getData().length,
                       "Should return empty array for missing file");
        } else {
            // If file somehow exists, verify the response structure
            assertNotNull(response.getData());
            assertTrue(response.getMessage().contains("read") ||
                      response.getMessage().contains("Successfully"),
                     "Message should confirm data was read");
        }
    }

    @Test
    void testReadDataWithSpecificContent() {
        // Arrange - Create a temporary test file with known content
        String testFileName = "test_known_data.txt";
        
        // Create the test file
        createTestFileWithContent(testFileName, "1, 10, 25");
        
        DataReadRequest mockRequest = mock(DataReadRequest.class);
        when(mockRequest.getSource()).thenReturn(testFileName);
        when(mockRequest.getFormat()).thenReturn(DataFormat.INTEGER_ARRAY);

        // Act
        DataReadResponse response = dataStoreAPI.readData(mockRequest);

        // Assert - Check that specific expected data is returned
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(),
                    "Should successfully read from existing file");
        
        // Verify the exact array content [1, 10, 25]
        int[] expectedData = {1, 10, 25};
        assertArrayEquals(expectedData, response.getData(),
                         "Should return exact known data content [1, 10, 25]");
        assertTrue(response.getMessage().contains("Successfully read 3 integers"),
                 "Message should confirm data was read");

        // Clean up
        deleteTestFile(testFileName);
    }

    @Test
    void testReadDataWithNonExistentFile() {
        // Arrange - Test with a file that definitely doesn't exist
        DataReadRequest mockRequest = mock(DataReadRequest.class);
        when(mockRequest.getSource()).thenReturn("non_existent_file_12345.txt");
        when(mockRequest.getFormat()).thenReturn(DataFormat.INTEGER_ARRAY);

        // Act
        DataReadResponse response = dataStoreAPI.readData(mockRequest);

        // Assert - Should handle missing file gracefully
        assertEquals(RequestStatus.REJECTED, response.getStatus(),
                    "Non-existent file should return REJECTED status");
        assertTrue(response.getMessage().contains("File not found") ||
                  response.getMessage().contains("No valid integer data found"),
                 "Should indicate file not found");
        assertEquals(0, response.getData().length,
                   "Should return empty array for missing file");
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
        assertTrue(response.getMessage().contains("Write configuration accepted"),
                 "Message should confirm write configuration was accepted");
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
        assertTrue(response.getMessage().contains("Stream configuration applied"),
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
        assertEquals(1024, response.getAppliedBufferSize(),
                    "Buffer size should match requested value");
        assertTrue(response.getMessage().contains("Stream configuration applied"),
                 "Message should confirm configuration");
    }

    @Test
    void testConfigureStreamWithInvalidBufferSize() {
        // Arrange - Test with invalid buffer size
        DataStreamRequest mockRequest = mock(DataStreamRequest.class);
        when(mockRequest.getMode()).thenReturn(DataStreamMode.BATCH);
        when(mockRequest.getBufferSize()).thenReturn(0); // Invalid
        when(mockRequest.getDataFormat()).thenReturn(DataFormat.INTEGER_ARRAY);

        // Act
        DataStreamResponse response = dataStoreAPI.configureStream(mockRequest);

        // Assert - Should reject invalid buffer size
        assertEquals(RequestStatus.REJECTED, response.getStatus(),
                    "Invalid buffer size should be rejected");
        assertTrue(response.getMessage().contains("Buffer size must be positive"),
                 "Should indicate the specific error");
    }

    // Helper methods to create and delete test files
    private void createTestFileWithContent(String fileName, String content) {
        try {
            java.io.FileWriter writer = new java.io.FileWriter(fileName);
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            System.err.println("Failed to create test file: " + e.getMessage());
        }
    }

    private void deleteTestFile(String fileName) {
        try {
            java.io.File file = new java.io.File(fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            System.err.println("Failed to delete test file: " + e.getMessage());
        }
    }
}