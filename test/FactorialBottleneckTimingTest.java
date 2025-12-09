

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import project.conceptualapi.ComputationMode;
import project.conceptualapi.ComputationResponse;
import project.conceptualapi.ComputationRequest;
import project.conceptualapi.ComputeEngineAPI;
import project.conceptualapi.BasicComputationRequest;
import project.conceptualapi.EmptyComputeEngineAPI;

/**
 * Checkpoint 8 - Performance Tuning
 *
 * This test is used to empirically measure the CPU cost of repeated factorial
 * computations in EmptyComputeEngineAPI. It exercises the same factorial
 * input many times to expose the cost of recomputing factorials from scratch
 * with BigInteger, which we later optimize by adding caching.
 */
public class FactorialBottleneckTimingTest {

    // Large enough to be expensive with BigInteger
    private static final int LARGE_N = 500;

    // Number of repeated calls for the same input
    private static final int REPEAT_COUNT = 50;

    @Test
    public void measureRepeatedFactorialTiming() {
        ComputeEngineAPI engine = new EmptyComputeEngineAPI();

        long startNs = System.nanoTime();

        for (int i = 0; i < REPEAT_COUNT; i++) {
            ComputationRequest request =
                    new BasicComputationRequest(LARGE_N, ComputationMode.FACTORIAL);

            ComputationResponse response = engine.compute(request);

            // Checks so the test also validates correctness
            assertNotNull(response, "ComputationResponse should not be null");
            assertNotNull(response.getResult(), "Factorial result string should not be null");
            assertFalse(response.getResult().isEmpty(), "Factorial result should not be empty");

        }

        long endNs = System.nanoTime();
        long durationNs = endNs - startNs;
        double durationMs = durationNs / 1_000_000.0;
        double avgMsPerCall = durationMs / REPEAT_COUNT;

        System.out.println("=== Factorial Bottleneck Baseline Timing ===");
        System.out.println("n = " + LARGE_N);
        System.out.println("repeats = " + REPEAT_COUNT);
        System.out.println("total time = " + durationMs + " ms");
        System.out.println("avg per call = " + avgMsPerCall + " ms");
        System.out.println("===========================================");

        // We don't assert a specific performance threshold here (machine-dependent),
        // only that we actually measured *some* non-zero time.
        Assertions.assertTrue(durationNs > 0);
    }
    
//    To identify a CPU-based performance bottleneck, 
//    I implemented a timing test that repeatedly computes 500! using EmptyComputeEngineAPI 
//    in FACTORIAL mode. Running 50 repeated calls on the same input took approximately 325 ms 
//    total, or about 6.5 ms per call on my machine.
//
//    Since each call recomputes the factorial from scratch using a BigInteger loop, 
//    even when the input is identical, the cost scales directly with both the input size 
//    and the number of repetitions. This repeated, redundant factorial computation is clearly
//    CPU‐bound and dominates the overall runtime, 
//    so I selected it as the primary bottleneck to optimize.

    }
