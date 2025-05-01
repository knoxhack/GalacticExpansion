package com.astroframe.galactic.core.api.machine;

import com.astroframe.galactic.core.GalacticCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manager for machine recipes.
 * Provides methods to register and retrieve machine recipes.
 */
public class MachineRecipeManager {
    
    private static final Map<ResourceLocation, IMachineRecipeType<?>> RECIPE_TYPES = new HashMap<>();
    private static final Map<ResourceLocation, List<IMachineRecipe>> RECIPES = new HashMap<>();
    
    /**
     * Registers a new machine recipe type.
     * 
     * @param recipeType The recipe type to register
     */
    public static void registerRecipeType(IMachineRecipeType<?> recipeType) {
        if (recipeType == null) {
            GalacticCore.LOGGER.error("Cannot register null recipe type");
            return;
        }
        
        ResourceLocation id = recipeType.getId();
        if (RECIPE_TYPES.containsKey(id)) {
            GalacticCore.LOGGER.warn("Recipe type {} is already registered, skipping", id);
            return;
        }
        
        RECIPE_TYPES.put(id, recipeType);
        RECIPES.put(id, new ArrayList<>());
        GalacticCore.LOGGER.info("Registered machine recipe type: {}", id);
    }
    
    /**
     * Registers a new machine recipe.
     * 
     * @param recipe The recipe to register
     */
    public static void registerRecipe(IMachineRecipe recipe) {
        if (recipe == null) {
            GalacticCore.LOGGER.error("Cannot register null recipe");
            return;
        }
        
        IMachineRecipeType<?> type = recipe.getType();
        if (type == null) {
            GalacticCore.LOGGER.error("Recipe has null type: {}", recipe.getId());
            return;
        }
        
        ResourceLocation typeId = type.getId();
        if (!RECIPE_TYPES.containsKey(typeId)) {
            GalacticCore.LOGGER.error("Recipe type {} is not registered", typeId);
            return;
        }
        
        List<IMachineRecipe> recipes = RECIPES.get(typeId);
        recipes.add(recipe);
        GalacticCore.LOGGER.info("Registered machine recipe: {}", recipe.getId());
    }
    
    /**
     * Gets a registered recipe type by its ID.
     * 
     * @param id The recipe type ID
     * @return The recipe type, or null if not found
     */
    public static IMachineRecipeType<?> getRecipeType(ResourceLocation id) {
        return RECIPE_TYPES.get(id);
    }
    
    /**
     * Gets all registered recipe types.
     * 
     * @return A collection of all registered recipe types
     */
    public static Collection<IMachineRecipeType<?>> getAllRecipeTypes() {
        return Collections.unmodifiableCollection(RECIPE_TYPES.values());
    }
    
    /**
     * Gets all recipes of a specific type.
     * 
     * @param <T> The recipe type
     * @param level The level (not used in this implementation but kept for API compatibility)
     * @param recipeType The recipe type to get
     * @return A list of all recipes of the specified type
     */
    @SuppressWarnings("unchecked")
    public static <T extends IMachineRecipe> List<T> getRecipesForType(Level level, IMachineRecipeType<T> recipeType) {
        if (recipeType == null) {
            return Collections.emptyList();
        }
        
        List<IMachineRecipe> recipes = RECIPES.get(recipeType.getId());
        if (recipes == null) {
            return Collections.emptyList();
        }
        
        return (List<T>) Collections.unmodifiableList(recipes);
    }
    
    /**
     * Gets a matching recipe for the specified container and recipe type.
     * 
     * @param <T> The recipe type
     * @param container The container to match
     * @param level The level to get recipes from
     * @param recipeType The recipe type to match
     * @return The matching recipe, or null if no match found
     */
    public static <T extends IMachineRecipe> Optional<T> getMatchingRecipe(Container container, Level level, IMachineRecipeType<T> recipeType) {
        if (container == null || level == null || recipeType == null) {
            return Optional.empty();
        }
        
        if (!recipeType.isValidContainer(container)) {
            return Optional.empty();
        }
        
        List<T> recipes = getRecipesForType(level, recipeType);
        return recipes.stream()
                .filter(recipe -> recipe.matches(container, level))
                .findFirst();
    }
    
    /**
     * Gets all recipe types that are valid for the given container.
     * 
     * @param container The container to check
     * @return A list of valid recipe types
     */
    public static List<IMachineRecipeType<?>> getValidRecipeTypesForContainer(Container container) {
        return RECIPE_TYPES.values().stream()
                .filter(type -> type.isValidContainer(container))
                .collect(Collectors.toList());
    }
}