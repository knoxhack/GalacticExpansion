package com.astroframe.galactic.core.api.construction;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Predicate;

/**
 * Interface for structure scanners that can create blueprints from existing structures.
 */
public interface IStructureScanner {
    
    /**
     * Scans a structure in the world and creates a blueprint.
     * 
     * @param level The world level
     * @param player The player performing the scan
     * @param startPos The starting position of the scan
     * @param endPos The ending position of the scan
     * @param blueprintName The name for the created blueprint
     * @return The scanned blueprint, or null if scanning failed
     */
    IBlueprint scanStructure(Level level, Player player, BlockPos startPos, BlockPos endPos, String blueprintName);
    
    /**
     * Sets the block predicate to determine which blocks should be included in the scan.
     * 
     * @param predicate The block predicate
     * @return This scanner, for method chaining
     */
    IStructureScanner setBlockFilter(Predicate<BlockPos> predicate);
    
    /**
     * Includes blocks with NBT data in the scan.
     * 
     * @param includeNBT Whether to include NBT data
     * @return This scanner, for method chaining
     */
    IStructureScanner includeBlockNBT(boolean includeNBT);
    
    /**
     * Includes air blocks in the scan.
     * 
     * @param includeAir Whether to include air blocks
     * @return This scanner, for method chaining
     */
    IStructureScanner includeAirBlocks(boolean includeAir);
    
    /**
     * Sets whether to use relative positions in the blueprint.
     * 
     * @param useRelative Whether to use relative positions
     * @return This scanner, for method chaining
     */
    IStructureScanner useRelativePositions(boolean useRelative);
    
    /**
     * Sets the origin corner for relative positions.
     * 
     * @param originCorner The origin corner (0 = min/min/min, 1 = min/min/max, etc.)
     * @return This scanner, for method chaining
     */
    IStructureScanner setOriginCorner(int originCorner);
    
    /**
     * Sets the maximum size (in blocks) that can be scanned.
     * 
     * @param maxSize The maximum size
     * @return This scanner, for method chaining
     */
    IStructureScanner setMaxSize(int maxSize);
    
    /**
     * Adds a callback to be notified when scanning starts.
     * 
     * @param callback The callback
     * @return This scanner, for method chaining
     */
    IStructureScanner onScanStart(Runnable callback);
    
    /**
     * Adds a callback to be notified when scanning completes.
     * 
     * @param callback The callback
     * @return This scanner, for method chaining
     */
    IStructureScanner onScanComplete(Runnable callback);
    
    /**
     * Gets the scan progress as a value between 0 and 1.
     * 
     * @return The scan progress
     */
    float getScanProgress();
    
    /**
     * Checks if scanning is currently in progress.
     * 
     * @return Whether scanning is in progress
     */
    boolean isScanning();
    
    /**
     * Cancels the current scan if one is in progress.
     */
    void cancelScan();
    
    /**
     * Gets a list of error messages from the last scan attempt.
     * 
     * @return A list of error messages
     */
    List<String> getScanErrors();
}