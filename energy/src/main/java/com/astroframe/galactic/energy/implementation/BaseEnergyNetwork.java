package com.astroframe.galactic.energy.implementation;

import com.astroframe.galactic.energy.api.EnergyNetwork;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Basic implementation of the EnergyNetwork interface.
 * This provides a simple network for connecting energy storage devices.
 */
public class BaseEnergyNetwork implements EnergyNetwork {
    
    private final EnergyType energyType;
    private final Set<EnergyStorage> storageDevices = new HashSet<>();
    
    /**
     * Create a new energy network for the specified energy type.
     * 
     * @param energyType The type of energy this network handles
     */
    public BaseEnergyNetwork(EnergyType energyType) {
        this.energyType = energyType;
    }
    
    @Override
    public EnergyType getEnergyType() {
        return energyType;
    }
    
    @Override
    public boolean addStorage(EnergyStorage storage) {
        if (storage.getEnergyType() != energyType) {
            return false;
        }
        
        return storageDevices.add(storage);
    }
    
    @Override
    public boolean removeStorage(EnergyStorage storage) {
        return storageDevices.remove(storage);
    }
    
    @Override
    public Collection<EnergyStorage> getStorageDevices() {
        return storageDevices;
    }
    
    @Override
    public int addEnergy(int amount, boolean simulate) {
        // Find storages that can receive energy
        Set<EnergyStorage> receivers = new HashSet<>();
        for (EnergyStorage storage : storageDevices) {
            if (storage.canReceive()) {
                receivers.add(storage);
            }
        }
        
        if (receivers.isEmpty()) {
            return 0;
        }
        
        // Calculate how much energy each storage should receive
        int energyPerStorage = amount / receivers.size();
        int remainder = amount % receivers.size();
        
        // Track how much energy was actually added
        int energyAdded = 0;
        
        // Distribute energy evenly
        for (EnergyStorage storage : receivers) {
            int energyToAdd = energyPerStorage;
            if (remainder > 0) {
                energyToAdd++;
                remainder--;
            }
            
            energyAdded += storage.receiveEnergy(energyToAdd, simulate);
        }
        
        return energyAdded;
    }
    
    @Override
    public int extractEnergy(int amount, boolean simulate) {
        // Find storages that can provide energy
        Set<EnergyStorage> providers = new HashSet<>();
        for (EnergyStorage storage : storageDevices) {
            if (storage.canExtract()) {
                providers.add(storage);
            }
        }
        
        if (providers.isEmpty()) {
            return 0;
        }
        
        // Calculate how much energy each storage should provide
        int energyPerStorage = amount / providers.size();
        int remainder = amount % providers.size();
        
        // Track how much energy was actually extracted
        int energyExtracted = 0;
        
        // Extract energy evenly
        for (EnergyStorage storage : providers) {
            int energyToExtract = energyPerStorage;
            if (remainder > 0) {
                energyToExtract++;
                remainder--;
            }
            
            energyExtracted += storage.extractEnergy(energyToExtract, simulate);
        }
        
        return energyExtracted;
    }
    
    @Override
    public int getEnergy() {
        int totalEnergy = 0;
        
        for (EnergyStorage storage : storageDevices) {
            totalEnergy += storage.getEnergy();
        }
        
        return totalEnergy;
    }
    
    @Override
    public int getMaxEnergy() {
        int totalCapacity = 0;
        
        for (EnergyStorage storage : storageDevices) {
            totalCapacity += storage.getMaxEnergy();
        }
        
        return totalCapacity;
    }
    
    @Override
    public void tick() {
        // In a real implementation, this would handle energy transfer between
        // sources, consumers, and storage based on demands and priorities
        // For this example, we'll just balance energy between storage devices
        balanceEnergy();
    }
    
    /**
     * Balance energy between storage devices.
     * This attempts to distribute energy evenly across all storage devices.
     */
    private void balanceEnergy() {
        if (storageDevices.size() <= 1) {
            return;
        }
        
        // Calculate the target energy level (as a percentage of capacity)
        float totalEnergy = getEnergy();
        float totalCapacity = getMaxEnergy();
        float targetFillLevel = totalCapacity > 0 ? totalEnergy / totalCapacity : 0;
        
        // Balance energy between storage devices
        for (EnergyStorage storage : storageDevices) {
            float currentFillLevel = storage.getFillLevel();
            
            if (Math.abs(currentFillLevel - targetFillLevel) < 0.01f) {
                // Already close enough to the target
                continue;
            }
            
            int targetEnergy = Math.round(storage.getMaxEnergy() * targetFillLevel);
            int currentEnergy = storage.getEnergy();
            int delta = targetEnergy - currentEnergy;
            
            if (delta > 0) {
                // Need to add energy
                storage.receiveEnergy(delta, false);
            } else if (delta < 0) {
                // Need to remove energy
                storage.extractEnergy(-delta, false);
            }
        }
    }
}