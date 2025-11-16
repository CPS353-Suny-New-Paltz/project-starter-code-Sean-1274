import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import project.networkapi.EmptyUserComputeAPI;
import project.networkapi.InputRequest;
import project.networkapi.InputResponse;
import project.networkapi.OutputRequest;
import project.networkapi.OutputResponse;
import project.networkapi.JobStatusResponse;
import project.networkapi.RequestStatus;
import project.networkapi.CompletionStatus;

import project.conceptualapi.ComputeEngineAPI;
import project.conceptualapi.ComputationRequest;
import project.conceptualapi.ComputationResponse;
import project.conceptualapi.BasicComputationRequest;
import project.conceptualapi.ComputationMode;

import project.datastoreapi.DataStoreAPI;
import project.datastoreapi.DataReadRequest;
import project.datastoreapi.DataReadResponse;
import project.datastoreapi.DataWriteRequest;
import project.datastoreapi.DataWriteResponse;


import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Integration test for exception handling logic
 * Verifies that exceptions are caught and transformed into error responses
 * instead of propagating as uncaught exceptions
 */
class ExceptionIntegrationTest {

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
	void testExceptionInDataStoreRead() {
		// Arrange - Configure input/output
		InputRequest inputRequest = mock(InputRequest.class);
		when(inputRequest.getSource()).thenReturn("test_input.txt");
		userComputeAPI.setInputSource(inputRequest);

		OutputRequest outputRequest = mock(OutputRequest.class);
		when(outputRequest.getDestination()).thenReturn("test_output.txt");
		userComputeAPI.setOutputDestination(outputRequest);

		// Mock DataStore to throw RuntimeException during read
		when(mockDataStore.readData(any(DataReadRequest.class)))
		.thenThrow(new RuntimeException("Simulated file read error"));

		// Act - This should NOT throw an exception but return error response
		JobStatusResponse response = userComputeAPI.startComputation();

		// Assert - Exception should be caught and transformed
		assertNotNull(response, "Response should not be null - no exception should propagate");
		assertEquals(RequestStatus.ACCEPTED, response.getRequestStatus(),
				"Exception handling should return ACCEPTED status for error responses");
		assertEquals(CompletionStatus.JOB_FAILED, response.getStatus(),
				"Should return JOB_FAILED status when exception occurs");
		assertTrue(response.getMessage().contains("Computation failed"),
				"Error message should indicate computation failure");
		assertTrue(response.getMessage().contains("Simulated file read error"),
				"Should preserve original exception message for expected errors");
	}

	@Test
	void testUnexpectedExceptionInComputeEngine() {
		// Arrange - Configure input/output
		InputRequest inputRequest = mock(InputRequest.class);
		when(inputRequest.getSource()).thenReturn("test_input.txt");
		userComputeAPI.setInputSource(inputRequest);

		OutputRequest outputRequest = mock(OutputRequest.class);
		when(outputRequest.getDestination()).thenReturn("test_output.txt");
		userComputeAPI.setOutputDestination(outputRequest);

		// Mock successful data store read
		DataReadResponse mockReadResponse = mock(DataReadResponse.class);
		when(mockReadResponse.getStatus()).thenReturn(project.datastoreapi.RequestStatus.ACCEPTED);
		when(mockReadResponse.getData()).thenReturn(new int[]{1, 2, 3});
		when(mockDataStore.readData(any(DataReadRequest.class))).thenReturn(mockReadResponse);

		// Mock ComputeEngine to throw unexpected exception
		when(mockComputeEngine.compute(any(ComputationRequest.class)))
		.thenThrow(new RuntimeException("Unexpected computation error"));

		// Act - This should NOT throw an exception
		JobStatusResponse response = userComputeAPI.startComputation();

		// Assert - Unexpected exception should be caught and generic error returned
		assertNotNull(response, "Response should not be null");
		assertEquals(RequestStatus.ACCEPTED, response.getRequestStatus(),
				"Should return ACCEPTED status even for unexpected exceptions");
		assertEquals(CompletionStatus.JOB_FAILED, response.getStatus(),
				"Should return JOB_FAILED status");
		// For unexpected exceptions, might get generic error message
		assertTrue(response.getMessage() != null && !response.getMessage().isEmpty(),
				"Should return some error message");
	}

	@Test
	void testNullPointerExceptionInConfiguration() {
		// Arrange - Mock input request that causes NPE
		InputRequest problematicInput = mock(InputRequest.class);
		when(problematicInput.getSource()).thenThrow(new NullPointerException("Simulated NPE in getSource()"));

		// Act - This should catch the NPE and return error response
		InputResponse response = userComputeAPI.setInputSource(problematicInput);

		// Assert - NPE should be caught and transformed
		assertNotNull(response, "Response should not be null");
		assertEquals(RequestStatus.REJECTED, response.getStatus(),
				"Should return REJECTED status for configuration errors");
		assertTrue(response.getMessage().contains("Error configuring") ||
				response.getMessage().contains("Internal error"),
				"Should return generic error message for unexpected exceptions");
	}

