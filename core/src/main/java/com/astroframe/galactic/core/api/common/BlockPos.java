package com.astroframe.galactic.core.api.common;

/**
 * Abstraction for block position to avoid direct dependency on Minecraft classes.
 * This provides position handling for logistics systems.
 */
public class BlockPos {
    private final int x;
    private final int y;
    private final int z;
    
    /**
     * Creates a new block position.
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     */
    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Creates a new BlockPos from a Minecraft BlockPos.
     * @param pos The Minecraft BlockPos
     * @return A new abstracted BlockPos
     */
    public static BlockPos fromMinecraft(net.minecraft.core.BlockPos pos) {
        return new BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }
    
    /**
     * Converts to a Minecraft BlockPos.
     * @return The equivalent Minecraft BlockPos
     */
    public net.minecraft.core.BlockPos toMinecraft() {
        return new net.minecraft.core.BlockPos(x, y, z);
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
     * Returns a new BlockPos offset by the given values.
     * @param dx X offset
     * @param dy Y offset
     * @param dz Z offset
     * @return A new BlockPos with the offset applied
     */
    public BlockPos offset(int dx, int dy, int dz) {
        return new BlockPos(x + dx, y + dy, z + dz);
    }
    
    /**
     * Returns a new BlockPos offset in the given direction.
     * @param direction The direction to offset in
     * @return A new BlockPos with the offset applied
     */
    public BlockPos offset(Direction direction) {
        return switch (direction) {
            case UP -> offset(0, 1, 0);
            case DOWN -> offset(0, -1, 0);
            case NORTH -> offset(0, 0, -1);
            case SOUTH -> offset(0, 0, 1);
            case EAST -> offset(1, 0, 0);
            case WEST -> offset(-1, 0, 0);
        };
    }
    
    /**
     * Gets the Manhattan distance to another BlockPos.
     * @param other The other BlockPos
     * @return The Manhattan distance
     */
    public int distanceTo(BlockPos other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        BlockPos blockPos = (BlockPos) o;
        
        if (x != blockPos.x) return false;
        if (y != blockPos.y) return false;
        return z == blockPos.z;
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
        return "BlockPos{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}