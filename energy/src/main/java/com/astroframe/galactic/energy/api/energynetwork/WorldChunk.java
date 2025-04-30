package com.astroframe.galactic.energy.api.energynetwork;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

/**
 * A wrapper class for chunk positions in the energy network.
 * This provides a layer of abstraction between Minecraft's ChunkPos and our energy network implementation.
 */
public class WorldChunk {
    private final ChunkPos chunkPos;
    private final Level level;
    
    /**
     * Creates a new WorldChunk.
     * 
     * @param chunkPos The Minecraft ChunkPos
     * @param level The Minecraft Level
     */
    public WorldChunk(ChunkPos chunkPos, Level level) {
        this.chunkPos = chunkPos;
        this.level = level;
    }
    
    /**
     * Creates a new WorldChunk from a WorldPosition.
     * 
     * @param position The world position
     */
    public WorldChunk(WorldPosition position) {
        this.level = position.getLevel();
        this.chunkPos = new ChunkPos(position.getBlockPos());
    }
    
    /**
     * Gets the wrapped ChunkPos.
     * 
     * @return The ChunkPos
     */
    public ChunkPos getChunkPos() {
        return chunkPos;
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
     * Checks if this chunk is loaded.
     * 
     * @return True if the chunk is loaded
     */
    public boolean isLoaded() {
        return level.hasChunk(chunkPos.x, chunkPos.z);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WorldChunk other = (WorldChunk) obj;
        return chunkPos.equals(other.chunkPos) && level.equals(other.level);
    }
    
    @Override
    public int hashCode() {
        return 31 * chunkPos.hashCode() + level.hashCode();
    }
    
    @Override
    public String toString() {
        return "WorldChunk[" + chunkPos.x + ", " + chunkPos.z + "]";
    }
}