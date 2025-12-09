package project.conceptualapi;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Checkpoint 8 - Performance Tuning
 *
 * Faster ComputeEngineAPI implementation that optimizes factorial computation
 * by caching previously computed results.
 *
 * Bottleneck identified (from EmptyComputeEngineAPI):
 * - The original implementation recomputes n! from scratch on every call,
 *   using an iterative BigInteger loop from 2 → n.
 * - In timing tests (e.g., computing 500! 50 times), this repeated work
 *   results in hundreds of milliseconds of CPU time, with ~6–7 ms per call
 *   on a typical machine.
 * - Made a test to check the time of running a factorial calculation of 500 
 * 50 times and measured the average time of one calculation
 * Optimization in this implementation:
 * - Uses a thread-safe cache (ConcurrentHashMap<Integer, BigInteger>) to
 *   store factorial results.
 * - Reuses cached values on subsequent calls for the same n, eliminating
 *   repeated BigInteger multiplication for identical inputs.
 * - Also reuses the largest cached k! < n when computing a new n!, so
 *   it only multiplies from (k+1) up to n instead of 2 → n.
 *
 * This class is designed as a drop-in replacement for EmptyComputeEngineAPI
 * and is safe to use from multi-threaded coordinators (e.g., UserComputeMultiThreaded).
 */
public class CachedComputeEngineAPI implements ComputeEngineAPI {

    // Thread-safe cache of factorial values: n -> n!
    // We seed 0! and 1! = 1 to simplify logic.
    private final Map<Integer, BigInteger> factorialCache = new ConcurrentHashMap<>();

    public CachedComputeEngineAPI() {
        factorialCache.put(0, BigInteger.ONE);
        factorialCache.put(1, BigInteger.ONE);
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
                    return computeFactorialWithCache(input);
                case PROTOTYPE_ONLY:
                    return runPrototypeComputation(input);
                default:
                    return new BasicComputationResponse("Error: Unsupported computation mode: " + mode);
            }
        } catch (Exception e) {
            // Catch any unexpected runtime exceptions
            System.err.println("Unexpected error in compute (CachedComputeEngineAPI): " + e.getMessage());
            return new BasicComputationResponse("Error: Internal computation error");
        }
    }

    /**
     * Computes factorial with caching to avoid redundant BigInteger work.
     */
    private ComputationResponse computeFactorialWithCache(int input) {
        try {
            // Same input validation as original version
            if (input < 0) {
                return new BasicComputationResponse("Error: Factorial is not defined for negative numbers");
            }

            if (input > 1000) {
                return new BasicComputationResponse("Error: Input too large for factorial computation");
            }

            BigInteger result = getFactorial(input);
            return new BasicComputationResponse(result.toString());

        } catch (ArithmeticException e) {
            // Expected exception - computation overflow
            return new BasicComputationResponse("Error: Computation overflow for input: " + input);
        } catch (Exception e) {
            // Unexpected exceptions
            System.err.println("Unexpected error in computeFactorialWithCache: " + e.getMessage());
            return new BasicComputationResponse("Error: Internal factorial computation error");
        }
    }

    /**
     * Returns n! using a cached, incremental strategy:
     * - If n! is already in the cache, return it.
     * - Otherwise, find the largest k < n with k! cached, and multiply
     *   from (k+1) to n, caching intermediate results along the way.
     *
     * This is thread-safe due to use of ConcurrentHashMap. Races may cause
     * some redundant work, but correctness is preserved and overall CPU
     * usage is still much lower than recomputing from scratch every time.
     */
    private BigInteger getFactorial(int n) {
        // Fast path: already cached
        BigInteger cached = factorialCache.get(n);
        if (cached != null) {
            return cached;
        }

        // Find the largest k < n such that k! is cached
        int start = 2;
        BigInteger result = BigInteger.ONE;

        for (int k = n - 1; k >= 2; k--) {
            BigInteger partial = factorialCache.get(k);
            if (partial != null) {
                start = k + 1;
                result = partial;
                break;
            }
        }

        // If we didn't find anything above 1!, we start from 2 with result = 1
        for (int i = start; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
            // Cache intermediate factorials if not already present
            factorialCache.putIfAbsent(i, result);
        }

        // Ensure n! is in the cache and return it
        factorialCache.putIfAbsent(n, result);
        return result;
    }

    /**
     * Prototype mode computation - same behavior as in EmptyComputeEngineAPI.
     */
    private ComputationResponse runPrototypeComputation(int input) {
        String prototypeResult = String.format("PROTOTYPE: Input=%d, Mode=PROTOTYPE_ONLY", input);
        return new BasicComputationResponse(prototypeResult);
    }
}
