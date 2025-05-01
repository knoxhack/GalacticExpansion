package com.astroframe.galactic.energy.implementation;

import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;

/**
 * Base implementation of the EnergyStorage interface.
 * Provides a simple energy storage capability.
 */
public class BaseEnergyStorage implements EnergyStorage {
    
    private final int capacity;
    private final int maxReceive;
    private final int maxExtract;
    private int energy;
    private final EnergyType energyType;
    
    /**
     * Constructs a new energy storage.
     * 
     * @param capacity The maximum energy that can be stored
     * @param maxReceive The maximum energy that can be received per operation
     * @param maxExtract The maximum energy that can be extracted per operation
     * @param energyType The type of energy this storage handles
     */
    public BaseEnergyStorage(int capacity, int maxReceive, int maxExtract, EnergyType energyType) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = 0;
        this.energyType = energyType;
    }
    
    /**
     * Constructs a new energy storage with an initial energy amount.
     * 
     * @param capacity The maximum energy that can be stored
     * @param maxReceive The maximum energy that can be received per operation
     * @param maxExtract The maximum energy that can be extracted per operation
     * @param energy The initial energy stored
     * @param energyType The type of energy this storage handles
     */
    public BaseEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, EnergyType energyType) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0, Math.min(capacity, energy));
        this.energyType = energyType;
    }
    
    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        if (!canReceive()) {
            return 0;
        }
        
        int energyReceived = Math.min(capacity - energy, Math.min(maxReceive, amount));
        
        if (!simulate) {
            energy += energyReceived;
        }
        
        return energyReceived;
    }
    
    @Override
    public int extractEnergy(int amount, boolean simulate) {
        if (!canExtract()) {
            return 0;
        }
        
        int energyExtracted = Math.min(energy, Math.min(maxExtract, amount));
        
        if (!simulate) {
            energy -= energyExtracted;
        }
        
        return energyExtracted;
    }
    
    @Override
    public int getEnergy() {
        return energy;
    }
    
    @Override
    public int getMaxEnergy() {
        return capacity;
    }
    
    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }
    
    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }
    
    @Override
    public EnergyType getEnergyType() {
        return energyType;
    }
    
    /**
     * Sets the energy stored in this storage.
     * 
     * @param energy The new energy value
     */
    public void setEnergy(int energy) {
        this.energy = Math.max(0, Math.min(capacity, energy));
    }
}