package com.example.modapi.energy.api;

import com.astroframe.galactic.energy.api.energynetwork.WorldPosition;

/**
 * Adapter class to convert between BlockPos and WorldPosition.
 * This provides compatibility between the old and new APIs.
 * 
 * @deprecated Use the new energy API directly
 */
@Deprecated
public class BlockPosAdapter {
    
    /**
     * Convert a BlockPos to a WorldPosition.
     * 
     * @param pos The BlockPos to convert
     * @param level The level
     * @return The equivalent WorldPosition
     */
    public static WorldPosition toWorldPosition(BlockPos pos, Level level) {
        com.astroframe.galactic.energy.api.energynetwork.Level customLevel = 
            new com.astroframe.galactic.energy.api.energynetwork.Level(level.getDimension());
        return new WorldPosition(pos.getX(), pos.getY(), pos.getZ(), customLevel);
    }
    
    /**
     * Convert a WorldPosition to a BlockPos.
     * 
     * @param position The WorldPosition to convert
     * @return The equivalent BlockPos
     */
    public static BlockPos toBlockPos(WorldPosition position) {
        return new BlockPos(position.getX(), position.getY(), position.getZ());
    }
    
    /**
     * Convert a WorldPosition to a Minecraft BlockPos.
     * 
     * @param position The WorldPosition to convert
     * @return The equivalent Minecraft BlockPos
     */
    public static Object toMinecraftBlockPos(WorldPosition position) {
        // Due to class loading issues, we use reflection here
        try {
            Class<?> blockPosClass = Class.forName("net.minecraft.core.BlockPos");
            return blockPosClass.getConstructor(int.class, int.class, int.class)
                .newInstance(position.getX(), position.getY(), position.getZ());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Minecraft BlockPos", e);
        }
    }
    
    /**
     * Create a custom Level from a Minecraft Level.
     * 
     * @param mcLevel The Minecraft level object
     * @return The custom Level
     */
    public static com.astroframe.galactic.energy.api.energynetwork.Level toCustomLevel(Object mcLevel) {
        try {
            // Use reflection to get the dimension ID
            Class<?> levelClass = mcLevel.getClass();
            Object dimension = levelClass.getMethod("dimension").invoke(mcLevel);
            Object location = dimension.getClass().getMethod("location").invoke(dimension);
            String dimensionId = location.toString();
            
            return new com.astroframe.galactic.energy.api.energynetwork.Level(dimensionId);
        } catch (Exception e) {
            // If reflection fails, return a default level
            return new com.astroframe.galactic.energy.api.energynetwork.Level("unknown");
        }
    }
}