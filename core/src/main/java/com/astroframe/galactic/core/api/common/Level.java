package com.astroframe.galactic.core.api.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/**
 * Abstraction for Minecraft's Level class.
 * This provides world access without direct dependency on Minecraft classes.
 */
public class Level {
    private final net.minecraft.world.level.Level mcLevel;
    
    /**
     * Creates a new abstracted Level wrapping a Minecraft Level.
     * @param mcLevel The Minecraft Level
     */
    private Level(net.minecraft.world.level.Level mcLevel) {
        this.mcLevel = mcLevel;
    }
    
    /**
     * Creates a new abstracted Level from a Minecraft Level.
     * @param mcLevel The Minecraft Level
     * @return A new abstracted Level
     */
    public static Level fromMinecraft(net.minecraft.world.level.Level mcLevel) {
        return new Level(mcLevel);
    }
    
    /**
     * Gets the underlying Minecraft Level.
     * @return The Minecraft Level
     */
    public net.minecraft.world.level.Level toMinecraft() {
        return mcLevel;
    }
    
    /**
     * Gets the block entity at the given position.
     * @param pos The position
     * @return The block entity, or null if none exists
     */
    public BlockEntity getBlockEntity(BlockPos pos) {
        return mcLevel.getBlockEntity(pos.toMinecraft());
    }
    
    /**
     * Whether the block at the given position is loaded.
     * @param pos The position
     * @return true if the block is loaded
     */
    public boolean isLoaded(BlockPos pos) {
        return mcLevel.isLoaded(pos.toMinecraft());
    }
    
    /**
     * Gets a list of entities of the given type within a box.
     * @param <T> The entity type
     * @param entityClass The entity class
     * @param min The minimum corner of the box
     * @param max The maximum corner of the box
     * @return A list of entities
     */
    public <T extends Entity> java.util.List<T> getEntitiesOfClass(
            Class<T> entityClass, BlockPos min, BlockPos max) {
        return mcLevel.getEntitiesOfClass(
                entityClass,
                new AABB(min.toMinecraft(), max.toMinecraft()));
    }
    
    /**
     * Whether this level is a client-side level.
     * @return true if this is a client-side level
     */
    public boolean isClientSide() {
        return mcLevel.isClientSide();
    }
    
    /**
     * Gets the tick count for this level.
     * @return The tick count
     */
    public int getGameTime() {
        return (int) mcLevel.getGameTime();
    }
}