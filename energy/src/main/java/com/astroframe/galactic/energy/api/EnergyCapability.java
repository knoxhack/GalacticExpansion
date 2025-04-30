package com.astroframe.galactic.energy.api;

import com.astroframe.galactic.energy.GalacticEnergy;
import org.slf4j.Logger;

/**
 * Capability provider for energy functionality.
 * Handles capability registration and access.
 */
public class EnergyCapability {
    
    /**
     * Capability ID string for the energy capability.
     */
    public static final String ENERGY_CAPABILITY_ID = GalacticEnergy.MOD_ID + ":energy";
    
    /**
     * Register the energy capability.
     */
    public static void register() {
        GalacticEnergy.LOGGER.info("Registering energy capability");
    }
    
    /**
     * Register the energy capability with the system.
     * This will be called during mod initialization.
     */
    public static void registerCapabilities() {
        GalacticEnergy.LOGGER.info("Registering energy capability adapter");
    }
    
    /**
     * Helper class for attaching energy storage to block entities.
     */
    public static class EnergyStorage implements com.astroframe.galactic.energy.api.EnergyStorage {
        private final int capacity;
        private final int maxReceive;
        private final int maxExtract;
        private int energy;
        private final EnergyType energyType;
        
        /**
         * Constructor for the energy storage capability.
         * 
         * @param capacity The maximum capacity
         * @param maxReceive The maximum input rate
         * @param maxExtract The maximum output rate
         * @param energy The initial energy
         */
        public EnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
            this.capacity = capacity;
            this.maxReceive = maxReceive;
            this.maxExtract = maxExtract;
            this.energy = Math.max(0, Math.min(capacity, energy));
            this.energyType = EnergyType.ELECTRICAL; // Default to electrical
        }
        
        /**
         * Reads the energy storage from serialized data.
         * 
         * @param energy The energy value
         */
        public void deserialize(int energy) {
            this.energy = Math.max(0, Math.min(capacity, energy));
        }
        
        /**
         * Writes the energy storage to serialized data.
         * 
         * @return The energy value
         */
        public int serialize() {
            return this.energy;
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
    }
}