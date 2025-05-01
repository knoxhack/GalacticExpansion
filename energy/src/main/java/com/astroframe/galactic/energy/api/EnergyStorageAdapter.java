package com.astroframe.galactic.energy.api;

// Import the IEnergyHandler interface directly from core
import com.astroframe.galactic.core.api.energy.IEnergyHandler;
import com.astroframe.galactic.core.api.energy.IEnergyHandler.EnergyUnit;

/**
 * Adapter class to convert between the core API energy handler and the energy module's storage interfaces.
 * This allows code using the core API to interact with the energy module's storage implementation.
 */
public class EnergyStorageAdapter implements EnergyStorage {
    private final IEnergyHandler original;
    
    /**
     * Creates a new adapter for the given energy handler.
     * 
     * @param original The original energy handler from the core API
     */
    public EnergyStorageAdapter(IEnergyHandler original) {
        this.original = original;
    }
    
    /**
     * Gets the original energy handler.
     * 
     * @return The original energy handler
     */
    public IEnergyHandler getOriginal() {
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
        // Use a default type based on the energy unit
        // In the future we can implement a proper mapping between units and types
        switch (original.getEnergyUnit()) {
            default:
                return EnergyType.ELECTRICAL;
        }
    }
}