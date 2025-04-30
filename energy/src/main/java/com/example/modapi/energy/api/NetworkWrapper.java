package com.example.modapi.energy.api;

import com.astroframe.galactic.energy.api.EnergyNetwork;
import com.astroframe.galactic.energy.api.energynetwork.WorldPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Wrapper class for EnergyNetwork.
 * This provides compatibility for code that depends on the old API.
 */
public class NetworkWrapper {
    private final EnergyNetwork network;
    private final Level level;
    
    /**
     * Constructor for NetworkWrapper.
     * 
     * @param network The energy network
     * @param level The Minecraft level
     */
    public NetworkWrapper(EnergyNetwork network, Level level) {
        this.network = network;
        this.level = level;
    }
    
    /**
     * Gets the energy network.
     * 
     * @return The energy network
     */
    public EnergyNetwork getNetwork() {
        return network;
    }
    
    /**
     * Gets the energy type for this network.
     * 
     * @return The energy type
     */
    public com.astroframe.galactic.energy.api.EnergyType getEnergyType() {
        return network.getEnergyType();
    }
    
    /**
     * Gets the Minecraft level.
     * 
     * @return The level
     */
    public Level getLevel() {
        return level;
    }
    
    /**
     * Adds a node to the network at the specified position.
     * 
     * @param pos The position
     * @return True if the node was added
     */
    public boolean addNode(BlockPos pos) {
        WorldPosition position = BlockPosAdapter.toWorldPosition(pos, level);
        return network.addNode(position);
    }
    
    /**
     * Removes a node from the network at the specified position.
     * 
     * @param pos The position
     * @return True if the node was removed
     */
    public boolean removeNode(BlockPos pos) {
        WorldPosition position = BlockPosAdapter.toWorldPosition(pos, level);
        return network.removeNode(position);
    }
    
    /**
     * Checks if a node exists at the specified position.
     * 
     * @param pos The position
     * @return True if a node exists
     */
    public boolean hasNode(BlockPos pos) {
        WorldPosition position = BlockPosAdapter.toWorldPosition(pos, level);
        return network.hasNode(position);
    }
}