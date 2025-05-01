package com.example.modapi.energy.api;

/**
 * Adapter class to convert between the new and old energy storage interfaces.
 * This allows code using the new API to interact with the old API.
 * 
 * @deprecated Use the new energy API directly
 */
@Deprecated
public class EnergyStorageAdapter implements IEnergyHandler {
    private final com.astroframe.galactic.energy.api.EnergyStorage original;
    
    /**
     * Creates a new adapter for the given energy storage.
     * 
     * @param original The original energy storage
     */
    public EnergyStorageAdapter(com.astroframe.galactic.energy.api.EnergyStorage original) {
        this.original = original;
    }
    
    /**
     * Gets the original energy storage.
     * 
     * @return The original energy storage
     */
    public com.astroframe.galactic.energy.api.EnergyStorage getOriginal() {
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
    public int getEnergyStored() {
        return original.getEnergy();
    }
    
    @Override
    public int getMaxEnergyStored() {
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
    public com.astroframe.galactic.energy.api.EnergyUnit getEnergyUnit() {
        return com.astroframe.galactic.energy.api.EnergyUnit.FORGE_ENERGY;
    }
}