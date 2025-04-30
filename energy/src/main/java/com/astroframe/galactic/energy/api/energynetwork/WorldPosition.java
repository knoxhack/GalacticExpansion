package com.astroframe.galactic.energy.api.energynetwork;

/**
 * A position in the world used by the energy network system.
 * This is a Minecraft-independent implementation that can be adapted to different block position systems.
 */
public class WorldPosition {
    private final int x;
    private final int y;
    private final int z;
    private final Level level;
    
    /**
     * Creates a new WorldPosition.
     * 
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @param level The level (dimension)
     */
    public WorldPosition(int x, int y, int z, Level level) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.level = level;
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
    public WorldPosition offset(int dx, int dy, int dz) {
        return new WorldPosition(x + dx, y + dy, z + dz, level);
    }
    
    /**
     * Gets the position above this position.
     * 
     * @return The position above
     */
    public WorldPosition above() {
        return offset(0, 1, 0);
    }
    
    /**
     * Gets the position below this position.
     * 
     * @return The position below
     */
    public WorldPosition below() {
        return offset(0, -1, 0);
    }
    
    /**
     * Gets the position to the north of this position.
     * 
     * @return The position to the north
     */
    public WorldPosition north() {
        return offset(0, 0, -1);
    }
    
    /**
     * Gets the position to the south of this position.
     * 
     * @return The position to the south
     */
    public WorldPosition south() {
        return offset(0, 0, 1);
    }
    
    /**
     * Gets the position to the east of this position.
     * 
     * @return The position to the east
     */
    public WorldPosition east() {
        return offset(1, 0, 0);
    }
    
    /**
     * Gets the position to the west of this position.
     * 
     * @return The position to the west
     */
    public WorldPosition west() {
        return offset(-1, 0, 0);
    }
    
    /**
     * Gets the distance to another position.
     * 
     * @param other The other position
     * @return The distance
     */
    public double distanceTo(WorldPosition other) {
        if (!level.equals(other.level)) {
            return Double.POSITIVE_INFINITY; // Can't measure distance between different dimensions
        }
        
        int dx = x - other.x;
        int dy = y - other.y;
        int dz = z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /**
     * Gets the Manhattan distance to another position.
     * 
     * @param other The other position
     * @return The Manhattan distance
     */
    public int manhattanDistanceTo(WorldPosition other) {
        if (!level.equals(other.level)) {
            return Integer.MAX_VALUE; // Can't measure distance between different dimensions
        }
        
        return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WorldPosition other = (WorldPosition) obj;
        return x == other.x && y == other.y && z == other.z && level.equals(other.level);
    }
    
    @Override
    public int hashCode() {
        int result = 31 * x + y;
        result = 31 * result + z;
        return 31 * result + level.hashCode();
    }
    
    @Override
    public String toString() {
        return "WorldPosition[" + x + ", " + y + ", " + z + "]";
    }
}