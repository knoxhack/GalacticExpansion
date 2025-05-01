package com.astroframe.galactic.core.api.machine.event;

import com.astroframe.galactic.core.api.machine.IMachineRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Event fired when a machine completes processing a recipe.
 * This can be used to modify the results of processing.
 */
public class MachineProcessCompleteEvent extends Event {
    
    private final BlockEntity machine;
    private final Level level;
    private final BlockPos pos;
    private final IMachineRecipe recipe;
    private final List<ItemStack> results;
    private float experience;
    
    /**
     * Creates a new MachineProcessCompleteEvent.
     * 
     * @param machine The machine block entity
     * @param level The world level
     * @param pos The position of the machine
     * @param recipe The recipe that was processed
     * @param results The initial results of processing
     * @param experience The initial experience to award
     */
    public MachineProcessCompleteEvent(BlockEntity machine, Level level, BlockPos pos, IMachineRecipe recipe,
                                     List<ItemStack> results, float experience) {
        this.machine = machine;
        this.level = level;
        this.pos = pos;
        this.recipe = recipe;
        this.results = new ArrayList<>(results);
        this.experience = experience;
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
     * Gets the recipe that was processed.
     * 
     * @return The recipe
     */
    public IMachineRecipe getRecipe() {
        return recipe;
    }
    
    /**
     * Gets the results of processing.
     * 
     * @return The result items
     */
    public List<ItemStack> getResults() {
        return results;
    }
    
    /**
     * Adds a result item to the output.
     * 
     * @param stack The item stack to add
     */
    public void addResult(ItemStack stack) {
        if (!stack.isEmpty()) {
            results.add(stack.copy());
        }
    }
    
    /**
     * Removes a result item from the output.
     * 
     * @param index The index of the item to remove
     * @return The removed item stack, or an empty stack if the index was invalid
     */
    public ItemStack removeResult(int index) {
        if (index >= 0 && index < results.size()) {
            return results.remove(index);
        }
        return ItemStack.EMPTY;
    }
    
    /**
     * Gets the experience to award.
     * 
     * @return Experience points
     */
    public float getExperience() {
        return experience;
    }
    
    /**
     * Sets the experience to award.
     * 
     * @param experience New experience points
     */
    public void setExperience(float experience) {
        this.experience = Math.max(0, experience);
    }
}