import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import project.datastoreapi.*;
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
    void testReadData() {
        // Arrange
        DataReadRequest mockRequest = mock(DataReadRequest.class);
        when(mockRequest.getSource()).thenReturn("test_input.txt");
        when(mockRequest.getFormat()).thenReturn(DataFormat.INTEGER_ARRAY);

        // Act
        DataReadResponse response = dataStoreAPI.readData(mockRequest);

        // Assert - Should FAIL initially (expecting ACCEPTED but getting REJECTED)
        assertNotNull(response, "Response should not be null");
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(), 
                    "Implementation should return ACCEPTED for valid read request");
        assertNotNull(response.getData(), "Data array should not be null");
    }

    @Test
    void testWriteData() {
        // Arrange
        DataWriteRequest mockRequest = mock(DataWriteRequest.class);
        when(mockRequest.getDestination()).thenReturn("test_output.txt");
        when(mockRequest.getFormat()).thenReturn(DataFormat.INTEGER_ARRAY);

        // Act
        DataWriteResponse response = dataStoreAPI.writeData(mockRequest);

        // Assert - Should FAIL initially
        assertNotNull(response);
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(),
                    "Implementation should return ACCEPTED for valid write request");
    }

    @Test
    void testConfigureStream() {
        // Arrange
        DataStreamRequest mockRequest = mock(DataStreamRequest.class);
        when(mockRequest.getMode()).thenReturn(DataStreamMode.BATCH);
        when(mockRequest.getBufferSize()).thenReturn(1024);
        when(mockRequest.getDataFormat()).thenReturn(DataFormat.INTEGER_ARRAY);

        // Act
        DataStreamResponse response = dataStoreAPI.configureStream(mockRequest);

        // Assert - Should FAIL initially
        assertNotNull(response);
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(),
                    "Implementation should return ACCEPTED for valid stream configuration");
        assertEquals(DataStreamMode.BATCH, response.getAppliedMode());
        assertTrue(response.getAppliedBufferSize() > 0, "Buffer size should be positive");
    }
}
