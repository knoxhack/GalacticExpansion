package com.astroframe.galactic.energy.implementation;

import com.astroframe.galactic.energy.api.EnergyNetwork;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.api.energynetwork.WorldPosition;
import java.util.*;

/**
 * Base implementation of the EnergyNetwork interface.
 * Provides common functionality for energy networks.
 */
public abstract class BaseEnergyNetwork implements EnergyNetwork {
    
    protected final Map<WorldPosition, EnergyStorage> storages = new HashMap<>();
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
    public EnergyType getEnergyType() {
        return energyType;
    }
    
    @Override
    public void addStorage(WorldPosition position, EnergyStorage storage) {
        if (storage.getEnergyType() != energyType) {
            throw new IllegalArgumentException("Storage energy type does not match network energy type");
        }
        
        storages.put(position, storage);
    }
    
    @Override
    public void removeStorage(WorldPosition position) {
        storages.remove(position);
    }
    
    @Override
    public EnergyStorage getStorage(WorldPosition position) {
        return storages.get(position);
    }
    
    @Override
    public boolean hasStorage(WorldPosition position) {
        return storages.containsKey(position);
    }
    
    /**
     * Process a single tick of energy transfers within the network.
     * This handles energy distribution from providers to consumers.
     * Each subclass should implement this based on its specific energy distribution algorithm.
     */
    @Override
    public abstract void tick();
}