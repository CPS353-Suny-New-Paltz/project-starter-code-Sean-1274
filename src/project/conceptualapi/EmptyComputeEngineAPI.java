package project.conceptualapi;

import project.networkapi.UserComputeAPI;
import project.datastoreapi.DataStoreAPI;
import project.datastoreapi.DataReadRequest;
import project.datastoreapi.DataReadResponse;
import project.datastoreapi.DataWriteRequest;
import project.datastoreapi.DataWriteResponse;
import project.datastoreapi.BasicDataReadRequest;
import project.datastoreapi.BasicDataWriteRequest;
import project.datastoreapi.DataFormat;
import project.datastoreapi.RequestStatus;

import java.math.BigInteger;

/**
 * Concrete implementation of ComputeEngineAPI with factorial computation.
 * Central component that coordinates between Network API and Data Store API.
 */
public class EmptyComputeEngineAPI implements ComputeEngineAPI {

    // Bidirectional communication with both other APIs
    private final UserComputeAPI networkAPI;
    private final DataStoreAPI dataStoreAPI;

    public EmptyComputeEngineAPI(UserComputeAPI networkAPI, DataStoreAPI dataStoreAPI) {
        this.networkAPI = networkAPI;
        this.dataStoreAPI = dataStoreAPI;
    }

    @Override
    public ComputationResponse compute(ComputationRequest request) {
        // Validate the request
        if (request == null) {
            return new BasicComputationResponse("Error: Computation request cannot be null");
        }

        int input = request.getInput();
        ComputationMode mode = request.getMode();

        // Handle different computation modes
        switch (mode) {
            case FACTORIAL:
                return computeFactorial(input);
            case PROTOTYPE_ONLY:
                return runPrototypeComputation(input);
            default:
                return new BasicComputationResponse("Error: Unsupported computation mode: " + mode);
        }
    }

    /**
     * Computes factorial of the given input number.
     * Uses BigInteger to handle large factorial results.
     * 
     * @param input the number to compute factorial for
     * @return ComputationResponse with the factorial result
     */
    private ComputationResponse computeFactorial(int input) {
        // Validate input range
        if (input < 0) {
            return new BasicComputationResponse("Error: Factorial is not defined for negative numbers");
        }
        
        if (input > 1000) {
            return new BasicComputationResponse("Error: Input too large for factorial computation");
        }

        try {
            BigInteger result = calculateFactorial(input);
            return new BasicComputationResponse(result.toString());
        } catch (ArithmeticException e) {
            return new BasicComputationResponse("Error: Computation overflow for input: " + input);
        }
    }

    /**
     * Calculates factorial using iterative approach with BigInteger.
     * More efficient and stack-safe than recursive approach for large numbers.
     * 
     * @param n the number to calculate factorial for
     * @return factorial result as BigInteger
     */
    private BigInteger calculateFactorial(int n) {
        // Base cases: 0! = 1, 1! = 1
        if (n == 0 || n == 1) {
            return BigInteger.ONE;
        }

        // Iterative computation for larger numbers
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    /**
     * Prototype mode computation - returns a formatted response for testing.
     * 
     * @param input the input number
     * @return ComputationResponse with prototype format
     */
    private ComputationResponse runPrototypeComputation(int input) {
        // For prototype mode, return a formatted response without actual computation
        String prototypeResult = String.format("PROTOTYPE: Input=%d, Mode=PROTOTYPE_ONLY", input);
        return new BasicComputationResponse(prototypeResult);
    }

    /**
     * Helper method to read input data from DataStore (for future integration)
     */
    private int[] readInputData(String source) {
        try {
            DataReadRequest readRequest = new BasicDataReadRequest(source, DataFormat.INTEGER_ARRAY);
            DataReadResponse readResponse = dataStoreAPI.readData(readRequest);
            
            if (readResponse.getStatus() == RequestStatus.ACCEPTED) {
                return readResponse.getData();
            } else {
                System.err.println("Failed to read input data: " + readResponse.getMessage());
                return new int[0];
            }
        } catch (Exception e) {
            System.err.println("Error reading input data: " + e.getMessage());
            return new int[0];
        }
    }

    /**
     * Helper method to write results to DataStore (for future integration)
     */
    private boolean writeOutputData(String destination, String[] results) {
        try {
            DataWriteRequest writeRequest = new BasicDataWriteRequest(destination, DataFormat.TEXT);
            DataWriteResponse writeResponse = dataStoreAPI.writeData(writeRequest);
            
            return writeResponse.getStatus() == RequestStatus.ACCEPTED;
        } catch (Exception e) {
            System.err.println("Error writing output data: " + e.getMessage());
            return false;
        }
    }
}