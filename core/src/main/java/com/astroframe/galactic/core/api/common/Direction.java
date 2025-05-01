package com.astroframe.galactic.core.api.common;

/**
 * Abstraction for representing cardinal directions, similar to Minecraft's Direction.
 * This avoids direct dependency on Minecraft classes.
 */
public enum Direction {
    UP,
    DOWN,
    NORTH,
    SOUTH,
    EAST,
    WEST;
    
    /**
     * Gets the opposite direction.
     * @return The opposite direction
     */
    public Direction getOpposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }
    
    /**
     * Converts from a Minecraft Direction.
     * @param direction The Minecraft Direction
     * @return The equivalent abstracted Direction
     */
    public static Direction fromMinecraft(net.minecraft.core.Direction direction) {
        return switch (direction) {
            case UP -> UP;
            case DOWN -> DOWN;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
        };
    }
    
    /**
     * Converts to a Minecraft Direction.
     * @return The equivalent Minecraft Direction
     */
    public net.minecraft.core.Direction toMinecraft() {
        return switch (this) {
            case UP -> net.minecraft.core.Direction.UP;
            case DOWN -> net.minecraft.core.Direction.DOWN;
            case NORTH -> net.minecraft.core.Direction.NORTH;
            case SOUTH -> net.minecraft.core.Direction.SOUTH;
            case EAST -> net.minecraft.core.Direction.EAST;
            case WEST -> net.minecraft.core.Direction.WEST;
        };
    }
}