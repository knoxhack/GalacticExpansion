package com.astroframe.galactic.core.api.machine;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Interface for machine recipes.
 * This extends Minecraft's Recipe interface to provide additional functionality
 * specific to machine processing.
 */
public interface IMachineRecipe extends Recipe<Container> {
    
    /**
     * Gets the unique identifier of this recipe.
     * 
     * @return The recipe ID
     */
    ResourceLocation getId();
    
    /**
     * Gets the type of machine recipe.
     * 
     * @return The recipe type
     */
    IMachineRecipeType<?> getType();
    
    /**
     * Gets the processing time in ticks.
     * 
     * @return Processing time in ticks
     */
    int getProcessingTime();
    
    /**
     * Gets the energy consumption per tick.
     * 
     * @return Energy consumption per tick
     */
    int getEnergyPerTick();
    
    /**
     * Gets the total energy required to complete this recipe.
     * 
     * @return Total energy required
     */
    default int getTotalEnergyRequired() {
        return getProcessingTime() * getEnergyPerTick();
    }
    
    /**
     * Gets the result items of this recipe.
     * 
     * @return A list of result item stacks
     */
    List<ItemStack> getResultItems();
    
    /**
     * Gets the chance for secondary outputs to be produced.
     * 
     * @param index The index of the output
     * @return A value between 0.0 and 1.0 representing the chance
     */
    default float getSecondaryOutputChance(int index) {
        return index == 0 ? 1.0F : 0.0F;
    }
    
    /**
     * Checks if this recipe can be processed by the given container.
     * 
     * @param container The container to check
     * @param level The world level
     * @return Whether the recipe can be processed
     */
    boolean matches(Container container, Level level);
    
    /**
     * Gets the experience awarded for completing this recipe.
     * 
     * @return Experience points
     */
    float getExperience();
}