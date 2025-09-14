// File: UserNetworkAPIPrototype.java
package project.networkapi;

import project.annotations.NetworkAPIPrototype;

/**
 * Factory prototype for creating UserNetworkAPI instances.
 */
public interface UserNetworkAPIPrototype {
    @NetworkAPIPrototype
    UserNetworkAPI createDefault();
}