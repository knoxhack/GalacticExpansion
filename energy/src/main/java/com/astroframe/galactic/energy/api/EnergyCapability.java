package com.astroframe.galactic.energy.api;

import com.astroframe.galactic.energy.GalacticEnergy;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

/**
 * Capability provider for energy functionality.
 * Handles capability registration and access.
 */
public class EnergyCapability {
    
    /**
     * Resource location for the energy capability.
     */
    public static final ResourceLocation ENERGY_CAPABILITY = new ResourceLocation(GalacticEnergy.MOD_ID, "energy");
    
    /**
     * A capability that provides energy handling.
     */
    public static final BlockCapability<IEnergyHandler, Direction> ENERGY = BlockCapability.create(ENERGY_CAPABILITY, IEnergyHandler.class, Direction.class);
    
    /**
     * Register the energy capability.
     */
    public static void register() {
        GalacticEnergy.LOGGER.info("Registering energy capability");
    }
    
    /**
     * Register the energy capability with the NeoForge capability system.
     * 
     * @param event The capability registration event
     */
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        GalacticEnergy.LOGGER.info("Registering energy capability adapter");
        
        // Register the basic energy capability adapter
        event.registerBlock(ENERGY, (level, pos, state, be, side) -> {
            if (be instanceof IEnergyHandler energyHandler) {
                // Only provide the capability for the appropriate side
                return energyHandler;
            }
            return null;
        });
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
         * Reads the energy storage from NBT.
         * 
         * @param nbt The NBT tag
         */
        public void readNBT(CompoundTag nbt) {
            this.energy = nbt.getInt("Energy");
        }
        
        /**
         * Writes the energy storage to NBT.
         * 
         * @return The NBT tag
         */
        public CompoundTag writeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Energy", this.energy);
            return tag;
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