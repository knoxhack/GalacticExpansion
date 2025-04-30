package com.example.modapi.machinery.api;

import com.example.modapi.energy.api.IEnergyHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Interface for machines in the ModApi system.
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
}
