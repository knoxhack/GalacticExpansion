package com.example.modapi.energy.api;

import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.api.energynetwork.Level;
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
     * @param level The Minecraft level
     * @return The energy network
     */
    public static EnergyNetworkSimplified createNetwork(UUID networkId, net.minecraft.world.level.Level level) {
        Level customLevel = BlockPosAdapter.toCustomLevel(level);
        com.astroframe.galactic.energy.api.EnergyNetwork network = 
            new CachedEnergyNetwork(EnergyType.ELECTRICAL, customLevel);
        
        NetworkWrapper wrapper = new NetworkWrapper(network, level);
        return new EnergyNetworkSimplified(networkId, wrapper);
    }
    
    /**
     * Create a new energy network with a specific energy type.
     * 
     * @param networkId The network ID
     * @param level The Minecraft level
     * @param type The energy type
     * @return The energy network
     */
    public static EnergyNetworkSimplified createNetwork(UUID networkId, net.minecraft.world.level.Level level, EnergyType type) {
        Level customLevel = BlockPosAdapter.toCustomLevel(level);
        com.astroframe.galactic.energy.api.EnergyType newType;
        
        // Map old energy types to new energy types
        switch (type) {
            case FORGE_ENERGY:
            case REDSTONE_FLUX:
                newType = com.astroframe.galactic.energy.api.EnergyType.ELECTRICAL;
                break;
            case MODAPI_ENERGY:
                newType = com.astroframe.galactic.energy.api.EnergyType.QUANTUM;
                break;
            default:
                newType = com.astroframe.galactic.energy.api.EnergyType.ELECTRICAL;
                break;
        }
        
        com.astroframe.galactic.energy.api.EnergyNetwork network = 
            new CachedEnergyNetwork(newType, customLevel);
        
        NetworkWrapper wrapper = new NetworkWrapper(network, level);
        return new EnergyNetworkSimplified(networkId, wrapper);
    }
}