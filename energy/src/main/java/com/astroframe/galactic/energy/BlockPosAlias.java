package com.astroframe.galactic.energy;

/**
 * Alias class for BlockPos to help with import issues.
 * This is a temporary workaround.
 */
public class BlockPosAlias {
    public final Object pos;
    
    public BlockPosAlias(Object pos) {
        this.pos = pos;
    }
    
    public static BlockPosAlias of(Object pos) {
        return new BlockPosAlias(pos);
    }
    
    /**
     * Converts back to a standard BlockPos.
     * 
     * @return The BlockPos
     */
    public Object toBlockPos() {
        return pos;
    }
    
    /**
     * Gets the X coordinate.
     * 
     * @return The X coordinate
     */
    public int getX() {
        try {
            Class<?> blockPosClass = Class.forName("net.minecraft.core.BlockPos");
            java.lang.reflect.Method getXMethod = blockPosClass.getMethod("getX");
            return (int) getXMethod.invoke(pos);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get X coordinate", e);
        }
    }
    
    /**
     * Gets the Y coordinate.
     * 
     * @return The Y coordinate
     */
    public int getY() {
        try {
            Class<?> blockPosClass = Class.forName("net.minecraft.core.BlockPos");
            java.lang.reflect.Method getYMethod = blockPosClass.getMethod("getY");
            return (int) getYMethod.invoke(pos);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Y coordinate", e);
        }
    }
    
    /**
     * Gets the Z coordinate.
     * 
     * @return The Z coordinate
     */
    public int getZ() {
        try {
            Class<?> blockPosClass = Class.forName("net.minecraft.core.BlockPos");
            java.lang.reflect.Method getZMethod = blockPosClass.getMethod("getZ");
            return (int) getZMethod.invoke(pos);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get Z coordinate", e);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BlockPosAlias other = (BlockPosAlias) obj;
        return pos.equals(other.pos);
    }
    
    @Override
    public int hashCode() {
        return pos.hashCode();
    }
}