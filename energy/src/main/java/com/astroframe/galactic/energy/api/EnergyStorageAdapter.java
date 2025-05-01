package com.astroframe.galactic.energy.api;

/**
 * Adapter class to convert between the old and new energy storage interfaces.
 * This allows code using the old API to interact with the new API.
 */
public class EnergyStorageAdapter implements EnergyStorage {
    private final com.example.modapi.energy.api.EnergyStorage original;
    
    /**
     * Creates a new adapter for the given energy storage.
     * 
     * @param original The original energy storage
     */
    public EnergyStorageAdapter(com.example.modapi.energy.api.EnergyStorage original) {
        this.original = original;
    }
    
    /**
     * Gets the original energy storage.
     * 
     * @return The original energy storage
     */
    public com.example.modapi.energy.api.EnergyStorage getOriginal() {
        return original;
    }
    
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return original.receiveEnergy(maxReceive, simulate);
    }
    
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return original.extractEnergy(maxExtract, simulate);
    }
    
    @Override
    public int getEnergy() {
        return original.getEnergy();
    }
    
    @Override
    public int getMaxEnergy() {
        return original.getMaxEnergy();
    }
    
    @Override
    public boolean canExtract() {
        return original.canExtract();
    }
    
    @Override
    public boolean canReceive() {
        return original.canReceive();
    }
    
    @Override
    public EnergyType getEnergyType() {
        // Convert from old to new energy type
        return EnergyType.byId(original.getEnergyType().getId());
    }
}