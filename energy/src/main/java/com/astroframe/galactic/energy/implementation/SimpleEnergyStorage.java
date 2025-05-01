package com.astroframe.galactic.energy.implementation;

import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.api.EnergyUnit;
import com.astroframe.galactic.energy.nbt.CompoundTag;

/**
 * A simple implementation of the EnergyStorage interface.
 * Provides basic energy storage functionality.
 */
public class SimpleEnergyStorage implements EnergyStorage {
    
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;
    protected EnergyType energyType;
    
    /**
     * Creates a new SimpleEnergyStorage with the given parameters.
     * 
     * @param capacity The maximum capacity
     * @param maxReceive The maximum receive rate
     * @param maxExtract The maximum extract rate
     * @param energy The initial energy
     * @param energyType The energy type
     */
    public SimpleEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, EnergyType energyType) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0, Math.min(capacity, energy));
        this.energyType = energyType;
    }
    
    /**
     * Creates a new SimpleEnergyStorage with default parameters.
     * 
     * @param capacity The maximum capacity
     */
    public SimpleEnergyStorage(int capacity) {
        this(capacity, capacity, capacity, 0, EnergyType.ELECTRICAL);
    }
    
    /**
     * Creates a new SimpleEnergyStorage with the given capacity and energy type.
     * 
     * @param capacity The maximum capacity
     * @param energyType The energy type
     */
    public SimpleEnergyStorage(int capacity, EnergyType energyType) {
        this(capacity, capacity, capacity, 0, energyType);
    }
    
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive()) {
            return 0;
        }
        
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        
        if (!simulate) {
            energy += energyReceived;
        }
        
        return energyReceived;
    }
    
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract()) {
            return 0;
        }
        
        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        
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
     * Sets the current energy stored.
     * 
     * @param energy The energy to set
     */
    public void setEnergy(int energy) {
        this.energy = Math.max(0, Math.min(capacity, energy));
    }
    
    /**
     * Sets the maximum capacity.
     * 
     * @param capacity The capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
        
        if (energy > capacity) {
            energy = capacity;
        }
    }
    
    /**
     * Sets the maximum receive rate.
     * 
     * @param maxReceive The maximum receive rate
     */
    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }
    
    /**
     * Sets the maximum extract rate.
     * 
     * @param maxExtract The maximum extract rate
     */
    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }
    
    /**
     * Sets the energy type.
     * 
     * @param energyType The energy type
     */
    public void setEnergyType(EnergyType energyType) {
        this.energyType = energyType;
    }
    
    /**
     * Saves this energy storage to an NBT tag.
     * 
     * @param tag The tag to save to
     * @return The tag
     */
    public CompoundTag serializeNBT(CompoundTag tag) {
        tag.putInt("Energy", energy);
        tag.putInt("Capacity", capacity);
        tag.putInt("MaxReceive", maxReceive);
        tag.putInt("MaxExtract", maxExtract);
        tag.putString("EnergyType", energyType.getId());
        return tag;
    }
    
    /**
     * Loads this energy storage from an NBT tag.
     * 
     * @param tag The tag to load from
     */
    public void deserializeNBT(CompoundTag tag) {
        energy = tag.getInt("Energy");
        capacity = tag.getInt("Capacity");
        maxReceive = tag.getInt("MaxReceive");
        maxExtract = tag.getInt("MaxExtract");
        energyType = EnergyType.byId(tag.getString("EnergyType"));
    }
}