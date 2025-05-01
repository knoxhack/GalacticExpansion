package com.example.modapi.energy.api;

import com.astroframe.galactic.energy.implementation.CachedEnergyNetwork;
import java.util.UUID;

/**
 * Factory class to create energy networks.
 * This provides compatibility for code that depends on the old API.
 */
public class EnergyNetworkFactory {
    
    /**
     * Create a new energy network.
     * 
     * @param networkId The network ID
     * @param mcLevel The Minecraft level or any other level object
     * @return The energy network
     */
    public static EnergyNetworkSimplified createNetwork(UUID networkId, Object mcLevel) {
        // Convert to our custom Level class
        Level customLevelOld = Level.fromMinecraftLevel(mcLevel);
        com.astroframe.galactic.energy.api.energynetwork.Level customLevel = 
            new com.astroframe.galactic.energy.api.energynetwork.Level(customLevelOld.getDimension());
        
        com.astroframe.galactic.energy.api.EnergyNetwork network = 
            new CachedEnergyNetwork(com.astroframe.galactic.energy.api.EnergyType.ELECTRICAL, customLevel);
        
        NetworkWrapper wrapper = new NetworkWrapper(network, customLevelOld);
        return new EnergyNetworkSimplified(networkId, wrapper);
    }
    
    /**
     * Create a new energy network with a specific energy type.
     * 
     * @param networkId The network ID
     * @param mcLevel The Minecraft level or any other level object
     * @param type The energy type
     * @return The energy network
     */
    public static EnergyNetworkSimplified createNetwork(UUID networkId, Object mcLevel, EnergyType type) {
        // Convert to our custom Level class
        Level customLevelOld = Level.fromMinecraftLevel(mcLevel);
        com.astroframe.galactic.energy.api.energynetwork.Level customLevel = 
            new com.astroframe.galactic.energy.api.energynetwork.Level(customLevelOld.getDimension());
        
        com.astroframe.galactic.energy.api.EnergyType newType;
        
        // Convert the old energy type to the new energy type
        newType = EnergyTypeAdapter.toNewType(type);
        
        com.astroframe.galactic.energy.api.EnergyNetwork network = 
            new CachedEnergyNetwork(newType, customLevel);
        
        NetworkWrapper wrapper = new NetworkWrapper(network, customLevelOld);
        return new EnergyNetworkSimplified(networkId, wrapper);
    }
}