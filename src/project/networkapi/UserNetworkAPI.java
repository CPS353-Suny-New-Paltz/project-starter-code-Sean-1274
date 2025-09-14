// File: NetworkAPI.java
package project.networkapi;

import project.annotations.NetworkAPI;

/**
 * Network-facing API interface between the user and the backend services.
 * This API defines the contract for submitting computation jobs via the network.
 * The interface is annotated with @NetworkAPI to indicate it's part of the
 * network-facing API surface that will be exposed to users.
 */
@NetworkAPI
public interface UserNetworkAPI {
    /**
     * Submits a computation job via the network API.
     */
    JobResponse submitJob(JobRequest jobRequest);
}