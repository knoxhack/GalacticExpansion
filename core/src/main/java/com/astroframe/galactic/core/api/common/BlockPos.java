package com.astroframe.galactic.core.api.common;

/**
 * Simple wrapper around a 3D position in the world.
 * This abstraction allows us to avoid direct Minecraft dependencies in our API.
 */
public class BlockPos {
    private final int x;
    private final int y;
    private final int z;
    
    /**
     * Creates a new block position.
     * 
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     */
    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Gets the X coordinate.
     * @return The X coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Gets the Y coordinate.
     * @return The Y coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Gets the Z coordinate.
     * @return The Z coordinate
     */
    public int getZ() {
        return z;
    }
    
    /**
     * Creates a new position offset from this one.
     * 
     * @param dx X offset
     * @param dy Y offset
     * @param dz Z offset
     * @return A new position
     */
    public BlockPos offset(int dx, int dy, int dz) {
        return new BlockPos(x + dx, y + dy, z + dz);
    }
    
    /**
     * Gets the distance to another position.
     * 
     * @param other The other position
     * @return The distance
     */
    public double getDistance(BlockPos other) {
        double dx = x - other.x;
        double dy = y - other.y;
        double dz = z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /**
     * Gets the squared distance to another position.
     * This is more efficient than getDistance() for comparisons.
     * 
     * @param other The other position
     * @return The squared distance
     */
    public double getDistanceSq(BlockPos other) {
        double dx = x - other.x;
        double dy = y - other.y;
        double dz = z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }
    
    /**
     * Gets the Manhattan distance to another position.
     * This is the sum of the absolute differences of their coordinates.
     * 
     * @param other The other position
     * @return The Manhattan distance
     */
    public int getManhattanDistance(BlockPos other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BlockPos other = (BlockPos) obj;
        return x == other.x && y == other.y && z == other.z;
    }
    
    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
    
    @Override
    public String toString() {
        return "BlockPos{x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}