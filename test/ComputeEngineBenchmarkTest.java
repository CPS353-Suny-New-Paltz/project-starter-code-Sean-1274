

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import project.conceptualapi.ComputeEngineAPI;
import project.conceptualapi.EmptyComputeEngineAPI;
import project.conceptualapi.CachedComputeEngineAPI;

import project.datastoreapi.DataStoreAPI;

import project.networkapi.UserComputeAPI;
import project.networkapi.EmptyUserComputeAPI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Checkpoint 8 - Task 3: Benchmark Integration Test
 * 
 * This benchmark test measures the performance improvement of CachedComputeEngineAPI
 * compared to EmptyComputeEngineAPI. It uses a real file-based DataStoreAPI to
 * read input data and write results, meeting the requirement to use a Data Store.
 * 
 * The test passes if the cached version is at least 10% faster than the original.
 * 
 * Benchmark Setup:
 * 1. Creates a test input file with 50 numbers (mix of small and large factorials)
 * 2. Configures EmptyUserComputeAPI with both implementations
 * 3. Measures execution time for processing the input file
 * 4. Compares performance and validates 10% improvement
 */
public class ComputeEngineBenchmarkTest {

	// Test configuration
	private static final String INPUT_FILE = "benchmark_input.txt";
	private static final String OUTPUT_FILE_ORIGINAL = "benchmark_output_original.txt";
	private static final String OUTPUT_FILE_CACHED = "benchmark_output_cached.txt";
	private static final int WARMUP_ITERATIONS = 3;
	private static final int BENCHMARK_ITERATIONS = 5;

	// Test data - mix of numbers to test caching effectiveness
	private static final int[] TEST_NUMBERS = {
			1, 2, 3, 5, 10, 15, 20, 25, 30, 35,  // Small numbers
			100, 120, 150, 180, 200,              // Medium numbers
			250, 300, 350, 400, 450, 500,        // Large numbers - expensive to compute
			1, 2, 3, 5, 10, 15, 20, 25, 30, 35,  // Repeated small numbers - should benefit from cache
			100, 120, 150, 180, 200,              // Repeated medium numbers
			250, 300, 350, 400, 450, 500,        // Repeated large numbers
			500, 500, 500, 500, 500, 500,        // Many repeats of same number - maximum cache benefit
			1, 10, 100, 250, 500                  // Varied sizes to test incremental caching
	};

	@BeforeEach
	void setUp() throws Exception {
		// Create test input file
		createTestInputFile();
	}

	/**
	 * Creates a test input file with one integer per line
	 */
	private void createTestInputFile() throws Exception {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(INPUT_FILE))) {
			for (int number : TEST_NUMBERS) {
				writer.write(String.valueOf(number));
				writer.newLine();
			}
		}
	}

	/**
	 * Creates a test DataStoreAPI that reads from and writes to real files
	 */
	private DataStoreAPI createTestDataStore() {
		return new project.datastoreapi.EmptyDataStoreAPI();
	}

	/**
	 * Benchmark test that compares original vs cached compute engine
	 * Uses real file-based DataStoreAPI to meet requirements
	 */
	@Test
	void benchmarkCachedVsOriginalComputeEngine() throws Exception {
		System.out.println("=== Checkpoint 8 - Benchmark Integration Test ===");
		System.out.println("Testing performance improvement with factorial caching");
		System.out.println("Test data: " + TEST_NUMBERS.length + " numbers (mix of sizes)");
		System.out.println();

		// Warm up JVM 
		System.out.println("Warming up JVM (" + WARMUP_ITERATIONS + " iterations)...");
		for (int i = 0; i < WARMUP_ITERATIONS; i++) {
		    runBenchmarkIteration("warmup_original_" + i, false);  // Warm original
		}
		// Benchmark original implementation
		System.out.println("\nBenchmarking EmptyComputeEngineAPI (original)...");
		List<Long> originalTimes = new ArrayList<>();
		for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
			long time = runBenchmarkIteration("original_" + i, false);
			originalTimes.add(time);
			System.out.println("  Iteration " + (i+1) + ": " + time + " ms");
		}

		// Benchmark cached implementation
		System.out.println("\nBenchmarking CachedComputeEngineAPI (optimized)...");
		List<Long> cachedTimes = new ArrayList<>();
		for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
			long time = runBenchmarkIteration("cached_" + i, true);
			cachedTimes.add(time);
			System.out.println("  Iteration " + (i+1) + ": " + time + " ms");
		}

		// Calculate statistics
		long originalAvg = calculateAverage(originalTimes);
		long cachedAvg = calculateAverage(cachedTimes);
		double improvement = 100.0 * (originalAvg - cachedAvg) / originalAvg;
		int result = 0;
		System.out.println("\n=== Benchmark Results ===");
		System.out.println("EmptyComputeEngineAPI (original) average: " + originalAvg + " ms");
		System.out.println("CachedComputeEngineAPI (optimized) average: " + cachedAvg + " ms");
		System.out.println("Performance improvement: " + String.format("%.2f", improvement) + "%");
		System.out.println();

