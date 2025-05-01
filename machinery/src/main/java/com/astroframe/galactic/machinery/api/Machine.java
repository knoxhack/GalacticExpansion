package com.astroframe.galactic.machinery.api;

import com.astroframe.galactic.core.api.energy.IEnergyHandler;
// Update imports to use correct Minecraft and NeoForge paths for 1.21.5
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Interface for machines in the Galactic Expansion mod.
 * Defines common functionality for all machines.
 */
public interface Machine extends IEnergyHandler {
    
    /**
     * Gets the machine's unique identifier.
     * 
     * @return The machine ID
     */
    String getMachineId();
    
    /**
     * Gets the display name of the machine.
     * 
     * @return The machine name
     */
    String getMachineName();
    
    /**
     * Gets the tier of the machine.
     * 
     * @return The machine tier
     */
    int getMachineTier();
    
    /**
     * Called every tick to update the machine.
     * 
     * @param level The world
     * @param pos The machine position
     */
    void tick(Level level, BlockPos pos);
    
    /**
     * Gets whether the machine is currently active.
     * 
     * @return True if the machine is active
     */
    boolean isActive();
    
    /**
     * Gets the maximum energy input rate.
     * 
     * @return The max input
     */
    int getMaxEnergyInput();
    
    /**
     * Gets the maximum energy output rate.
     * 
     * @return The max output
     */
    int getMaxEnergyOutput();
    
    /**
     * Gets the energy consumption per tick.
     * 
     * @return The energy consumption
     */
    int getEnergyConsumption();
    
    /**
     * Whether the machine has enough energy to operate.
     * 
     * @return True if the machine has sufficient energy
     */
    default boolean hasSufficientEnergy() {
        return getEnergyStored() >= getEnergyConsumption();
    }
    
    /**
     * Consume energy for machine operation.
     * 
     * @return True if energy was successfully consumed
     */
    default boolean consumeEnergy() {
        if (hasSufficientEnergy()) {
            extractEnergy(getEnergyConsumption(), false);
            return true;
        }
        return false;
    }
    
    /**
     * Gets the name of the machine.
     * 
     * @return The machine name
     */
    String getName();
    
    /**
     * Gets the progress of the current operation.
     * 
     * @return The progress from 0.0 to 1.0
     */
    float getProgress();
    
    /**
     * Gets the efficiency of the machine.
     * 
     * @return The efficiency factor
     */
    float getEfficiency();
    
    /**
     * Start the machine's operation.
     * 
     * @return True if the machine was started successfully
     */
    boolean start();
    
    /**
     * Stop the machine's operation.
     * 
     * @return True if the machine was stopped successfully
     */
    boolean stop();
}