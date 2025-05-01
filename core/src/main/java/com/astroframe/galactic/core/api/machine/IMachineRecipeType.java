package com.astroframe.galactic.core.api.machine;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Interface for machine recipe types.
 * Acts as a custom recipe type system for machines in the mod.
 * 
 * @param <T> The specific type of machine recipe
 */
public interface IMachineRecipeType<T extends IMachineRecipe> {
    
    /**
     * Gets the unique identifier for this recipe type.
     * 
     * @return The recipe type ID
     */
    ResourceLocation getId();
    
    /**
     * Gets the display name for this recipe type.
     * 
     * @return The display name
     */
    String getDisplayName();
    
    /**
     * Checks if the given container can be used for this recipe type.
     * 
     * @param container The container to check
     * @return Whether the container is valid for this recipe type
     */
    boolean isValidContainer(Container container);
    
    /**
     * Get the minimum number of input slots required for this recipe type.
     * 
     * @return The minimum number of input slots
     */
    int getMinInputSlots();
    
    /**
     * Get the minimum number of output slots required for this recipe type.
     * 
     * @return The minimum number of output slots
     */
    int getMinOutputSlots();
    
    /**
     * Creates a new recipe builder for this type.
     * 
     * @return A new recipe builder
     */
    default IMachineRecipeBuilder<T> builder() {
        throw new UnsupportedOperationException("This recipe type does not support builders");
    }
    
    /**
     * Interface for building machine recipes.
     * 
     * @param <T> The specific type of machine recipe
     */
    interface IMachineRecipeBuilder<T extends IMachineRecipe> {
        
        /**
         * Builds the recipe with the current configuration.
         * 
         * @return The built recipe
         */
        T build();
    }
}