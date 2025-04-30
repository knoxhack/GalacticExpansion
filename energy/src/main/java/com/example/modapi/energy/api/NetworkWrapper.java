package com.example.modapi.energy.api;

import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.api.energynetwork.Level;
import com.astroframe.galactic.energy.api.energynetwork.WorldPosition;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper for the new energy network implementation.
 * This provides backwards compatibility for code using the old API.
 */
public class NetworkWrapper {
    private final com.astroframe.galactic.energy.api.EnergyNetwork network;
    private final net.minecraft.world.level.Level minecraftLevel;
    private final Map<BlockPos, WorldPosition> positionCache = new HashMap<>();
    
    /**
     * Create a new network wrapper around an existing energy network.
     * 
     * @param network The energy network to wrap
     * @param minecraftLevel The Minecraft level
     */
    public NetworkWrapper(com.astroframe.galactic.energy.api.EnergyNetwork network, net.minecraft.world.level.Level minecraftLevel) {
        this.network = network;
        this.minecraftLevel = minecraftLevel;
    }
    
    /**
     * Add an energy handler to the network at the given position.
     * 
     * @param pos The block position
     * @param handler The energy handler
     */
    public void addHandler(BlockPos pos, IEnergyHandler handler) {
        WorldPosition worldPosition = getOrCreatePosition(pos);
        network.addStorage(worldPosition, new HandlerWrapper(handler));
    }
    
    /**
     * Remove an energy handler from the network at the given position.
     * 
     * @param pos The block position
     */
    public void removeHandler(BlockPos pos) {
        WorldPosition worldPosition = getOrCreatePosition(pos);
        network.removeStorage(worldPosition);
        positionCache.remove(pos);
    }
    
    /**
     * Get or create a WorldPosition for a BlockPos.
     * 
     * @param pos The Minecraft block position
     * @return The equivalent WorldPosition
     */
    private WorldPosition getOrCreatePosition(BlockPos pos) {
        return positionCache.computeIfAbsent(pos, p -> {
            Level level = new Level(minecraftLevel.dimension().location().toString());
            return new WorldPosition(p.getX(), p.getY(), p.getZ(), level);
        });
    }
    
    /**
     * Get the energy network being wrapped.
     * 
     * @return The energy network
     */
    public com.astroframe.galactic.energy.api.EnergyNetwork getNetwork() {
        return network;
    }
    
    /**
     * Get the energy type for this network.
     * 
     * @return The energy type
     */
    public EnergyType getEnergyType() {
        return network.getEnergyType();
    }
    
    /**
     * Wrapper class for IEnergyHandler to adapt to the new EnergyStorage interface.
     */
    private static class HandlerWrapper implements com.astroframe.galactic.energy.api.EnergyStorage {
        private final IEnergyHandler handler;
        
        public HandlerWrapper(IEnergyHandler handler) {
            this.handler = handler;
        }
        
        @Override
        public int receiveEnergy(int amount, boolean simulate) {
            return handler.receiveEnergy(amount, simulate);
        }
        
        @Override
        public int extractEnergy(int amount, boolean simulate) {
            return handler.extractEnergy(amount, simulate);
        }
        
        @Override
        public int getEnergy() {
            return handler.getEnergyStored();
        }
        
        @Override
        public int getMaxEnergy() {
            return handler.getMaxEnergyStored();
        }
        
        @Override
        public boolean canReceive() {
            return handler.canReceive();
        }
        
        @Override
        public boolean canExtract() {
            return handler.canExtract();
        }
        
        @Override
        public EnergyType getEnergyType() {
            // Default to electrical energy type for compatibility
            return EnergyType.ELECTRICAL;
        }
    }
}