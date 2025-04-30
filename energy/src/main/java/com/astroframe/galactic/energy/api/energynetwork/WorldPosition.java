package com.astroframe.galactic.energy.api.energynetwork;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * A wrapper class for block positions in the energy network.
 * This provides a layer of abstraction between Minecraft's BlockPos and our energy network implementation.
 */
public class WorldPosition {
    private final BlockPos blockPos;
    private final Level level;
    
    /**
     * Creates a new WorldPosition.
     * 
     * @param blockPos The Minecraft BlockPos
     * @param level The Minecraft Level
     */
    public WorldPosition(BlockPos blockPos, Level level) {
        this.blockPos = blockPos;
        this.level = level;
    }
    
    /**
     * Gets the wrapped BlockPos.
     * 
     * @return The BlockPos
     */
    public BlockPos getBlockPos() {
        return blockPos;
    }
    
    /**
     * Gets the Level.
     * 
     * @return The Level
     */
    public Level getLevel() {
        return level;
    }
    
    /**
     * Gets the X coordinate.
     * 
     * @return The X coordinate
     */
    public int getX() {
        return blockPos.getX();
    }
    
    /**
     * Gets the Y coordinate.
     * 
     * @return The Y coordinate
     */
    public int getY() {
        return blockPos.getY();
    }
    
    /**
     * Gets the Z coordinate.
     * 
     * @return The Z coordinate
     */
    public int getZ() {
        return blockPos.getZ();
    }
    
    /**
     * Gets a position that is offset by the given amounts.
     * 
     * @param x The x offset
     * @param y The y offset
     * @param z The z offset
     * @return The offset position
     */
    public WorldPosition offset(int x, int y, int z) {
        return new WorldPosition(blockPos.offset(x, y, z), level);
    }
    
    /**
     * Gets the position above this position.
     * 
     * @return The position above
     */
    public WorldPosition above() {
        return new WorldPosition(blockPos.above(), level);
    }
    
    /**
     * Gets the position below this position.
     * 
     * @return The position below
     */
    public WorldPosition below() {
        return new WorldPosition(blockPos.below(), level);
    }
    
    /**
     * Gets the position to the north of this position.
     * 
     * @return The position to the north
     */
    public WorldPosition north() {
        return new WorldPosition(blockPos.north(), level);
    }
    
    /**
     * Gets the position to the south of this position.
     * 
     * @return The position to the south
     */
    public WorldPosition south() {
        return new WorldPosition(blockPos.south(), level);
    }
    
    /**
     * Gets the position to the east of this position.
     * 
     * @return The position to the east
     */
    public WorldPosition east() {
        return new WorldPosition(blockPos.east(), level);
    }
    
    /**
     * Gets the position to the west of this position.
     * 
     * @return The position to the west
     */
    public WorldPosition west() {
        return new WorldPosition(blockPos.west(), level);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WorldPosition other = (WorldPosition) obj;
        return blockPos.equals(other.blockPos) && level.equals(other.level);
    }
    
    @Override
    public int hashCode() {
        return 31 * blockPos.hashCode() + level.hashCode();
    }
    
    @Override
    public String toString() {
        return "WorldPosition[" + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ() + "]";
    }
}