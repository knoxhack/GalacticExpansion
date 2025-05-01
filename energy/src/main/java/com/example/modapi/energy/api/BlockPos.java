package com.example.modapi.energy.api;

/**
 * BlockPos compatibility class for internal use by the legacy energy module.
 * This is a simplified version to provide compatibility with the older energy code.
 * 
 * @deprecated Use WorldPosition from the new energy API instead
 */
@Deprecated
public class BlockPos {
    private final int x;
    private final int y;
    private final int z;
    
    /**
     * Creates a new BlockPos.
     * 
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
     * Gets the X coordinate.
     * 
     * @return The X coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Gets the Y coordinate.
     * 
     * @return The Y coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Gets the Z coordinate.
     * 
     * @return The Z coordinate
     */
    public int getZ() {
        return z;
    }
    
    /**
     * Gets a position that is offset by the given amounts.
     * 
     * @param dx The x offset
     * @param dy The y offset
     * @param dz The z offset
     * @return The offset position
     */
    public BlockPos offset(int dx, int dy, int dz) {
        return new BlockPos(x + dx, y + dy, z + dz);
    }
    
    /**
     * Creates a new BlockPos from a net.minecraft.core.BlockPos
     * 
     * @param mcPos The Minecraft BlockPos
     * @return The new BlockPos
     */
    public static BlockPos fromMinecraftBlockPos(net.minecraft.core.BlockPos mcPos) {
        return new BlockPos(mcPos.getX(), mcPos.getY(), mcPos.getZ());
    }
    
    /**
     * Converts this BlockPos to a net.minecraft.core.BlockPos
     * 
     * @return The Minecraft BlockPos
     */
    public net.minecraft.core.BlockPos toMinecraftBlockPos() {
        return new net.minecraft.core.BlockPos(x, y, z);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BlockPos other = (BlockPos) obj;
        return x == other.x && y == other.y && z == other.z;
    }
    
    @Override
    public int hashCode() {
        int result = 31 * x + y;
        return 31 * result + z;
    }
    
    @Override
    public String toString() {
        return "BlockPos[" + x + ", " + y + ", " + z + "]";
    }
}