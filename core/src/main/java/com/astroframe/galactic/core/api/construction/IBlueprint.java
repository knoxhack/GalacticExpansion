package com.astroframe.galactic.core.api.construction;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Interface for construction blueprints.
 * Blueprints define structures that can be built in the world.
 */
public interface IBlueprint {
    
    /**
     * Gets the unique identifier for this blueprint.
     * 
     * @return The blueprint UUID
     */
    UUID getBlueprintId();
    
    /**
     * Gets the blueprint's name.
     * 
     * @return The name
     */
    String getName();
    
    /**
     * Sets the blueprint's name.
     * 
     * @param name The new name
     */
    void setName(String name);
    
    /**
     * Gets the creator of this blueprint.
     * 
     * @return The creator's name
     */
    String getCreator();
    
    /**
     * Sets the creator of this blueprint.
     * 
     * @param creator The creator's name
     */
    void setCreator(String creator);
    
    /**
     * Gets the category of this blueprint.
     * 
     * @return The category
     */
    String getCategory();
    
    /**
     * Sets the category of this blueprint.
     * 
     * @param category The new category
     */
    void setCategory(String category);
    
    /**
     * Gets all blocks in this blueprint.
     * 
     * @return A map of relative positions to block states
     */
    Map<BlockPos, BlockState> getBlocks();
    
    /**
     * Adds a block to this blueprint.
     * 
     * @param relativePos The relative position
     * @param state The block state
     * @param nbt Optional NBT data for the block
     */
    void addBlock(BlockPos relativePos, BlockState state, CompoundTag nbt);
    
    /**
     * Removes a block from this blueprint.
     * 
     * @param relativePos The relative position
     * @return Whether the block was removed
     */
    boolean removeBlock(BlockPos relativePos);
    
    /**
     * Gets the NBT data for a block in this blueprint.
     * 
     * @param relativePos The relative position
     * @return The NBT data, or null if none
     */
    CompoundTag getBlockNBT(BlockPos relativePos);
    
    /**
     * Gets the dimensions of this blueprint.
     * 
     * @return An array of [width, height, depth]
     */
    int[] getDimensions();
    
    /**
     * Gets the materials required to build this blueprint.
     * 
     * @return A list of required materials
     */
    List<ItemStack> getRequiredMaterials();
    
    /**
     * Projects a holographic preview of this blueprint in the world.
     * 
     * @param level The world level
     * @param origin The origin position
     * @param rotation The rotation in degrees (0, 90, 180, 270)
     * @param mirror Whether to mirror the blueprint
     * @return Whether the projection was successful
     */
    boolean projectHologram(Level level, BlockPos origin, int rotation, boolean mirror);
    
    /**
     * Builds this blueprint in the world.
     * 
     * @param level The world level
     * @param origin The origin position
     * @param rotation The rotation in degrees (0, 90, 180, 270)
     * @param mirror Whether to mirror the blueprint
     * @param ignoreObstructions Whether to ignore obstructing blocks
     * @return Whether the building was successful
     */
    boolean build(Level level, BlockPos origin, int rotation, boolean mirror, boolean ignoreObstructions);
    
    /**
     * Checks if this blueprint can be built at the specified location.
     * 
     * @param level The world level
     * @param origin The origin position
     * @param rotation The rotation in degrees (0, 90, 180, 270)
     * @param mirror Whether to mirror the blueprint
     * @return A list of error messages, or an empty list if buildable
     */
    List<String> canBuildAt(Level level, BlockPos origin, int rotation, boolean mirror);
    
    /**
     * Saves this blueprint to NBT.
     * 
     * @param tag The tag to save to
     * @return The updated tag
     */
    CompoundTag save(CompoundTag tag);
    
    /**
     * Loads a blueprint from NBT.
     * 
     * @param tag The tag to load from
     */
    void load(CompoundTag tag);
    
    /**
     * Creates a copy of this blueprint.
     * 
     * @return A new instance with the same data
     */
    IBlueprint copy();
    
    /**
     * Exports this blueprint to a shareable format.
     * 
     * @return A string representation of the blueprint
     */
    String exportToString();
    
    /**
     * Gets an icon representing this blueprint.
     * 
     * @return The icon resource location
     */
    ResourceLocation getIcon();
}