package com.example.modapi.energy.api;

import com.astroframe.galactic.energy.api.EnergyCapability as GalacticEnergyCapability;
import com.astroframe.galactic.energy.api.IEnergyHandler;
import com.example.modapi.energy.ModApiEnergy;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

/**
 * Compatibility class for the energy capability.
 * This class exists only for backward compatibility with existing code.
 * All new code should use GalacticEnergyCapability directly.
 * 
 * @deprecated Use {@link com.astroframe.galactic.energy.api.EnergyCapability} instead
 */
@Deprecated
public class EnergyCapability {
    
    /**
     * Resource location for the energy capability.
     */
    public static final ResourceLocation ENERGY_CAPABILITY = GalacticEnergyCapability.ENERGY_CAPABILITY;
    
    /**
     * A capability that provides energy handling.
     */
    public static final net.neoforged.neoforge.capabilities.BlockCapability<IEnergyHandler, Direction> ENERGY = 
        GalacticEnergyCapability.ENERGY;
    
    /**
     * Register the energy capability.
     * 
     * @deprecated Use {@link com.astroframe.galactic.energy.api.EnergyCapability#register()} instead
     */
    @Deprecated
    public static void register() {
        ModApiEnergy.LOGGER.warn("EnergyCapability.register() is deprecated, use GalacticEnergyCapability.register() instead");
        GalacticEnergyCapability.register();
    }
    
    /**
     * Compatibility wrapper for the energy storage class.
     * 
     * @deprecated Use {@link com.astroframe.galactic.energy.api.EnergyCapability.EnergyStorage} instead
     */
    @Deprecated
    public static class EnergyStorage implements com.astroframe.galactic.energy.api.EnergyStorage {
        private final com.astroframe.galactic.energy.api.EnergyCapability.EnergyStorage delegate;
        
        /**
         * Constructor for the energy storage capability.
         * 
         * @param capacity The maximum capacity
         * @param maxReceive The maximum input rate
         * @param maxExtract The maximum output rate
         * @param energy The initial energy
         */
        public EnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
            this.delegate = new com.astroframe.galactic.energy.api.EnergyCapability.EnergyStorage(
                capacity, maxReceive, maxExtract, energy);
        }

        @Override
        public int receiveEnergy(int amount, boolean simulate) {
            return delegate.receiveEnergy(amount, simulate);
        }

        @Override
        public int extractEnergy(int amount, boolean simulate) {
            return delegate.extractEnergy(amount, simulate);
        }

        @Override
        public int getEnergy() {
            return delegate.getEnergy();
        }

        @Override
        public int getMaxEnergy() {
            return delegate.getMaxEnergy();
        }

        @Override
        public boolean canExtract() {
            return delegate.canExtract();
        }

        @Override
        public boolean canReceive() {
            return delegate.canReceive();
        }

        @Override
        public com.astroframe.galactic.energy.api.EnergyType getEnergyType() {
            return delegate.getEnergyType();
        }
    }
}
