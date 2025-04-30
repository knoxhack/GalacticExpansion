package com.astroframe.galactic.energy.api;

import com.astroframe.galactic.energy.api.energynetwork.WorldChunk;
import com.astroframe.galactic.energy.api.energynetwork.WorldPosition;

import java.util.List;
import java.util.Optional;

/**
 * Interface for energy networks.
 * Represents a network of connected energy handlers that can transfer energy between them.
 */
public interface EnergyNetwork {
    
    /**
     * Gets all nodes in the network.
     * 
     * @return All nodes in the network
     */
    List<IEnergyHandler> getNodes();
    
    /**
     * Gets the node at the given position.
     * 
     * @param position The position
     * @return The node, if one exists
     */
    Optional<IEnergyHandler> getNode(WorldPosition position);
    
    /**
     * Adds a node to the network.
     * 
     * @param handler The energy handler to add
     * @param position The position of the handler
     * @return True if the node was added
     */
    boolean addNode(IEnergyHandler handler, WorldPosition position);
    
    /**
     * Removes a node from the network.
     * 
     * @param position The position of the handler to remove
     * @return True if the node was removed
     */
    boolean removeNode(WorldPosition position);
    
    /**
     * Updates the network when a chunk is loaded or unloaded.
     * 
     * @param chunk The chunk
     * @param loaded Whether the chunk is loaded (true) or unloaded (false)
     */
    void onChunkStatusChange(WorldChunk chunk, boolean loaded);
    
    /**
     * Gets the type of energy used by this network.
     * 
     * @return The energy type
     */
    EnergyType getEnergyType();
    
    /**
     * Distributes energy from sources to sinks within the network.
     * Called each tick to transfer energy between nodes.
     * 
     * @return The amount of energy transferred
     */
    int distributeEnergy();
    
    /**
     * Finds the path from one node to another.
     * 
     * @param from The source position
     * @param to The destination position
     * @return The path between nodes, or an empty list if no path exists
     */
    List<WorldPosition> findPath(WorldPosition from, WorldPosition to);
    
    /**
     * Checks if two nodes are connected.
     * 
     * @param from The source position
     * @param to The destination position
     * @return True if the nodes are connected
     */
    boolean areNodesConnected(WorldPosition from, WorldPosition to);
    
    /**
     * Gets the energy loss for transferring energy along the given path.
     * 
     * @param path The path
     * @return The energy loss as a percentage (0.0 to 1.0)
     */
    float getEnergyLoss(List<WorldPosition> path);
    
    /**
     * Gets the energy transfer rate for the given path.
     * 
     * @param path The path
     * @return The maximum energy transfer rate
     */
    int getTransferRate(List<WorldPosition> path);
}