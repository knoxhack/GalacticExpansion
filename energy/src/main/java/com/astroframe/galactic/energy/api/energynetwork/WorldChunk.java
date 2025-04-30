package com.astroframe.galactic.energy.api.energynetwork;

/**
 * A wrapper class for chunk positions in the energy network.
 * This provides a layer of abstraction between chunk positions and our energy network implementation.
 */
public class WorldChunk {
    private final int chunkX;
    private final int chunkZ;
    private final Level level;
    
    /**
     * Creates a new WorldChunk with chunk coordinates.
     * 
     * @param chunkX The chunk X coordinate
     * @param chunkZ The chunk Z coordinate
     * @param level The world level
     */
    public WorldChunk(int chunkX, int chunkZ, Level level) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.level = level;
    }
    
    /**
     * Creates a new WorldChunk from a WorldPosition.
     * 
     * @param position The world position
     */
    public WorldChunk(WorldPosition position) {
        this.level = position.getLevel();
        // Convert block coordinates to chunk coordinates (16 blocks per chunk)
        this.chunkX = Math.floorDiv(position.getX(), 16);
        this.chunkZ = Math.floorDiv(position.getZ(), 16);
    }
    
    /**
     * Gets the chunk X coordinate.
     * 
     * @return The chunk X coordinate
     */
    public int getChunkX() {
        return chunkX;
    }
    
    /**
     * Gets the chunk Z coordinate.
     * 
     * @return The chunk Z coordinate
     */
    public int getChunkZ() {
        return chunkZ;
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
     * In this implementation, we always assume chunks are loaded
     * to avoid dependencies on Minecraft.
     * 
     * @return True (always loaded in this implementation)
     */
    public boolean isLoaded() {
        return true;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WorldChunk other = (WorldChunk) obj;
        return chunkX == other.chunkX && chunkZ == other.chunkZ && level.equals(other.level);
    }
    
    @Override
    public int hashCode() {
        int result = 31 * chunkX + chunkZ;
        return 31 * result + level.hashCode();
    }
    
    @Override
    public String toString() {
        return "WorldChunk[" + chunkX + ", " + chunkZ + "]";
    }
}