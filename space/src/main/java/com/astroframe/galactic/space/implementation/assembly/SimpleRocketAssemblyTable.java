package com.astroframe.galactic.space.implementation.assembly;

import net.minecraft.world.level.block.Block;

/**
 * Simplified version of the rocket assembly table.
 * This is a placeholder implementation that avoids complex block entity functionality,
 * allowing the mod to load without errors while still providing the visual assets.
 */
public class SimpleRocketAssemblyTable extends Block {
    
    /**
     * Creates a new simple rocket assembly table.
     *
     * @param properties The block properties
     */
    public SimpleRocketAssemblyTable(Properties properties) {
        super(properties);
    }
}