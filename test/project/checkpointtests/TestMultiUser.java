package project.checkpointtests;

import project.networkapi.UserComputeMultiThreaded;
import project.networkapi.BasicDelimiterRequest;
import project.networkapi.BasicInputRequest;
import project.networkapi.BasicOutputRequest;
import project.networkapi.DelimiterMode;
import project.networkapi.DelimiterResponse;
import project.networkapi.InputResponse;
import project.networkapi.OutputResponse;
import project.networkapi.RequestStatus;
import project.networkapi.UserComputeAPI;
import project.conceptualapi.ComputeEngineAPI;
import project.conceptualapi.EmptyComputeEngineAPI;
import project.datastoreapi.DataStoreAPI;
import project.datastoreapi.EmptyDataStoreAPI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

public class TestMultiUser {
    
    // TODO 1: change the type of this variable to the name you're using for your @NetworkAPI
    // interface
    // The coordinator that handles job submissions
    private UserComputeAPI coordinator;
    // multithreaded test implementation of the job coordinator
    private UserComputeMultiThreaded networkAPI;
    
    @BeforeEach
    public void initializeComputeEngine() {
        //TODO 2: create an instance of the implementation of your @NetworkAPI; this is the component
        // that the user will make requests to
        // Creates all API components
        ComputeEngineAPI computationAPI = new EmptyComputeEngineAPI();
        DataStoreAPI dataStorageAPI = new EmptyDataStoreAPI();
         //Creates multi threaded coordinator with the dependencies
        networkAPI = new UserComputeMultiThreaded(computationAPI,dataStorageAPI);    
         //Store it in the 'coordinator' instance variable
        coordinator = networkAPI;
    }
    
    public void cleanup() {
        if (networkAPI != null) {
            networkAPI.shutdown();
        }
    }
    @Test
    public void testBasicSetup() {
        try {
            // Test 1: Verify input file exists
            File inputFile = new File("test" + File.separator + "testInputFile.txt");
            Assertions.assertTrue(inputFile.exists(), "Input file should exist: " + inputFile.getAbsolutePath());
            
            // Test 2: Verify we can configure the coordinator
            InputResponse inputResponse = coordinator.setInputSource(new BasicInputRequest(inputFile.getPath()));
            System.out.println("Input response: " + inputResponse.getMessage());
            Assertions.assertEquals(RequestStatus.ACCEPTED, inputResponse.getStatus());
            
            OutputResponse outputResponse = coordinator.setOutputDestination(new BasicOutputRequest("test_output.tmp"));
            System.out.println("Output response: " + outputResponse.getMessage());
            Assertions.assertEquals(RequestStatus.ACCEPTED, outputResponse.getStatus());
            
            DelimiterResponse delimiterResponse = coordinator.configureDelimiters(new BasicDelimiterRequest(",", DelimiterMode.CUSTOM));
            System.out.println("Delimiter response: " + delimiterResponse.getMessage());
            Assertions.assertEquals(RequestStatus.ACCEPTED, delimiterResponse.getStatus());
            
            System.out.println("Basic setup test passed!");
            
        } catch (Exception e) {
            System.err.println("Basic setup test failed: " + e.getMessage());
            e.printStackTrace();
            Assertions.fail("Basic setup test failed: " + e.getMessage());
        }
    }
    
    @Test
   
    public void compareMultiAndSingleThreaded() throws Exception {
        int nThreads = 4;
        List<TestUser> testUsers = new ArrayList<>();
        for (int i = 0; i < nThreads; i++) {
            testUsers.add(new TestUser(coordinator));
        }
        
        // Run single threaded
        String singleThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.singleThreadOut.tmp";
        for (int i = 0; i < nThreads; i++) {
            File singleThreadedOut = 
                    new File(singleThreadFilePrefix + i);
            singleThreadedOut.deleteOnExit();
            testUsers.get(i).run(singleThreadedOut.getCanonicalPath());
        }
        
        // Run multi threaded
        ExecutorService threadPool = Executors.newCachedThreadPool();
        List<Future<?>> results = new ArrayList<>();
        String multiThreadFilePrefix = "testMultiUser.compareMultiAndSingleThreaded.test.multiThreadOut.tmp";
        for (int i = 0; i < nThreads; i++) {
            File multiThreadedOut = 
                    new File(multiThreadFilePrefix + i);
            multiThreadedOut.deleteOnExit();
            String multiThreadOutputPath = multiThreadedOut.getCanonicalPath();
            TestUser testUser = testUsers.get(i);
            results.add(threadPool.submit(() -> testUser.run(multiThreadOutputPath)));
        }
        
        results.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        
        
        // Check that the output is the same for multi-threaded and single-threaded
        List<String> singleThreaded = loadAllOutput(singleThreadFilePrefix, nThreads);
        List<String> multiThreaded = loadAllOutput(multiThreadFilePrefix, nThreads);
        Assertions.assertEquals(singleThreaded, multiThreaded);
    }

    private List<String> loadAllOutput(String prefix, int nThreads) throws IOException {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < nThreads; i++) {
            File multiThreadedOut = 
                    new File(prefix + i);
            result.addAll(Files.readAllLines(multiThreadedOut.toPath()));
        }
        return result;
    }
}