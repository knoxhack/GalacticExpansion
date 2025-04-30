package com.astroframe.galactic.energy.implementation;

import com.astroframe.galactic.energy.api.EnergyNetwork;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.api.IEnergyHandler;
import com.astroframe.galactic.energy.api.energynetwork.WorldChunk;
import com.astroframe.galactic.energy.api.energynetwork.WorldPosition;

import java.util.*;

/**
 * Base implementation of the EnergyNetwork interface.
 * Provides common functionality for energy networks.
 */
public abstract class BaseEnergyNetwork implements EnergyNetwork {
    
    protected final Map<WorldPosition, IEnergyHandler> nodes = new HashMap<>();
    protected final Map<WorldChunk, Set<WorldPosition>> chunkToNodesMap = new HashMap<>();
    protected final EnergyType energyType;
    
    /**
     * Creates a new BaseEnergyNetwork with the given energy type.
     * 
     * @param energyType The energy type
     */
    public BaseEnergyNetwork(EnergyType energyType) {
        this.energyType = energyType;
    }
    
    @Override
    public List<IEnergyHandler> getNodes() {
        return new ArrayList<>(nodes.values());
    }
    
    @Override
    public Optional<IEnergyHandler> getNode(WorldPosition position) {
        return Optional.ofNullable(nodes.get(position));
    }
    
    @Override
    public boolean addNode(IEnergyHandler handler, WorldPosition position) {
        if (nodes.containsKey(position)) {
            return false;
        }
        
        // Only add nodes that match this network's energy type
        if (handler.getEnergyType() != energyType) {
            return false;
        }
        
        nodes.put(position, handler);
        
        // Register in chunk map for efficient loading/unloading
        WorldChunk chunk = new WorldChunk(position);
        chunkToNodesMap.computeIfAbsent(chunk, k -> new HashSet<>()).add(position);
        
        return true;
    }
    
    @Override
    public boolean removeNode(WorldPosition position) {
        IEnergyHandler handler = nodes.remove(position);
        
        if (handler != null) {
            // Remove from chunk mapping
            WorldChunk chunk = new WorldChunk(position);
            Set<WorldPosition> chunkNodes = chunkToNodesMap.get(chunk);
            
            if (chunkNodes != null) {
                chunkNodes.remove(position);
                
                if (chunkNodes.isEmpty()) {
                    chunkToNodesMap.remove(chunk);
                }
            }
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public void onChunkStatusChange(WorldChunk chunk, boolean loaded) {
        // Override in subclasses if needed
    }
    
    @Override
    public EnergyType getEnergyType() {
        return energyType;
    }
    
    @Override
    public boolean areNodesConnected(WorldPosition from, WorldPosition to) {
        List<WorldPosition> path = findPath(from, to);
        return !path.isEmpty();
    }
}