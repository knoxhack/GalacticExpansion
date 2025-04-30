package com.astroframe.galactic.energy.implementation;

import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;

/**
 * Base implementation of the EnergyStorage interface.
 * This provides a simple energy storage capability.
 */
public class BaseEnergyStorage implements EnergyStorage {
    
    protected int energy;
    protected final int capacity;
    protected final int maxReceive;
    protected final int maxExtract;
    protected final boolean canReceive;
    protected final boolean canExtract;
    protected final EnergyType energyType;
    
    /**
     * Create a new energy storage with the specified parameters.
     * 
     * @param capacity The maximum amount of energy that can be stored
     * @param maxReceive The maximum amount of energy that can be received per operation
     * @param maxExtract The maximum amount of energy that can be extracted per operation
     * @param energy The initial amount of energy
     * @param energyType The type of energy this storage can handle
     */
    public BaseEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, EnergyType energyType) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0, Math.min(capacity, energy));
        this.canReceive = maxReceive > 0;
        this.canExtract = maxExtract > 0;
        this.energyType = energyType;
    }
    
    /**
     * Create a new empty energy storage with the specified parameters.
     * 
     * @param capacity The maximum amount of energy that can be stored
     * @param maxReceive The maximum amount of energy that can be received per operation
     * @param maxExtract The maximum amount of energy that can be extracted per operation
     * @param energyType The type of energy this storage can handle
     */
    public BaseEnergyStorage(int capacity, int maxReceive, int maxExtract, EnergyType energyType) {
        this(capacity, maxReceive, maxExtract, 0, energyType);
    }
    
    /**
     * Create a new energy storage with default parameters.
     * 
     * @param capacity The maximum amount of energy that can be stored
     * @param energyType The type of energy this storage can handle
     */
    public BaseEnergyStorage(int capacity, EnergyType energyType) {
        this(capacity, capacity, capacity, 0, energyType);
    }
    
    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        if (!canReceive) {
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
        if (!canExtract) {
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
        return canExtract && energy > 0;
    }
    
    @Override
    public boolean canReceive() {
        return canReceive && energy < capacity;
    }
    
    @Override
    public EnergyType getEnergyType() {
        return energyType;
    }
    
    /**
     * Set the amount of stored energy.
     * This is intended for internal use only.
     * 
     * @param energy The new energy amount
     */
    public void setEnergy(int energy) {
        this.energy = Math.max(0, Math.min(capacity, energy));
    }
}