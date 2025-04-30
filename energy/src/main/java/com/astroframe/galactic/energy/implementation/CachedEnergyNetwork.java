package com.astroframe.galactic.energy.implementation;

import com.astroframe.galactic.energy.api.EnergyNetwork;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.api.energynetwork.WorldPosition;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of EnergyNetwork that caches the energy handlers in the network.
 * It allows for efficient energy distribution and transfer.
 */
public class CachedEnergyNetwork implements EnergyNetwork {
    
    private final EnergyType energyType;
    private final Level level;
    private final Map<WorldPosition, EnergyStorage> storages = new HashMap<>();
    private final List<EnergyStorage> providers = new ArrayList<>();
    private final List<EnergyStorage> consumers = new ArrayList<>();
    
    /**
     * Create a new cached energy network.
     * 
     * @param energyType The type of energy this network handles
     * @param level The level (dimension) this network exists in
     */
    public CachedEnergyNetwork(EnergyType energyType, Level level) {
        this.energyType = energyType;
        this.level = level;
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
        
        // Categorize the storage as provider or consumer
        if (storage.canExtract()) {
            providers.add(storage);
        }
        
        if (storage.canReceive()) {
            consumers.add(storage);
        }
    }
    
    @Override
    public void removeStorage(WorldPosition position) {
        EnergyStorage removed = storages.remove(position);
        
        if (removed != null) {
            if (removed.canExtract()) {
                providers.remove(removed);
            }
            
            if (removed.canReceive()) {
                consumers.remove(removed);
            }
        }
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
     */
    public void tick() {
        if (providers.isEmpty() || consumers.isEmpty()) {
            return; // Nothing to do
        }
        
        // For each provider, try to distribute energy to consumers
        for (EnergyStorage provider : providers) {
            // Calculate how much energy can be extracted from this provider
            int available = provider.extractEnergy(Integer.MAX_VALUE, true);
            
            if (available <= 0) {
                continue; // Skip if no energy available
            }
            
            // Distribute energy evenly among consumers (simple algorithm)
            int totalDistributed = 0;
            int consumersCount = consumers.size();
            
            if (consumersCount > 0) {
                int perConsumer = Math.max(1, available / consumersCount);
                
                for (EnergyStorage consumer : consumers) {
                    if (totalDistributed >= available) {
                        break;
                    }
                    
                    int toTransfer = Math.min(perConsumer, available - totalDistributed);
                    int accepted = consumer.receiveEnergy(toTransfer, true);
                    
                    if (accepted > 0) {
                        // Actually perform the transfer
                        int extracted = provider.extractEnergy(accepted, false);
                        consumer.receiveEnergy(extracted, false);
                        totalDistributed += extracted;
                    }
                }
            }
        }
    }
}