package com.example.modapi.core.api.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

/**
 * Base class for mod blocks.
 * Extends the Minecraft Block class with additional functionality.
 */
public class ModBlock extends Block {

    /**
     * Constructor for ModBlock.
     * 
     * @param properties The block properties
     */
    public ModBlock(Properties properties) {
        super(properties);
    }
    
    /**
     * Creates a new block with default properties.
     * 
     * @return A new block
     */
    public static ModBlock create() {
        return new ModBlock(defaultProperties());
    }
    
    /**
     * Creates a new block with custom properties.
     * 
     * @param properties The block properties
     * @return A new block
     */
    public static ModBlock create(Properties properties) {
        return new ModBlock(properties);
    }
    
    /**
     * Gets the default block properties.
     * 
     * @return The default properties
     */
    public static Properties defaultProperties() {
        return Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6.0F);
    }
}
