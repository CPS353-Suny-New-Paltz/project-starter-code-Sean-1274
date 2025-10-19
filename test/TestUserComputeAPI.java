import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
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
import project.conceptualapi.ComputationRequest;
import project.conceptualapi.ComputationResponse;
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
        // Arrange - Test with specific file source that should be accepted
        InputRequest mockRequest = mock(InputRequest.class);
        when(mockRequest.getSource()).thenReturn("valid_input_data.txt");

        // Act
        InputResponse response = userComputeAPI.setInputSource(mockRequest);

        // Assert - Check for specific successful configuration
        assertNotNull(response, "Response should not be null");
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(), 
                    "Valid input file should be accepted");
        assertTrue(response.getMessage().toLowerCase().contains("input source configured") || 
                  response.getMessage().toLowerCase().contains("success"),
                 "Success message should confirm input configuration");
    }

    @Test
    void testSetOutputDestination() {
        // Arrange - Test with specific output destination
        OutputRequest mockRequest = mock(OutputRequest.class);
        when(mockRequest.getDestination()).thenReturn("computation_results.txt");

        // Act
        OutputResponse response = userComputeAPI.setOutputDestination(mockRequest);

        // Assert - Check for specific output configuration success
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(),
                    "Valid output destination should be accepted");
        assertTrue(response.getMessage().toLowerCase().contains("output configured") ||
                  response.getMessage().toLowerCase().contains("ready for results"),
                 "Success message should indicate output is ready");
    }

    @Test
    void testConfigureDelimitersWithCustomMode() {
        // Arrange - Test custom delimiter configuration
        DelimiterRequest mockRequest = mock(DelimiterRequest.class);
        when(mockRequest.getDelimiters()).thenReturn(";|:");
        when(mockRequest.getMode()).thenReturn(DelimiterMode.CUSTOM);

        // Act
        DelimiterResponse response = userComputeAPI.configureDelimiters(mockRequest);

        // Assert - Check that custom delimiters were applied
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(),
                    "Custom delimiter configuration should be accepted");
        assertEquals(";|:", response.getAppliedDelimiters(),
                    "Applied delimiters should match requested custom delimiters");
        assertTrue(response.getMessage().toLowerCase().contains("custom delimiters applied"),
                  "Message should confirm custom delimiter configuration");
    }

    @Test
    void testConfigureDelimitersWithDefaultMode() {
        // Arrange - Test default delimiter configuration (should ignore custom delimiters)
        DelimiterRequest mockRequest = mock(DelimiterRequest.class);
        when(mockRequest.getDelimiters()).thenReturn("should_be_ignored");
        when(mockRequest.getMode()).thenReturn(DelimiterMode.DEFAULT);

        // Act
        DelimiterResponse response = userComputeAPI.configureDelimiters(mockRequest);

        // Assert - Check that default delimiters were applied
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(),
                    "Default delimiter configuration should be accepted");
        assertTrue(!response.getAppliedDelimiters().equals("should_be_ignored"),
                  "Default mode should not use custom delimiters");
        assertTrue(response.getMessage().toLowerCase().contains("default"),
                  "Message should indicate default delimiters are being used");
    }

    @Test
    void testCheckJobCompletionForCompletedJob() {
        // Arrange - Test job status check for a completed job
        JobStatusRequest mockRequest = mock(JobStatusRequest.class);
        when(mockRequest.getJobIdentifier()).thenReturn("completed_job_123");

        // Act
        JobStatusResponse response = userComputeAPI.checkJobCompletion(mockRequest);

        // Assert - Check for specific job completion status
        assertEquals(RequestStatus.ACCEPTED, response.getRequestStatus(),
                    "Valid job status request should be accepted");
        assertEquals(CompletionStatus.JOB_COMPLETED, response.getStatus(),
                    "Completed job should return JOB_COMPLETED status");
        assertEquals(100, response.getProgress(),
                    "Completed job should show 100% progress");
        assertTrue(response.getMessage().toLowerCase().contains("completed") ||
                  response.getMessage().toLowerCase().contains("finished"),
                 "Message should indicate job completion");
    }

    @Test
    void testCheckJobCompletionForRunningJob() {
        // Arrange - Test job status check for a running job
        JobStatusRequest mockRequest = mock(JobStatusRequest.class);
        when(mockRequest.getJobIdentifier()).thenReturn("running_job_456");

        // Act
        JobStatusResponse response = userComputeAPI.checkJobCompletion(mockRequest);

        // Assert - Check for running job status with specific progress
        assertEquals(RequestStatus.ACCEPTED, response.getRequestStatus());
        assertEquals(CompletionStatus.JOB_RUNNING, response.getStatus(),
                    "Running job should return JOB_RUNNING status");
        assertTrue(response.getProgress() >= 0 && response.getProgress() < 100,
                  "Running job progress should be between 0 and 100");
        assertTrue(response.getMessage().toLowerCase().contains("running") ||
                  response.getMessage().toLowerCase().contains("processing"),
                 "Message should indicate job is in progress");
    }
}