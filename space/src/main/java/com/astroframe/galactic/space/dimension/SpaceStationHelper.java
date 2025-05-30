package com.astroframe.galactic.space.dimension;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Helper utility for Space Station dimension operations.
 * Contains constants and methods for building the space station platform.
 */
public class SpaceStationHelper {

    // Constants for the space station platform
    public static final int PLATFORM_Y = 100;
    public static final int PLATFORM_CENTER_X = 0;
    public static final int PLATFORM_CENTER_Z = 0;
    public static final int PLATFORM_RADIUS = 32;
    
    // Block states for platform construction
    public static final BlockState PLATFORM_BORDER = Blocks.REINFORCED_DEEPSLATE.defaultBlockState();
    public static final BlockState PLATFORM_FLOOR = Blocks.LIGHT_GRAY_CONCRETE.defaultBlockState();
    public static final BlockState PLATFORM_LIGHT = Blocks.LIGHT.defaultBlockState();

    /**
     * Utility method to build the space station platform at the origin.
     * This can be called during initial world generation or used by a command.
     */
    public static void buildSpaceStationPlatform(WorldGenLevel level) {
        GalacticSpace.LOGGER.info("Building space station platform at origin");
        
        // Build the platform blocks
        for (int x = -PLATFORM_RADIUS; x <= PLATFORM_RADIUS; x++) {
            for (int z = -PLATFORM_RADIUS; z <= PLATFORM_RADIUS; z++) {
                int distanceSquared = x * x + z * z;
                
                // Within platform radius
                if (distanceSquared <= PLATFORM_RADIUS * PLATFORM_RADIUS) {
                    // Place floor
                    BlockPos pos = new BlockPos(x, PLATFORM_Y, z);
                    
                    // Border blocks
                    if (distanceSquared >= (PLATFORM_RADIUS - 1) * (PLATFORM_RADIUS - 1)) {
                        level.setBlock(pos, PLATFORM_BORDER, 3);
                    } else {
                        // Floor blocks with occasional lights
                        if ((x + z) % 8 == 0) {
                            level.setBlock(pos, PLATFORM_LIGHT, 3);
                        } else {
                            level.setBlock(pos, PLATFORM_FLOOR, 3);
                        }
                    }
                }
            }
        }
        
        // Add station marker block
        level.setBlock(new BlockPos(0, PLATFORM_Y + 1, 0), Blocks.BEACON.defaultBlockState(), 3);
        
        GalacticSpace.LOGGER.info("Space station platform built successfully");
    }
}