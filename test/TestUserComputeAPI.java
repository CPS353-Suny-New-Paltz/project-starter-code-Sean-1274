import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Specific imports instead of .*
import project.networkapi.UserComputeAPI;
import project.networkapi.EmptyUserComputeAPI;
import project.networkapi.InputRequest;
import project.networkapi.InputResponse;
import project.networkapi.OutputRequest;
import project.networkapi.OutputResponse;
import project.networkapi.DelimiterRequest;
import project.networkapi.DelimiterResponse;
import project.networkapi.JobStatusRequest;
import project.networkapi.JobStatusResponse;
import project.networkapi.RequestStatus;
import project.networkapi.DelimiterMode;
import project.networkapi.CompletionStatus;
import project.conceptualapi.ComputeEngineAPI;

class TestUserComputeAPI {

    @Mock
    private ComputeEngineAPI mockComputeEngine;

    private EmptyUserComputeAPI userComputeAPI;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userComputeAPI = new EmptyUserComputeAPI(mockComputeEngine);
    }

    @Test
    void testSetInputSource() {
        // Arrange
        InputRequest mockRequest = mock(InputRequest.class);
        when(mockRequest.getSource()).thenReturn("test_input.txt");

        // Act
        InputResponse response = userComputeAPI.setInputSource(mockRequest);

        // Assert - Test should FAIL because we expect ACCEPTED but get REJECTED
        assertNotNull(response, "Response should not be null");
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(),  // ← This will FAIL
                    "Implementation should return ACCEPTED for valid input");
    }

    @Test
    void testSetOutputDestination() {
        // Arrange
        OutputRequest mockRequest = mock(OutputRequest.class);
        when(mockRequest.getDestination()).thenReturn("test_output.txt");

        // Act
        OutputResponse response = userComputeAPI.setOutputDestination(mockRequest);

        // Assert - This will FAIL
        assertEquals(RequestStatus.ACCEPTED, response.getStatus()); // ← Expecting ACCEPTED
    }

    @Test
    void testConfigureDelimiters() {
        // Arrange
        DelimiterRequest mockRequest = mock(DelimiterRequest.class);
        when(mockRequest.getDelimiters()).thenReturn(",");
        when(mockRequest.getMode()).thenReturn(DelimiterMode.CUSTOM);

        // Act
        DelimiterResponse response = userComputeAPI.configureDelimiters(mockRequest);

        // Assert - This will FAIL
        assertEquals(RequestStatus.ACCEPTED, response.getStatus()); // ← Expecting ACCEPTED
    }

    @Test
    void testCheckJobCompletion() {
        // Arrange
        JobStatusRequest mockRequest = mock(JobStatusRequest.class);
        when(mockRequest.getJobIdentifier()).thenReturn("test_job_123");

        // Act
        JobStatusResponse response = userComputeAPI.checkJobCompletion(mockRequest);

        // Assert - This will FAIL
        assertEquals(RequestStatus.ACCEPTED, response.getRequestStatus()); // ← Expecting ACCEPTED
        assertEquals(CompletionStatus.JOB_COMPLETED, response.getStatus()); // ← Expecting COMPLETED
    }
}
