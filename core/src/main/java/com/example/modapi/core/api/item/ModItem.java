package com.example.modapi.core.api.item;

import net.minecraft.world.item.Item;

/**
 * Base class for mod items.
 * Extends the Minecraft Item class with additional functionality.
 */
public class ModItem extends Item {

    /**
     * Constructor for ModItem.
     * 
     * @param properties The item properties
     */
    public ModItem(Properties properties) {
        super(properties);
    }
    
    /**
     * Creates a new item with default properties.
     * 
     * @return A new item
     */
    public static ModItem create() {
        return new ModItem(new Properties());
    }
    
    /**
     * Creates a new item with custom properties.
     * 
     * @param properties The item properties
     * @return A new item
     */
    public static ModItem create(Properties properties) {
        return new ModItem(properties);
    }
}
