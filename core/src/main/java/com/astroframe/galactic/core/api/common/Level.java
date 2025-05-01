package com.astroframe.galactic.core.api.common;

/**
 * Abstraction for a game world level.
 * This allows us to avoid direct Minecraft dependencies in our API.
 */
public class Level {
    private final String dimensionId;
    private final boolean isRemote;
    
    /**
     * Creates a new level.
     * 
     * @param dimensionId The dimension identifier
     * @param isRemote Whether this level is remote (client-side)
     */
    public Level(String dimensionId, boolean isRemote) {
        this.dimensionId = dimensionId;
        this.isRemote = isRemote;
    }
    
    /**
     * Gets the dimension identifier.
     * 
     * @return The dimension identifier
     */
    public String getDimensionId() {
        return dimensionId;
    }
    
    /**
     * Checks if this level is remote (client-side).
     * 
     * @return true if this level is remote
     */
    public boolean isRemote() {
        return isRemote;
    }
    
    /**
     * Gets the block state at the given position.
     * This would normally return a BlockState, but for our API it's just a placeholder.
     * 
     * @param pos The position
     * @return A placeholder for the block state
     */
    public Object getBlockState(BlockPos pos) {
        // This is just a placeholder method
        return null;
    }
    
    /**
     * Checks if the block at the given position is loaded.
     * 
     * @param pos The position
     * @return true if the block is loaded
     */
    public boolean isBlockLoaded(BlockPos pos) {
        // This is just a placeholder method
        return true;
    }
    
    /**
     * Gets the current game time.
     * 
     * @return The game time
     */
    public long getGameTime() {
        return System.currentTimeMillis(); // Just a placeholder implementation
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Level other = (Level) obj;
        return isRemote == other.isRemote && dimensionId.equals(other.dimensionId);
    }
    
    @Override
    public int hashCode() {
        int result = dimensionId.hashCode();
        result = 31 * result + (isRemote ? 1 : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "Level{" +
               "dimensionId='" + dimensionId + '\'' +
               ", isRemote=" + isRemote +
               '}';
    }
}