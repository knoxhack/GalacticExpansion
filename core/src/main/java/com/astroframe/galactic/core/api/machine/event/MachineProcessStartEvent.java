package com.astroframe.galactic.core.api.machine.event;

import com.astroframe.galactic.core.api.machine.IMachineRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.Event;

/**
 * Event fired when a machine starts processing a recipe.
 * This can be used to modify the process before it begins.
 */
public class MachineProcessStartEvent extends Event {
    
    private final BlockEntity machine;
    private final Level level;
    private final BlockPos pos;
    private final IMachineRecipe recipe;
    private int processingTime;
    private int energyPerTick;
    private boolean canceled;
    
    /**
     * Creates a new MachineProcessStartEvent.
     * 
     * @param machine The machine block entity
     * @param level The world level
     * @param pos The position of the machine
     * @param recipe The recipe being processed
     * @param processingTime The initial processing time
     * @param energyPerTick The initial energy consumption per tick
     */
    public MachineProcessStartEvent(BlockEntity machine, Level level, BlockPos pos, IMachineRecipe recipe, 
                                   int processingTime, int energyPerTick) {
        this.machine = machine;
        this.level = level;
        this.pos = pos;
        this.recipe = recipe;
        this.processingTime = processingTime;
        this.energyPerTick = energyPerTick;
        this.canceled = false;
    }
    
    /**
     * Gets the machine block entity.
     * 
     * @return The machine block entity
     */
    public BlockEntity getMachine() {
        return machine;
    }
    
    /**
     * Gets the world level.
     * 
     * @return The world level
     */
    public Level getLevel() {
        return level;
    }
    
    /**
     * Gets the position of the machine.
     * 
     * @return The machine position
     */
    public BlockPos getPos() {
        return pos;
    }
    
    /**
     * Gets the recipe being processed.
     * 
     * @return The recipe
     */
    public IMachineRecipe getRecipe() {
        return recipe;
    }
    
    /**
     * Gets the processing time.
     * 
     * @return Processing time in ticks
     */
    public int getProcessingTime() {
        return processingTime;
    }
    
    /**
     * Sets the processing time.
     * 
     * @param processingTime New processing time in ticks
     */
    public void setProcessingTime(int processingTime) {
        this.processingTime = Math.max(1, processingTime);
    }
    
    /**
     * Gets the energy consumption per tick.
     * 
     * @return Energy per tick
     */
    public int getEnergyPerTick() {
        return energyPerTick;
    }
    
    /**
     * Sets the energy consumption per tick.
     * 
     * @param energyPerTick New energy per tick
     */
    public void setEnergyPerTick(int energyPerTick) {
        this.energyPerTick = Math.max(0, energyPerTick);
    }
    
    /**
     * Gets whether the process has been canceled.
     * 
     * @return Whether the process is canceled
     */
    public boolean isCanceled() {
        return canceled;
    }
    
    /**
     * Sets whether the process should be canceled.
     * 
     * @param canceled Whether to cancel the process
     */
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
    
    @Override
    public boolean isCancelable() {
        return true;
    }
}