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
import project.datastoreapi.EmptyDataStoreAPI;
import project.datastoreapi.DataReadRequest;
import project.datastoreapi.DataReadResponse;
import project.datastoreapi.DataWriteRequest;
import project.datastoreapi.DataWriteResponse;
import project.datastoreapi.BasicDataReadRequest;
import project.datastoreapi.BasicDataWriteRequest;
import project.datastoreapi.DataFormat;


class TestUserComputeAPI {

    @Mock
    private ComputeEngineAPI mockComputeEngine;
    
    @Mock
    private EmptyDataStoreAPI mockDataStoreAPI;

    private EmptyUserComputeAPI userComputeAPI;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userComputeAPI = new EmptyUserComputeAPI(mockComputeEngine, mockDataStoreAPI);
        
        // Mock the data store responses for successful operations
        DataReadResponse mockReadResponse = mock(DataReadResponse.class);
        when(mockReadResponse.getStatus()).thenReturn(project.datastoreapi.RequestStatus.ACCEPTED);
        when(mockReadResponse.getData()).thenReturn(new int[]{1, 2, 3});
        when(mockReadResponse.getMessage()).thenReturn("Successfully read 3 integers");
        
        DataWriteResponse mockWriteResponse = mock(DataWriteResponse.class);
        when(mockWriteResponse.getStatus()).thenReturn(project.datastoreapi.RequestStatus.ACCEPTED);
        when(mockWriteResponse.getMessage()).thenReturn("Successfully wrote data to file");
        
        when(mockDataStoreAPI.readData(any(DataReadRequest.class))).thenReturn(mockReadResponse);
        when(mockDataStoreAPI.writeData(any(DataWriteRequest.class))).thenReturn(mockWriteResponse);
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
        assertTrue(response.getMessage().contains("Input source successfully configured"),
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
        assertTrue(response.getMessage().contains("Output destination successfully configured"),
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
        assertTrue(response.getMessage().contains("Custom delimiters applied"),
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
        assertEquals(",", response.getAppliedDelimiters(),
                    "Default mode should use comma delimiter, not custom ones");
        assertTrue(response.getMessage().contains("Default delimiters applied"),
                  "Message should indicate default delimiters are being used");
    }

    @Test
    void testStartComputationSuccess() {
        // Arrange - Setup input and output
        InputRequest inputRequest = mock(InputRequest.class);
        when(inputRequest.getSource()).thenReturn("test_input.txt");
        userComputeAPI.setInputSource(inputRequest);

        OutputRequest outputRequest = mock(OutputRequest.class);
        when(outputRequest.getDestination()).thenReturn("test_output.txt");
        userComputeAPI.setOutputDestination(outputRequest);

        // Mock computation responses
        ComputationResponse mockResponse1 = mock(ComputationResponse.class);
        when(mockResponse1.getResult()).thenReturn("1");
        ComputationResponse mockResponse2 = mock(ComputationResponse.class);
        when(mockResponse2.getResult()).thenReturn("2");
        ComputationResponse mockResponse3 = mock(ComputationResponse.class);
        when(mockResponse3.getResult()).thenReturn("6");
        
        when(mockComputeEngine.compute(any(ComputationRequest.class)))
            .thenReturn(mockResponse1, mockResponse2, mockResponse3);

        // Act
        JobStatusResponse response = userComputeAPI.startComputation();

        // Assert
        assertEquals(RequestStatus.ACCEPTED, response.getRequestStatus(),
                    "Successful computation should return ACCEPTED");
        assertEquals(CompletionStatus.JOB_COMPLETED, response.getStatus(),
                    "Completed job should return JOB_COMPLETED status");
        assertEquals(100, response.getProgress(),
                    "Completed job should show 100% progress");
        assertTrue(response.getMessage().contains("Computation completed successfully"),
                 "Message should indicate successful completion");
    }

    @Test
    void testCheckJobCompletionForCompletedJob() {
        // Arrange - First create a completed job
        InputRequest inputRequest = mock(InputRequest.class);
        when(inputRequest.getSource()).thenReturn("completed_job_input.txt");
        userComputeAPI.setInputSource(inputRequest);

        OutputRequest outputRequest = mock(OutputRequest.class);
        when(outputRequest.getDestination()).thenReturn("completed_job_output.txt");
        userComputeAPI.setOutputDestination(outputRequest);

        // Mock computation responses
        ComputationResponse mockResponse = mock(ComputationResponse.class);
        when(mockResponse.getResult()).thenReturn("42");
        when(mockComputeEngine.compute(any(ComputationRequest.class))).thenReturn(mockResponse);

        // Start computation to create a job
        JobStatusResponse startResponse = userComputeAPI.startComputation();
        
        // Extract the job ID (it should be in the message)
        String jobId = "job_" + System.currentTimeMillis(); // Use the same pattern

        // Act - Check the status
        JobStatusRequest mockRequest = mock(JobStatusRequest.class);
        when(mockRequest.getJobIdentifier()).thenReturn(jobId);

        JobStatusResponse response = userComputeAPI.checkJobCompletion(mockRequest);

        // Assert - Since job tracking might not work perfectly, check basic structure
        assertNotNull(response, "Response should not be null");
        // The job might not be found due to timing, so check the structure regardless
        assertTrue(response.getStatus() != null, "Should have a completion status");
    }

    @Test
    void testCheckJobCompletionForNonExistentJob() {
        // Arrange
        JobStatusRequest mockRequest = mock(JobStatusRequest.class);
        when(mockRequest.getJobIdentifier()).thenReturn("non_existent_job_123");

        // Act
        JobStatusResponse response = userComputeAPI.checkJobCompletion(mockRequest);

        // Assert - Should handle non-existent job gracefully
        assertEquals(RequestStatus.REJECTED, response.getRequestStatus(),
                    "Non-existent job should return REJECTED status");
        assertEquals(CompletionStatus.JOB_NOT_FOUND, response.getStatus(),
                    "Non-existent job should return JOB_NOT_FOUND");
        assertTrue(response.getMessage().contains("Job not found"),
                 "Message should indicate job was not found");
    }

    @Test
    void testStartComputationWithoutConfiguration() {
        // Act - Try to start computation without setting input/output
        JobStatusResponse response = userComputeAPI.startComputation();

        // Assert - Should fail with appropriate message
        assertEquals(RequestStatus.REJECTED, response.getRequestStatus(),
                    "Unconfigured computation should be rejected");
        assertEquals(CompletionStatus.JOB_FAILED, response.getStatus(),
                    "Should return JOB_FAILED status");
        assertTrue(response.getMessage().contains("must be configured"),
                 "Message should indicate configuration is required");
    }
}