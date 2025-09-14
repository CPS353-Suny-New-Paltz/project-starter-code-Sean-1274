package project.networkapi;

import project.annotations.NetworkAPIPrototype;

/**

 * Prototype API for job submission.
 * 
 * Provides a placeholder "createDefault" so later
 * implementations can be plugged in.
 */

public interface UserComputeEngineAPIPrototype {
	@NetworkAPIPrototype
    UserComputeEngineAPI createDefault();
}
