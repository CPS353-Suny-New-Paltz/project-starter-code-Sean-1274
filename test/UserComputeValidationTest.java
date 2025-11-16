import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;



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
import project.datastoreapi.DataStoreAPI;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit test specifically for UserComputeAPI validation logic
 * Tests part 1 of functional requirements - parameter validation
 */
class UserComputeValidationTest {

    @Mock
    private ComputeEngineAPI mockComputeEngine;
    
    @Mock
    private DataStoreAPI mockDataStore;

    private EmptyUserComputeAPI userComputeAPI;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userComputeAPI = new EmptyUserComputeAPI(mockComputeEngine, mockDataStore);
    }

    @Test
    void testSetInputSource_NullRequest_ReturnsValidationError() {
        // Test null request validation
        InputResponse response = userComputeAPI.setInputSource(null);
        
        assertNotNull(response, "Response should not be null");
        assertEquals(RequestStatus.REJECTED, response.getStatus(), 
                    "Null request should be rejected");
        assertTrue(response.getMessage().contains("cannot be null") || 
                  response.getMessage().contains("null"),
                 "Error message should indicate null request");
    }

    @Test
    void testSetInputSource_NullSource_ReturnsValidationError() {
        // Test null source validation
        InputRequest mockRequest = mock(InputRequest.class);
        when(mockRequest.getSource()).thenReturn(null);
        
        InputResponse response = userComputeAPI.setInputSource(mockRequest);
        
        assertEquals(RequestStatus.REJECTED, response.getStatus(),
                    "Null source should be rejected");
        assertTrue(response.getMessage().contains("null") || 
                  response.getMessage().contains("empty"),
                 "Error message should indicate null/empty source");
    }

    @Test
    void testSetInputSource_EmptySource_ReturnsValidationError() {
        // Test empty source validation
        InputRequest mockRequest = mock(InputRequest.class);
        when(mockRequest.getSource()).thenReturn("   ");
        
        InputResponse response = userComputeAPI.setInputSource(mockRequest);
        
        assertEquals(RequestStatus.REJECTED, response.getStatus(),
                    "Empty source should be rejected");
        assertTrue(response.getMessage().contains("empty") || 
                  response.getMessage().contains("null"),
                 "Error message should indicate empty source");
    }

    @Test
    void testSetOutputDestination_NullRequest_ReturnsValidationError() {
        // Test null output request validation
        OutputResponse response = userComputeAPI.setOutputDestination(null);
        
        assertEquals(RequestStatus.REJECTED, response.getStatus(),
                    "Null output request should be rejected");
        assertTrue(response.getMessage().contains("cannot be null") || 
                  response.getMessage().contains("null"),
                 "Error message should indicate null request");
    }

    @Test
    void testConfigureDelimiters_NullRequest_ReturnsValidationError() {
        // Test null delimiter request validation
        DelimiterResponse response = userComputeAPI.configureDelimiters(null);
        
        assertEquals(RequestStatus.REJECTED, response.getStatus(),
                    "Null delimiter request should be rejected");
        assertTrue(response.getMessage().contains("cannot be null") || 
                  response.getMessage().contains("null"),
                 "Error message should indicate null request");
    }

    @Test
    void testConfigureDelimiters_CustomMode_EmptyDelimiters_ReturnsValidationError() {
        // Test custom mode with empty delimiters validation
        DelimiterRequest mockRequest = mock(DelimiterRequest.class);
        when(mockRequest.getMode()).thenReturn(DelimiterMode.CUSTOM);
        when(mockRequest.getDelimiters()).thenReturn("   ");
        
        DelimiterResponse response = userComputeAPI.configureDelimiters(mockRequest);
        
        assertEquals(RequestStatus.REJECTED, response.getStatus(),
                    "Empty custom delimiters should be rejected");
        assertTrue(response.getMessage().contains("cannot be null") || 
                  response.getMessage().contains("empty"),
                 "Error message should indicate empty delimiters");
    }

    @Test
    void testCheckJobCompletion_NullRequest_ReturnsValidationError() {
        // Test null job status request validation
        JobStatusResponse response = userComputeAPI.checkJobCompletion(null);
        
        assertEquals(RequestStatus.REJECTED, response.getRequestStatus(),
                    "Null job status request should be rejected");
        assertTrue(response.getMessage().contains("cannot be null") || 
                  response.getMessage().contains("null"),
                 "Error message should indicate null request");
    }

    @Test
    void testCheckJobCompletion_EmptyJobId_ReturnsValidationError() {
        // Test empty job ID validation
        JobStatusRequest mockRequest = mock(JobStatusRequest.class);
        when(mockRequest.getJobIdentifier()).thenReturn("   ");
        
        JobStatusResponse response = userComputeAPI.checkJobCompletion(mockRequest);
        
        assertEquals(RequestStatus.REJECTED, response.getRequestStatus(),
                    "Empty job ID should be rejected");
        assertTrue(response.getMessage().contains("cannot be null") || 
                  response.getMessage().contains("empty"),
                 "Error message should indicate empty job identifier");
    }

    @Test
    void testStartComputation_WithoutConfiguration_ReturnsValidationError() {
        // Test that computation cannot start without input/output configuration
        JobStatusResponse response = userComputeAPI.startComputation();
        
        assertEquals(RequestStatus.REJECTED, response.getRequestStatus(),
                    "Unconfigured computation should be rejected");
        assertEquals(CompletionStatus.JOB_FAILED, response.getStatus(),
                    "Should return JOB_FAILED status");
        assertTrue(response.getMessage().contains("configure") || 
                  response.getMessage().contains("must be"),
                 "Error message should indicate configuration requirement");
    }

    @Test
    void testValidInputConfiguration_PassesValidation() {
        // Test that valid input passes validation
        InputRequest mockRequest = mock(InputRequest.class);
        when(mockRequest.getSource()).thenReturn("valid_input.txt");
        
        InputResponse response = userComputeAPI.setInputSource(mockRequest);
        
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(),
                    "Valid input should be accepted");
        assertTrue(response.getMessage().contains("successfully") || 
                  response.getMessage().contains("configured"),
                 "Success message should confirm configuration");
    }

    @Test
    void testValidOutputConfiguration_PassesValidation() {
        // Test that valid output passes validation
        OutputRequest mockRequest = mock(OutputRequest.class);
        when(mockRequest.getDestination()).thenReturn("valid_output.txt");
        
        OutputResponse response = userComputeAPI.setOutputDestination(mockRequest);
        
        assertEquals(RequestStatus.ACCEPTED, response.getStatus(),
                    "Valid output should be accepted");
        assertTrue(response.getMessage().contains("successfully") || 
                  response.getMessage().contains("configured"),
                 "Success message should confirm configuration");
    }
}