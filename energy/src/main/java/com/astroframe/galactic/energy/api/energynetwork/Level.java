package com.astroframe.galactic.energy.api.energynetwork;

/**
 * Represents a level or dimension in the world.
 * This is a simplified version for the energy API that doesn't depend on Minecraft.
 */
public class Level {
    private final String dimensionId;
    
    /**
     * Create a new level.
     * 
     * @param dimensionId The dimension ID string
     */
    public Level(String dimensionId) {
        this.dimensionId = dimensionId;
    }
    
    /**
     * Get the dimension ID for this level.
     * 
     * @return The dimension ID
     */
    public String getDimensionId() {
        return dimensionId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Level level = (Level) o;
        return dimensionId.equals(level.dimensionId);
    }
    
    @Override
    public int hashCode() {
        return dimensionId.hashCode();
    }
    
    @Override
    public String toString() {
        return "Level{" +
                "dimensionId='" + dimensionId + '\'' +
                '}';
    }
}