	@Test
	void testExceptionInDataStoreWrite() {
		// Arrange - Configure input/output and mock successful read + computation
		InputRequest inputRequest = mock(InputRequest.class);
		when(inputRequest.getSource()).thenReturn("test_input.txt");
		userComputeAPI.setInputSource(inputRequest);

		OutputRequest outputRequest = mock(OutputRequest.class);
		when(outputRequest.getDestination()).thenReturn("test_output.txt");
		userComputeAPI.setOutputDestination(outputRequest);

		// Mock successful data store read
		DataReadResponse mockReadResponse = mock(DataReadResponse.class);
		when(mockReadResponse.getStatus()).thenReturn(project.datastoreapi.RequestStatus.ACCEPTED);
		when(mockReadResponse.getData()).thenReturn(new int[]{5});
		when(mockDataStore.readData(any(DataReadRequest.class))).thenReturn(mockReadResponse);

		// Mock successful computation
		ComputationResponse mockCompResponse = mock(ComputationResponse.class);
		when(mockCompResponse.getResult()).thenReturn("120");
		when(mockComputeEngine.compute(any(ComputationRequest.class))).thenReturn(mockCompResponse);

		// Mock DataStore to throw exception during write
		when(mockDataStore.writeData(any(DataWriteRequest.class)))
		.thenThrow(new RuntimeException("Simulated write permission error"));

		// Act - Should handle write exception gracefully
		JobStatusResponse response = userComputeAPI.startComputation();

		// Assert - Write exception should be caught
		assertNotNull(response, "Response should not be null");
		assertEquals(CompletionStatus.JOB_FAILED, response.getStatus(),
				"Should return JOB_FAILED when write fails");
		assertTrue(response.getMessage().contains("Failed to write results") ||
				response.getMessage().contains("Computation failed"),
				"Should indicate write failure in error message");
	}

	@Test
	void testMultipleExceptionsHandledGracefully() {
		// Arrange - Test that multiple potential exception points are all handled
		InputRequest inputRequest = mock(InputRequest.class);
		when(inputRequest.getSource()).thenReturn(null); // This should trigger validation error

		// Act - Null source should be caught by validation, not cause uncaught exception
		InputResponse response = userComputeAPI.setInputSource(inputRequest);

		// Assert - Validation error handled properly
		assertNotNull(response, "Response should not be null");
		assertEquals(RequestStatus.REJECTED, response.getStatus(),
				"Validation error should return REJECTED status");
		assertTrue(response.getMessage().contains("cannot be null") ||
				response.getMessage().contains("null") ||
				response.getMessage().contains("empty"),
				"Should return descriptive validation error message");

		// Test another scenario - empty output destination
		OutputRequest outputRequest = mock(OutputRequest.class);
		when(outputRequest.getDestination()).thenReturn("");

		OutputResponse outputResponse = userComputeAPI.setOutputDestination(outputRequest);

		// Assert - Another validation error handled properly
		assertNotNull(outputResponse, "Output response should not be null");
		assertEquals(RequestStatus.REJECTED, outputResponse.getStatus(),
				"Empty destination should be rejected");
	}

	@Test
	void testComputeEngineExceptionPropagationHandling() {
		// Test that ComputeEngine exceptions don't propagate to API boundary
		ComputeEngineAPI throwingEngine = new ComputeEngineAPI() {
			@Override
			public ComputationResponse compute(ComputationRequest request) {
				throw new RuntimeException("Direct exception from compute engine");
			}
		};

		EmptyUserComputeAPI isolatedUserAPI = new EmptyUserComputeAPI(throwingEngine, mockDataStore);

		// Configure input/output
		InputRequest inputRequest = mock(InputRequest.class);
		when(inputRequest.getSource()).thenReturn("test.txt");
		isolatedUserAPI.setInputSource(inputRequest);

		OutputRequest outputRequest = mock(OutputRequest.class);
		when(outputRequest.getDestination()).thenReturn("output.txt");
		isolatedUserAPI.setOutputDestination(outputRequest);

		// Mock data store to return some data
		DataReadResponse mockReadResponse = mock(DataReadResponse.class);
		when(mockReadResponse.getStatus()).thenReturn(project.datastoreapi.RequestStatus.ACCEPTED);
		when(mockReadResponse.getData()).thenReturn(new int[]{1});
		when(mockDataStore.readData(any(DataReadRequest.class))).thenReturn(mockReadResponse);

		// Act - This should NOT throw an exception despite ComputeEngine throwing one
		JobStatusResponse response = isolatedUserAPI.startComputation();

		// Assert - Exception should be caught and handled
		assertNotNull(response, "Response should not be null - exception was caught");
		assertEquals(CompletionStatus.JOB_FAILED, response.getStatus(),
				"Should return JOB_FAILED status");
		assertTrue(response.getRequestStatus() == RequestStatus.ACCEPTED,
				"Should return ACCEPTED request status for error handling");
	}
	@Test
	void testCheckedExceptionHandling() {
		// Arrange
		InputRequest inputRequest = mock(InputRequest.class);
		when(inputRequest.getSource()).thenReturn("test.txt");
		userComputeAPI.setInputSource(inputRequest);

		OutputRequest outputRequest = mock(OutputRequest.class);
		when(outputRequest.getDestination()).thenReturn("output.txt");
		userComputeAPI.setOutputDestination(outputRequest);

		// Mock DataStore to throw a checked exception (like IOException)
		when(mockDataStore.readData(any(DataReadRequest.class)))
		.thenThrow(new RuntimeException(new java.io.IOException("Simulated IO exception")));

		// Act
		JobStatusResponse response = userComputeAPI.startComputation();

		// Assert
		assertNotNull(response);
		assertEquals(CompletionStatus.JOB_FAILED, response.getStatus());
		assertTrue(response.getMessage().contains("Computation failed"));
	}
}