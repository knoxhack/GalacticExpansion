package com.astroframe.galactic.energy;

import net.minecraft.core.BlockPos;

/**
 * Alias class for BlockPos to help with import issues.
 * This is a temporary workaround.
 */
public class BlockPosAlias {
    public final BlockPos pos;
    
    public BlockPosAlias(BlockPos pos) {
        this.pos = pos;
    }
    
    public static BlockPosAlias of(BlockPos pos) {
        return new BlockPosAlias(pos);
    }
    
    /**
     * Converts back to a standard BlockPos.
     * 
     * @return The BlockPos
     */
    public BlockPos toBlockPos() {
        return pos;
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