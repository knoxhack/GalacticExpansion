package com.astroframe.galactic.core.api.construction;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for managing blueprints.
 * This provides methods to register, retrieve, and manipulate blueprints.
 */
public interface IBlueprintManager {
    
    /**
     * Registers a blueprint with the manager.
     * 
     * @param blueprint The blueprint to register
     * @return Whether the registration was successful
     */
    boolean registerBlueprint(IBlueprint blueprint);
    
    /**
     * Unregisters a blueprint from the manager.
     * 
     * @param blueprintId The ID of the blueprint to unregister
     * @return Whether the unregistration was successful
     */
    boolean unregisterBlueprint(UUID blueprintId);
    
    /**
     * Gets a blueprint by its ID.
     * 
     * @param blueprintId The blueprint ID
     * @return The blueprint, or empty if not found
     */
    Optional<IBlueprint> getBlueprint(UUID blueprintId);
    
    /**
     * Gets a blueprint from an item stack.
     * 
     * @param stack The item stack, which should be a blueprint item
     * @return The blueprint, or empty if the item doesn't contain a blueprint
     */
    Optional<IBlueprint> getBlueprintFromItem(ItemStack stack);
    
    /**
     * Creates a new empty blueprint.
     * 
     * @param name The name for the new blueprint
     * @param creator The creator of the blueprint
     * @return The new blueprint
     */
    IBlueprint createBlueprint(String name, String creator);
    
    /**
     * Creates a blueprint from a schematic file.
     * 
     * @param schematicLocation The location of the schematic file
     * @param name The name for the new blueprint
     * @param creator The creator of the blueprint
     * @return The new blueprint, or empty if creation failed
     */
    Optional<IBlueprint> createBlueprintFromSchematic(ResourceLocation schematicLocation, String name, String creator);
    
    /**
     * Gets all registered blueprints.
     * 
     * @return A collection of all blueprints
     */
    Collection<IBlueprint> getAllBlueprints();
    
    /**
     * Gets all blueprints in a specific category.
     * 
     * @param category The category to filter by
     * @return A list of blueprints in the category
     */
    List<IBlueprint> getBlueprintsByCategory(String category);
    
    /**
     * Gets all blueprints by a specific creator.
     * 
     * @param creator The creator to filter by
     * @return A list of blueprints by the creator
     */
    List<IBlueprint> getBlueprintsByCreator(String creator);
    
    /**
     * Searches for blueprints by name.
     * 
     * @param query The search query
     * @return A list of blueprints matching the query
     */
    List<IBlueprint> searchBlueprints(String query);
    
    /**
     * Creates an item stack containing a blueprint.
     * 
     * @param blueprint The blueprint to store in the item
     * @return The item stack containing the blueprint
     */
    ItemStack createBlueprintItem(IBlueprint blueprint);
    
    /**
     * Gets all available blueprint categories.
     * 
     * @return A list of all categories
     */
    List<String> getCategories();
    
    /**
     * Imports a blueprint from a string representation.
     * 
     * @param blueprintString The string representation
     * @return The imported blueprint, or empty if import failed
     */
    Optional<IBlueprint> importBlueprint(String blueprintString);
    
    /**
     * Saves all blueprints to storage.
     * 
     * @return Whether the save was successful
     */
    boolean saveAllBlueprints();
    
    /**
     * Loads all blueprints from storage.
     * 
     * @return Whether the load was successful
     */
    boolean loadAllBlueprints();
}