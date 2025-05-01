package com.astroframe.galactic.core.api.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Interface for multiblock structures.
 * Multiblock structures are formed by connecting multiple blocks together to create
 * a larger functional structure.
 */
public interface IMultiblockStructure {
    
    /**
     * Gets the unique identifier for this multiblock structure.
     * 
     * @return The structure UUID
     */
    UUID getStructureId();
    
    /**
     * Gets the type of this multiblock structure.
     * 
     * @return The structure type
     */
    String getStructureType();
    
    /**
     * Gets the controller position of this multiblock structure.
     * 
     * @return The controller position
     */
    BlockPos getControllerPos();
    
    /**
     * Gets all blocks that are part of this structure.
     * 
     * @return A map of positions to block states
     */
    Map<BlockPos, BlockState> getStructureBlocks();
    
    /**
     * Gets all positions that are part of this structure.
     * 
     * @return A set of all block positions in the structure
     */
    Set<BlockPos> getPositions();
    
    /**
     * Checks if a position is part of this structure.
     * 
     * @param pos The position to check
     * @return Whether the position is part of this structure
     */
    boolean contains(BlockPos pos);
    
    /**
     * Gets the minimum corner of this structure's bounding box.
     * 
     * @return The minimum corner position
     */
    BlockPos getMinCorner();
    
    /**
     * Gets the maximum corner of this structure's bounding box.
     * 
     * @return The maximum corner position
     */
    BlockPos getMaxCorner();
    
    /**
     * Gets the size of this structure in blocks.
     * 
     * @return The structure size (number of blocks)
     */
    int getSize();
    
    /**
     * Gets the facing direction of this structure.
     * 
     * @return The structure's facing direction
     */
    Direction getFacing();
    
    /**
     * Checks if this structure is valid.
     * 
     * @param level The world level
     * @return Whether the structure is valid
     */
    boolean isValid(Level level);
    
    /**
     * Attempts to validate the structure.
     * 
     * @param level The world level
     * @return Whether the validation was successful
     */
    boolean validate(Level level);
    
    /**
     * Disassembles the structure, notifying all component blocks.
     * 
     * @param level The world level
     */
    void disassemble(Level level);
    
    /**
     * Updates all components of the structure.
     * 
     * @param level The world level
     */
    void updateComponents(Level level);
    
    /**
     * Get the master component for this structure.
     * 
     * @return The master component
     */
    IMultiblockComponent getMasterComponent();
    
    /**
     * Sets the master component for this structure.
     * 
     * @param component The master component
     */
    void setMasterComponent(IMultiblockComponent component);
}