package com.astroframe.galactic.energy.api;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Collection;

/**
 * Interface for energy networks.
 * An energy network connects energy sources, consumers, and storage devices
 * in a spatial representation, allowing for pathing and distance-based calculations.
 */
public interface EnergyNetwork {
    
    /**
     * Get the unique identifier for this network.
     * 
     * @return The network ID
     */
    ResourceLocation getId();
    
    /**
     * Get the energy type this network handles.
     * 
     * @return The energy type
     */
    EnergyType getEnergyType();
    
    /**
     * Get the level (dimension) this network exists in.
     * 
     * @return The level
     */
    Level getLevel();
    
    /**
     * Add a node to the network at the specified position.
     * 
     * @param pos The position of the node
     * @param storage The energy storage at this node
     * @return true if the node was added, false if a node already exists at that position
     */
    boolean addNode(BlockPos pos, EnergyStorage storage);
    
    /**
     * Remove a node from the network.
     * 
     * @param pos The position of the node to remove
     * @return true if the node was removed, false if no node exists at that position
     */
    boolean removeNode(BlockPos pos);
    
    /**
     * Get all node positions in the network.
     * 
     * @return A collection of block positions
     */
    Collection<BlockPos> getNodes();
    
    /**
     * Get the energy storage at the specified node.
     * 
     * @param pos The position of the node
     * @return The energy storage, or null if no node exists at that position
     */
    EnergyStorage getNodeStorage(BlockPos pos);
    
    /**
     * Transfer energy between two nodes in the network.
     * 
     * @param source The source node position
     * @param destination The destination node position
     * @param amount The maximum amount of energy to transfer
     * @param simulate If true, the operation will only be simulated
     * @return The result of the energy transfer operation
     */
    EnergyTransferResult transferEnergy(BlockPos source, BlockPos destination, int amount, boolean simulate);
    
    /**
     * Add a storage device to the network.
     * 
     * @param storage The storage to add
     * @return true if the storage was added, false if it was incompatible
     * @deprecated Use {@link #addNode(BlockPos, EnergyStorage)} instead
     */
    @Deprecated
    default boolean addStorage(EnergyStorage storage) {
        // Default implementation for backward compatibility
        return false;
    }
    
    /**
     * Remove a storage device from the network.
     * 
     * @param storage The storage to remove
     * @return true if the storage was removed, false if it wasn't in the network
     * @deprecated Use {@link #removeNode(BlockPos)} instead
     */
    @Deprecated
    default boolean removeStorage(EnergyStorage storage) {
        // Default implementation for backward compatibility
        return false;
    }
    
    /**
     * Get all storage devices in the network.
     * 
     * @return A collection of storage devices
     * @deprecated Use {@link #getNodes()} and {@link #getNodeStorage(BlockPos)} instead
     */
    @Deprecated
    default Collection<EnergyStorage> getStorageDevices() {
        // Default implementation for backward compatibility
        return java.util.Collections.emptyList();
    }
    
    /**
     * Add energy to the network.
     * The energy will be distributed among storage devices.
     * 
     * @param amount The amount of energy to add
     * @param simulate If true, the operation will only be simulated
     * @return The amount of energy that was (or would have been) added
     * @deprecated Use {@link #transferEnergy(BlockPos, BlockPos, int, boolean)} instead
     */
    @Deprecated
    default int addEnergy(int amount, boolean simulate) {
        // Default implementation for backward compatibility
        return 0;
    }
    
    /**
     * Extract energy from the network.
     * The energy will be taken from storage devices.
     * 
     * @param amount The maximum amount of energy to extract
     * @param simulate If true, the operation will only be simulated
     * @return The amount of energy that was (or would have been) extracted
     * @deprecated Use {@link #transferEnergy(BlockPos, BlockPos, int, boolean)} instead
     */
    @Deprecated
    default int extractEnergy(int amount, boolean simulate) {
        // Default implementation for backward compatibility
        return 0;
    }
    
    /**
     * Get the total amount of energy stored in the network.
     * 
     * @return The total stored energy
     */
    default int getEnergy() {
        return getNodes().stream()
            .map(this::getNodeStorage)
            .filter(java.util.Objects::nonNull)
            .mapToInt(EnergyStorage::getEnergyStored)
            .sum();
    }
    
    /**
     * Get the maximum amount of energy that can be stored in the network.
     * 
     * @return The maximum energy capacity
     */
    default int getMaxEnergy() {
        return getNodes().stream()
            .map(this::getNodeStorage)
            .filter(java.util.Objects::nonNull)
            .mapToInt(EnergyStorage::getMaxEnergyStored)
            .sum();
    }
    
    /**
     * Notifies the network that a chunk's loaded status has changed.
     * Implementations should handle this to optimize network operations 
     * with unloaded chunks.
     * 
     * @param chunkPos The chunk position that changed
     * @param loaded Whether the chunk is now loaded (true) or unloaded (false)
     */
    default void onChunkStatusChange(ChunkPos chunkPos, boolean loaded) {
        // Default empty implementation
    }
    
    /**
     * Process the network for one game tick.
     * This includes transferring energy between nodes, balancing storage,
     * and any other periodic operations.
     */
    default void tick() {
        // Default empty implementation
    }
}