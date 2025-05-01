package com.astroframe.galactic.machinery.implementation;

import com.astroframe.galactic.core.api.energy.IEnergyHandler;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.machinery.api.Machine;
import com.astroframe.galactic.machinery.api.MachineType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Base implementation of the Machine interface.
 * This provides common functionality for all machines.
 */
public abstract class BaseMachine implements Machine {
    
    protected final String name;
    protected final MachineType type;
    protected final EnergyStorage energyStorage;
    protected final List<EnergyType> acceptableEnergyTypes;
    protected final float efficiency;
    
    protected boolean active;
    protected float progress;
    protected int processingTime;
    protected int energyPerTick;
    
    /**
     * Construct a new base machine.
     * 
     * @param name The name of the machine
     * @param type The type of machine
     * @param energyStorage The energy storage for this machine (can be null if not using energy)
     * @param efficiency The efficiency of this machine (0.0 to 1.0)
     * @param processingTime The base time (in ticks) it takes to complete one operation
     * @param energyPerTick The amount of energy consumed per tick while active
     */
    public BaseMachine(String name, MachineType type, EnergyStorage energyStorage, 
                      float efficiency, int processingTime, int energyPerTick) {
        this.name = name;
        this.type = type;
        this.energyStorage = energyStorage;
        this.efficiency = Math.max(0.1f, Math.min(1.0f, efficiency)); // Clamp between 0.1 and 1.0
        this.active = false;
        this.progress = 0.0f;
        this.processingTime = Math.max(1, processingTime);
        this.energyPerTick = Math.max(0, energyPerTick);
        
        // Initialize acceptable energy types based on the provided energy storage
        this.acceptableEnergyTypes = new ArrayList<>();
        if (energyStorage != null) {
            this.acceptableEnergyTypes.add(energyStorage.getEnergyType());
        }
    }
    
    /**
     * Construct a new base machine without energy capabilities.
     * 
     * @param name The name of the machine
     * @param type The type of machine
     * @param efficiency The efficiency of this machine (0.0 to 1.0)
     * @param processingTime The base time (in ticks) it takes to complete one operation
     */
    public BaseMachine(String name, MachineType type, float efficiency, int processingTime) {
        this(name, type, null, efficiency, processingTime, 0);
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getMachineId() {
        // Use a sanitized version of the name as the ID
        return name.toLowerCase().replace(' ', '_');
    }
    
    @Override
    public String getMachineName() {
        return name;
    }
    
    @Override
    public int getMachineTier() {
        return 1; // Default tier
    }
    
    // These methods are not from the Machine interface, removing @Override annotations
    // to fix compilation errors
    public Optional<EnergyStorage> getEnergyStorage() {
        return Optional.ofNullable(energyStorage);
    }
    
    public List<EnergyType> getAcceptableEnergyTypes() {
        return Collections.unmodifiableList(acceptableEnergyTypes);
    }
    
    @Override
    public void tick(Level level, BlockPos pos) {
        if (!active) {
            return;
        }
        
        // Check if we need energy and have enough
        if (energyStorage != null && energyPerTick > 0) {
            int energyExtracted = energyStorage.extractEnergy(
                    Math.round(energyPerTick / efficiency), false);
            
            if (energyExtracted < Math.round(energyPerTick / efficiency)) {
                // Not enough energy, pause processing
                return;
            }
        }
        
        // Update progress
        float progressPerTick = 1.0f / (processingTime / efficiency);
        progress += progressPerTick;
        
        // Check if processing is complete
        if (progress >= 1.0f) {
            completeProcessing();
            progress = 0.0f;
            
            // Check if we should continue processing
            if (!canContinueProcessing()) {
                stop();
            }
        }
    }
    
    // Legacy method for backward compatibility with subclasses
    public void tick() {
        // This is now a default implementation that should be overridden by subclasses
        // to provide proper level and position context
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    @Override
    public boolean start() {
        if (active) {
            return false;
        }
        
        if (!canStartProcessing()) {
            return false;
        }
        
        active = true;
        progress = 0.0f;
        return true;
    }
    
    @Override
    public boolean stop() {
        if (!active) {
            return false;
        }
        
        active = false;
        return true;
    }
    
    @Override
    public float getProgress() {
        return progress;
    }
    
    @Override
    public float getEfficiency() {
        return efficiency;
    }
    
    @Override
    public com.astroframe.galactic.core.api.energy.IEnergyHandler.EnergyUnit getEnergyUnit() {
        return com.astroframe.galactic.core.api.energy.IEnergyHandler.EnergyUnit.GALACTIC_ENERGY_UNIT;
    }
    
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (energyStorage == null) return 0;
        return energyStorage.receiveEnergy(maxReceive, simulate);
    }
    
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (energyStorage == null) return 0;
        return energyStorage.extractEnergy(maxExtract, simulate);
    }
    
    @Override
    public int getEnergyStored() {
        if (energyStorage == null) return 0;
        return energyStorage.getEnergy();
    }
    
    @Override
    public int getMaxEnergyCapacity() {
        if (energyStorage == null) return 0;
        return energyStorage.getMaxEnergy();
    }
    
    @Override
    public boolean canExtract() {
        if (energyStorage == null) return false;
        return energyStorage.canExtract();
    }
    
    @Override
    public boolean canReceive() {
        if (energyStorage == null) return false;
        return energyStorage.canReceive();
    }
    
    /**
     * Get the type of this machine.
     * 
     * @return The machine type
     */
    public MachineType getType() {
        return type;
    }
    
    /**
     * Check if the machine can start processing.
     * This should be overridden by subclasses to implement specific logic.
     * 
     * @return true if processing can start, false otherwise
     */
    protected abstract boolean canStartProcessing();
    
    /**
     * Complete the current processing operation.
     * This should be overridden by subclasses to implement specific logic.
     */
    protected abstract void completeProcessing();
    
    /**
     * Check if the machine can continue processing after completing a cycle.
     * This should be overridden by subclasses to implement specific logic.
     * 
     * @return true if processing should continue, false otherwise
     */
    protected abstract boolean canContinueProcessing();
}