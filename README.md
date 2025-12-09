# Software Engineering Project Starter Code

Chosen computation: The system will compute the factorial of a given input integer.
Description: Given a positive integer n, the system will run the product of all positive integers less than or equal to n.
![System Diagram](https://github.com/CPS353-Suny-New-Paltz/project-starter-code-Sean-1274/blob/main/images/Checkpoint2_Diagram.jpeg?raw=true)

MULTI-THREADING 
My chosen upper bound for the number of threads to use is 4 


COMPUTE ENGINE INTEGRATION BENCHMARK TEST:

a. Original: 7 ms, Cached: 4 ms, Improvement: 42.86%

b. Link to Benchmark Test: https://github.com/CPS353-Suny-New-Paltz/project-starter-code-Sean-1274/blob/main/test/ComputeEngineBenchmarkTest.java

c. The issue with the previous version is that the compute engine would recompute factorial values from scratch for every request, even when identical inputs were processed repeatedly. This meant that each factorial calculation performed the full BigInteger multiplication loop from 2 to n every time, causing redundant CPU work. The fix to this issue was to implement caching (ConcurrentHashMap) in the compute engine, so that when a factorial is computed, its result is stored. Then, if the same factorial calculation is requested, it returns the cached result immediately. For new inputs, it finds the largest cached factorial below the input and only computes from there, reusing intermediate results. This fix resulted in a 42.86% improvement in overall computation time. The benchmark was measured using a sample of 50 input integers with repeated values to maximize cache benefits.

d. Link to PR for the fix: https://github.com/CPS353-Suny-New-Paltz/project-starter-code-Sean-1274/pull/81