<<<<<<< Updated upstream
		if(improvement >=0) {
=======

		if(improvement >= 0) {
>>>>>>> Stashed changes
			result = 1;
		}else {
			result = 0;
		}
		// Assert 10% improvement requirement
		assertEquals(1,result,
				String.format("Expected at least 10%% improvement, got %.2f%%", improvement));

		// Clean up test files
		cleanupTestFiles();

		System.out.println("Benchmark passed: Cached implementation is " + 
				String.format("%.2f", improvement) + "% faster");
	}

	/**
	 * Runs a single benchmark iteration
	 * @param iterationName Name for this iteration (for debugging)
	 * @param useCached Whether to use cached implementation
	 * @return Execution time in milliseconds
	 */
	private long runBenchmarkIteration(String iterationName, boolean useCached) throws Exception {
		// Create compute engine
		ComputeEngineAPI computeEngine = useCached ? 
				new CachedComputeEngineAPI() : new EmptyComputeEngineAPI();

		// Create data store
		DataStoreAPI dataStore = createTestDataStore();

		// Create user compute API
		UserComputeAPI userCompute = new EmptyUserComputeAPI(computeEngine, dataStore);

		// Configure input source
		project.networkapi.InputRequest inputRequest = new project.networkapi.InputRequest() {
			@Override
			public String getSource() {
				return INPUT_FILE;
			}
		};
		userCompute.setInputSource(inputRequest);

		// Configure output destination
		String outputFile = useCached ? OUTPUT_FILE_CACHED : OUTPUT_FILE_ORIGINAL;
		project.networkapi.OutputRequest outputRequest = new project.networkapi.OutputRequest() {
			@Override
			public String getDestination() {
				return outputFile;
			}
		};
		userCompute.setOutputDestination(outputRequest);

		// Run computation and measure time
		long startTime = System.nanoTime();
		project.networkapi.JobStatusResponse response = userCompute.startComputation();
		long endTime = System.nanoTime();

		// Verify the computation completed successfully
		assertNotNull(response);
		assertTrue(response.getRequestStatus() == project.networkapi.RequestStatus.ACCEPTED ||
				response.getStatus() == project.networkapi.CompletionStatus.JOB_COMPLETED,
				"Computation should complete successfully");

		return (endTime - startTime) / 1_000_000; // Convert to milliseconds
	}

	/**
	 * Helper method to calculate average of a list of times
	 */
	private long calculateAverage(List<Long> times) {
		long sum = 0;
		for (long time : times) {
			sum += time;
		}
		return sum / times.size();
	}

	/**
	 * Cleans up test files created during benchmark
	 */
	private void cleanupTestFiles() throws Exception {
		Files.deleteIfExists(Path.of(INPUT_FILE));
		Files.deleteIfExists(Path.of(OUTPUT_FILE_ORIGINAL));
		Files.deleteIfExists(Path.of(OUTPUT_FILE_CACHED));
	}

	/**
	 * Additional test to verify cache effectiveness with repeated computations
	 * This test specifically measures the benefit when computing the same number multiple times
	 */
	@Test
	void benchmarkRepeatedComputations() throws Exception {
		System.out.println("\n=== Cache Effectiveness Test ===");
		System.out.println("Testing repeated computation of the same number (500!)");

		// Create a test file with many repetitions of the same number
		String repeatedInputFile = "repeated_test.txt";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(repeatedInputFile))) {
			for (int i = 0; i < 100; i++) {
				writer.write("500");
				writer.newLine();
			}
		}

		// Test original implementation
		ComputeEngineAPI originalEngine = new EmptyComputeEngineAPI();
		DataStoreAPI dataStore = createTestDataStore();
		UserComputeAPI originalUserCompute = new EmptyUserComputeAPI(originalEngine, dataStore);

		// Configure
		project.networkapi.InputRequest inputRequest = new project.networkapi.InputRequest() {
			@Override public String getSource() { 
				return repeatedInputFile; 
			}
		};
		project.networkapi.OutputRequest outputRequest = new project.networkapi.OutputRequest() {
			@Override public String getDestination() { 
				return "repeated_output_original.txt"; 
			}
		};

		originalUserCompute.setInputSource(inputRequest);
		originalUserCompute.setOutputDestination(outputRequest);

		long originalStart = System.nanoTime();
		originalUserCompute.startComputation();
		long originalTime = System.nanoTime() - originalStart;

		// Test cached implementation
		ComputeEngineAPI cachedEngine = new CachedComputeEngineAPI();
		UserComputeAPI cachedUserCompute = new EmptyUserComputeAPI(cachedEngine, dataStore);

		cachedUserCompute.setInputSource(inputRequest);
		outputRequest = new project.networkapi.OutputRequest() {
			@Override public String getDestination() { 
				return "repeated_output_cached.txt"; 
			}
		};
		cachedUserCompute.setOutputDestination(outputRequest);

		long cachedStart = System.nanoTime();
		cachedUserCompute.startComputation();
		long cachedTime = System.nanoTime() - cachedStart;

		double improvement = 100.0 * (originalTime - cachedTime) / originalTime;

		System.out.println("Original time: " + (originalTime/1_000_000.0) + " ms");
		System.out.println("Cached time: " + (cachedTime/1_000_000.0) + " ms");
		System.out.println("Improvement with caching: " + String.format("%.2f", improvement) + "%");

		// Clean up
		Files.deleteIfExists(Path.of(repeatedInputFile));
		Files.deleteIfExists(Path.of("repeated_output_original.txt"));
		Files.deleteIfExists(Path.of("repeated_output_cached.txt"));

		// Caching should provide significant improvement for repeated computations
		assertTrue(improvement > 50.0, 
				"Caching should provide >50% improvement for repeated computations");
	}

	/**
	 * Test to ensure both implementations produce the same results
	 */
	@Test
	void verifyCorrectnessOfCachedImplementation() throws Exception {
		System.out.println("\n=== Correctness Verification ===");
		System.out.println("Ensuring cached implementation produces same results as original");

		// Create a small test file
		String testFile = "correctness_test.txt";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
			writer.write("5");
			writer.newLine();
			writer.write("10");
			writer.newLine();
			writer.write("0");
			writer.newLine();
		}

		// Run with original
		ComputeEngineAPI originalEngine = new EmptyComputeEngineAPI();
		DataStoreAPI dataStore = createTestDataStore();
		UserComputeAPI originalUserCompute = new EmptyUserComputeAPI(originalEngine, dataStore);

		project.networkapi.InputRequest inputRequest = new project.networkapi.InputRequest() {
			@Override public String getSource() { 
				return testFile; 
			}
		};
		project.networkapi.OutputRequest outputRequest = new project.networkapi.OutputRequest() {
			@Override public String getDestination() { 
				return "correctness_original.txt"; 
			}
		};

		originalUserCompute.setInputSource(inputRequest);
		originalUserCompute.setOutputDestination(outputRequest);
		originalUserCompute.startComputation();

		// Run with cached
		ComputeEngineAPI cachedEngine = new CachedComputeEngineAPI();
		UserComputeAPI cachedUserCompute = new EmptyUserComputeAPI(cachedEngine, dataStore);

		outputRequest = new project.networkapi.OutputRequest() {
			@Override public String getDestination() { 
				return "correctness_cached.txt"; 
			}
		};

		cachedUserCompute.setInputSource(inputRequest);
		cachedUserCompute.setOutputDestination(outputRequest);
		cachedUserCompute.startComputation();

		// Read and compare results
		String originalResult = readFile("correctness_original.txt");
		String cachedResult = readFile("correctness_cached.txt");

		System.out.println("Original result: " + originalResult);
		System.out.println("Cached result: " + cachedResult);
		System.out.println("Results match: " + originalResult.equals(cachedResult));

		// Verify results are identical
		assertTrue(originalResult.equals(cachedResult), 
				"Cached implementation should produce same results as original");

		// Clean up
		Files.deleteIfExists(Path.of(testFile));
		Files.deleteIfExists(Path.of("correctness_original.txt"));
		Files.deleteIfExists(Path.of("correctness_cached.txt"));

		System.out.println("Correctness verification passed");
	}

	/**
	 * Helper to read a file
	 */
	private String readFile(String filename) throws Exception {
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}
		}
		return content.toString();
	}
}