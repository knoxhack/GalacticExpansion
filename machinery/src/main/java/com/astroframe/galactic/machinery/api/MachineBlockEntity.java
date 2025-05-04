package com.astroframe.galactic.machinery.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Base interface for all machine block entities in the mod.
 * This provides a common API for machine functionality.
 */
public abstract class MachineBlockEntity extends BlockEntity {
    
    // Energy values 
    protected int currentEnergy = 0;
    protected boolean isActive = false;
    protected int processingTime = 0;
    
    /**
     * Constructor for a MachineBlockEntity
     * 
     * @param type The BlockEntityType
     * @param pos The position
     * @param state The block state
     */
    public MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    /**
     * Called when the block is right-clicked.
     * 
     * @param state The block state
     * @param level The level
     * @param pos The position
     * @param player The player
     * @param hand The hand used
     * @param hit The hit result
     * @return The interaction result
     */
    public abstract InteractionResult onBlockActivated(BlockState state, Level level, BlockPos pos, 
            Player player, net.minecraft.world.InteractionHand hand, BlockHitResult hit);
            
    /**
     * Updates the block entity every tick on the server side.
     * 
     * @param level The level
     * @param pos The position  
     * @param state The block state
     */
    public abstract void serverTick(Level level, BlockPos pos, BlockState state);
    
    /**
     * Process an item in this machine.
     * This is the main processing logic for the machine.
     */
    public abstract void processItem();
    
    /**
     * Gets the default processing time for this machine.
     * 
     * @return The default processing time in ticks
     */
    public abstract int getDefaultProcessingTime();
    
    /**
     * Gets the size of the inventory.
     * 
     * @return The inventory size
     */
    public abstract int getInventorySize();
    
    /**
     * Gets the maximum energy this machine can store.
     * 
     * @return The maximum energy
     */
    public abstract int getMaxEnergy();
    
    /**
     * Gets the maximum energy input rate.
     * 
     * @return The maximum energy input rate
     */
    public abstract int getMaxEnergyInput();
    
    /**
     * Gets the maximum energy output rate.
     * 
     * @return The maximum energy output rate
     */
    public abstract int getMaxEnergyOutput();
    
    /**
     * Gets the energy consumption rate per tick.
     * 
     * @return The energy consumption rate
     */
    public abstract int getEnergyConsumption();
    
    /**
     * Gets the type of this machine.
     * 
     * @return The machine type
     */
    public abstract MachineType getMachineType();
    
    /**
     * Checks if the machine can process its current inputs.
     * 
     * @return True if the machine can process
     */
    public abstract boolean canProcess();
    
    /**
     * Stops the machine's processing.
     * 
     * @return True if the machine was stopped successfully
     */
    public abstract boolean stop();
    
    /**
     * Starts the machine's operation.
     * 
     * @return True if the machine was started successfully
     */
    public abstract boolean start();
    
    /**
     * Gets the efficiency of the machine.
     * Higher efficiency means better performance and lower energy usage.
     * 
     * @return The efficiency factor from 0.0 to 1.0
     */
    public abstract float getEfficiency();
    
    /**
     * Gets the display name of the machine.
     * Used for UI elements and tooltips.
     * 
     * @return The localized name of this machine
     */
    public abstract String getName();
    
    /**
     * Gets the tier level of this machine.
     * Higher tier machines have better performance and efficiency.
     * 
     * @return The machine tier (1-3)
     */
    public abstract int getMachineTier();
    
    /**
     * Gets the machine's internal name identifier.
     * Used for data storage and lookup.
     * 
     * @return The machine's internal name
     */
    public abstract String getMachineName();
    
    /**
     * Gets the unique identifier for this machine type.
     * Used for registry lookups and serialization.
     * 
     * @return The machine's registry ID
     */
    public abstract String getMachineId();
    
    /**
     * Checks if the machine has enough energy to operate.
     * 
     * @return True if the machine has sufficient energy
     */
    public boolean hasSufficientEnergy() {
        return currentEnergy >= getEnergyConsumption();
    }
    
    /**
     * Gets the current energy level.
     * 
     * @return The current energy level
     */
    public int getCurrentEnergy() {
        return currentEnergy;
    }
    
    /**
     * Sets the current energy level.
     * 
     * @param energy The new energy level
     */
    public void setCurrentEnergy(int energy) {
        this.currentEnergy = Math.min(energy, getMaxEnergy());
    }
    
    /**
     * Consumes energy for processing.
     * 
     * @return True if energy was successfully consumed
     */
    public boolean consumeEnergy() {
        if (hasSufficientEnergy()) {
            currentEnergy -= getEnergyConsumption();
            return true;
        }
        return false;
    }
    
    /**
     * Checks if the machine is currently active.
     * 
     * @return True if the machine is active
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Sets the active state of the machine.
     * 
     * @param active The new active state
     */
    public void setActive(boolean active) {
        isActive = active;
    }
}