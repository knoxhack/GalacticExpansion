package com.example.modapi.energy.api;

import com.example.modapi.energy.ModApiEnergy;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

/**
 * Capability provider for energy functionality.
 * Handles capability registration and access.
 */
public class EnergyCapability {
    
    /**
     * Resource location for the energy capability.
     */
    public static final ResourceLocation ENERGY_CAPABILITY = ResourceLocation.fromNamespaceAndPath(ModApiEnergy.MOD_ID, "energy");
    
    /**
     * A capability that provides energy handling.
     */
    public static final BlockCapability<IEnergyHandler, Direction> ENERGY = BlockCapability.create(ENERGY_CAPABILITY, IEnergyHandler.class, Direction.class);
    
    /**
     * Register the energy capability.
     */
    public static void register() {
        ModApiEnergy.LOGGER.info("Registering energy capability");
    }
    
    /**
     * Register the energy capability with the Forge capability system.
     * 
     * @param event The capability registration event
     */
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        ModApiEnergy.LOGGER.info("Registering energy capability adapter");
        
        // Register the basic energy capability adapter
        event.registerBlockCapability(ENERGY, IEnergyHandler.class, (level, pos, state, be, side) -> {
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
    public static class EnergyStorage extends com.example.modapi.energy.api.EnergyStorage {
        /**
         * Constructor for the energy storage capability.
         * 
         * @param capacity The maximum capacity
         * @param maxReceive The maximum input rate
         * @param maxExtract The maximum output rate
         * @param energy The initial energy
         */
        public EnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
            super(capacity, maxReceive, maxExtract, energy, EnergyUnit.FORGE_ENERGY);
        }
        
        /**
         * Reads the energy storage from NBT.
         * 
         * @param nbt The NBT tag
         */
        public void readNBT(CompoundTag nbt) {
            deserializeNBT(nbt);
        }
        
        /**
         * Writes the energy storage to NBT.
         * 
         * @return The NBT tag
         */
        public CompoundTag writeNBT() {
            return serializeNBT(new CompoundTag());
        }
    }
}
