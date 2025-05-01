package com.astroframe.galactic.core.api.common;

/**
 * Abstraction for block face directions.
 * This allows us to avoid direct Minecraft dependencies in our API.
 */
public enum Direction {
    DOWN(0, -1, 0),
    UP(0, 1, 0),
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    EAST(1, 0, 0);
    
    private final int offsetX;
    private final int offsetY;
    private final int offsetZ;
    
    /**
     * Creates a new direction.
     * 
     * @param offsetX The X offset
     * @param offsetY The Y offset
     * @param offsetZ The Z offset
     */
    Direction(int offsetX, int offsetY, int offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }
    
    /**
     * Gets the X offset for this direction.
     * 
     * @return The X offset
     */
    public int getOffsetX() {
        return offsetX;
    }
    
    /**
     * Gets the Y offset for this direction.
     * 
     * @return The Y offset
     */
    public int getOffsetY() {
        return offsetY;
    }
    
    /**
     * Gets the Z offset for this direction.
     * 
     * @return The Z offset
     */
    public int getOffsetZ() {
        return offsetZ;
    }
    
    /**
     * Gets the opposite direction.
     * 
     * @return The opposite direction
     */
    public Direction getOpposite() {
        switch (this) {
            case DOWN: return UP;
            case UP: return DOWN;
            case NORTH: return SOUTH;
            case SOUTH: return NORTH;
            case WEST: return EAST;
            case EAST: return WEST;
            default: throw new IllegalStateException("Unknown direction: " + this);
        }
    }
    
    /**
     * Applies this direction's offset to a position.
     * 
     * @param pos The position
     * @return A new position with the offset applied
     */
    public BlockPos offset(BlockPos pos) {
        return new BlockPos(
            pos.getX() + offsetX,
            pos.getY() + offsetY,
            pos.getZ() + offsetZ
        );
    }
}