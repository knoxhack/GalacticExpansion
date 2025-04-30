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
     * Adds a storage to the network at the specified position.
     * 
     * @param pos The position
     * @param storage The energy storage
     */
    public void addStorage(net.minecraft.core.BlockPos pos, EnergyStorage storage) {
        WorldPosition position = BlockPosAdapter.toWorldPosition(pos, level);
        network.addStorage(position, storage);
    }
    
    /**
     * Removes a storage from the network at the specified position.
     * 
     * @param pos The position
     */
    public void removeStorage(net.minecraft.core.BlockPos pos) {
        WorldPosition position = BlockPosAdapter.toWorldPosition(pos, level);
        network.removeStorage(position);
    }
    
    /**
     * Checks if a storage exists at the specified position.
     * 
     * @param pos The position
     * @return True if a storage exists
     */
    public boolean hasStorage(net.minecraft.core.BlockPos pos) {
        WorldPosition position = BlockPosAdapter.toWorldPosition(pos, level);
        return network.hasStorage(position);
    }
    
    /**
     * Gets the storage at the specified position.
     * 
     * @param pos The position
     * @return The energy storage, or null if none exists
     */
    public EnergyStorage getStorage(net.minecraft.core.BlockPos pos) {
        WorldPosition position = BlockPosAdapter.toWorldPosition(pos, level);
        return network.getStorage(position);
    }
}