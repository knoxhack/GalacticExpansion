package com.example.modapi.energy.api;

/**
 * A simple Level abstraction for the energy API.
 * This provides compatibility with the new energy system.
 * 
 * @deprecated Use {@link com.astroframe.galactic.energy.api.energynetwork.Level} instead
 */
@Deprecated
public class Level {
    private final String dimension;
    
    /**
     * Creates a new Level.
     * 
     * @param dimension The dimension ID
     */
    public Level(String dimension) {
        this.dimension = dimension;
    }
    
    /**
     * Gets the dimension ID.
     * 
     * @return The dimension ID
     */
    public String getDimension() {
        return dimension;
    }
    
    /**
     * Gets a BlockEntity at the given position.
     * This is a simplified version that returns null for now.
     * 
     * @param pos The position
     * @return The BlockEntity, or null if not found
     */
    public Object getBlockEntity(BlockPos pos) {
        // In the old system, this would return a BlockEntity
        // Since we're decoupling from Minecraft, we return null
        return null;
    }
    
    /**
     * Creates a Level from a Minecraft Level.
     * 
     * @param mcLevel The Minecraft Level
     * @return A new Level
     */
    public static Level fromMinecraftLevel(Object mcLevel) {
        try {
            // Use reflection to get the dimension ID
            Class<?> levelClass = mcLevel.getClass();
            Object dimension = levelClass.getMethod("dimension").invoke(mcLevel);
            Object location = dimension.getClass().getMethod("location").invoke(dimension);
            String dimensionId = location.toString();
            
            return new Level(dimensionId);
        } catch (Exception e) {
            // If reflection fails, return a default level
            return new Level("unknown");
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Level other = (Level) obj;
        return dimension.equals(other.dimension);
    }
    
    @Override
    public int hashCode() {
        return dimension.hashCode();
    }
    
    @Override
    public String toString() {
        return "Level[" + dimension + "]";
    }
}