package com.example.modapi.energy.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * A simplified version of the EnergyNetwork class.
 * This provides compatibility for code that depends on the old API.
 */
public class EnergyNetworkSimplified {
    private final UUID networkId;
    private final NetworkWrapper wrapper;
    
    /**
     * Constructor for EnergyNetworkSimplified.
     * Creates a new network with the given ID.
     * 
     * @param networkId The network ID
     * @param wrapper The network wrapper
     */
    public EnergyNetworkSimplified(UUID networkId, NetworkWrapper wrapper) {
        this.networkId = networkId;
        this.wrapper = wrapper;
    }
    
    /**
     * Gets the unique ID of this network.
     * 
     * @return The network ID
     */
    public UUID getNetworkId() {
        return networkId;
    }
    
    /**
     * Gets the energy type for this network.
     * 
     * @return The energy type
     */
    public EnergyType getEnergyType() {
        return EnergyType.valueOf(wrapper.getEnergyType().name());
    }
    
    /**
     * Gets the wrapper for this network.
     * 
     * @return The network wrapper
     */
    public NetworkWrapper getWrapper() {
        return wrapper;
    }
}