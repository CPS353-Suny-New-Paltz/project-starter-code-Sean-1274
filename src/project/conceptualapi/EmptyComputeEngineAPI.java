package project.conceptualapi;

import java.math.BigInteger;

/**
 * Concrete implementation of ComputeEngineAPI with factorial computation.
 * Central component that coordinates between Network API and Data Store API.
 */
public class EmptyComputeEngineAPI implements ComputeEngineAPI {

   

    public EmptyComputeEngineAPI() {
    }

    @Override
    public ComputationResponse compute(ComputationRequest request) {
        try {
            // Validate the request
            if (request == null) {
                return new BasicComputationResponse("Error: Computation request cannot be null");
            }

            // Validate computation mode
            ComputationMode mode = request.getMode();
            if (mode == null) {
                return new BasicComputationResponse("Error: Computation mode cannot be null");
            }

            int input = request.getInput();
            
            // Validate input range for all modes
            if (input < 0) {
                return new BasicComputationResponse("Error: Input cannot be negative");
            }
            
            // Handle different computation modes
            switch (mode) {
                case FACTORIAL:
                    return computeFactorial(input);
                case PROTOTYPE_ONLY:
                    return runPrototypeComputation(input);
                default:
                    return new BasicComputationResponse("Error: Unsupported computation mode: " + mode);
            }
        } catch (Exception e) {
            // Catch any unexpected runtime exceptions
            System.err.println("Unexpected error in compute: " + e.getMessage());
            return new BasicComputationResponse("Error: Internal computation error");
        }
    }

    /**
     * Computes factorial of the given input number.
     * Uses BigInteger to handle large factorial results.
     */
    private ComputationResponse computeFactorial(int input) {
        try {
            // Validate input range
            if (input < 0) {
                return new BasicComputationResponse("Error: Factorial is not defined for negative numbers");
            }
            
            if (input > 1000) {
                return new BasicComputationResponse("Error: Input too large for factorial computation");
            }

            BigInteger result = calculateFactorial(input);
            return new BasicComputationResponse(result.toString());
            
        } catch (ArithmeticException e) {
            // Expected exception - computation overflow
            return new BasicComputationResponse("Error: Computation overflow for input: " + input);
        } catch (Exception e) {
            // Unexpected exceptions
            System.err.println("Unexpected error in computeFactorial: " + e.getMessage());
            return new BasicComputationResponse("Error: Internal factorial computation error");
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
    	 // No validation needed - method is private and called only after input validation
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

}