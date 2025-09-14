package project.networkapi;
import project.annotations.NetworkAPI;

@NetworkAPI
public interface UserComputeEngineAPI {
    /**
     * Submit a job to the compute engine.
     *
     * @param request The job request (input, output, delimiters).
     * @return JobResponse  Metadata about the submitted job.
     */
    JobResponse submitJob(JobRequest request);
}
