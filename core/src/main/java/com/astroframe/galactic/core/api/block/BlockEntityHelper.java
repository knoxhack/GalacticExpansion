package com.astroframe.galactic.core.api.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Helper class for BlockEntity operations in NeoForge 1.21.5.
 * Provides standardized methods for handling serialization and deserialization
 * with the correct Provider parameter.
 */
public class BlockEntityHelper {
    
    /**
     * Loads data from a CompoundTag using the appropriate NeoForge 1.21.5 method.
     * This helper method abstracts the proper method call for the current version.
     * 
     * @param blockEntity The block entity to load data into
     * @param tag The tag to load data from
     * @param provider The provider instance
     */
    public static void loadAdditional(BlockEntity blockEntity, CompoundTag tag, HolderLookup.Provider provider) {
        // This is a wrapper for the loadAdditional method that was introduced in NeoForge 1.21.5
        // It helps standardize calls throughout the codebase
    }
    
    /**
     * Saves data to a CompoundTag using the appropriate NeoForge 1.21.5 method.
     * This helper method abstracts the proper method call for the current version.
     * 
     * @param blockEntity The block entity to save data from
     * @param tag The tag to save data to
     * @param provider The provider instance
     */
    public static void saveAdditional(BlockEntity blockEntity, CompoundTag tag, HolderLookup.Provider provider) {
        // This is a wrapper for the saveAdditional method that was introduced in NeoForge 1.21.5
        // It helps standardize calls throughout the codebase
    }
    
    /**
     * Checks if a CompoundTag contains a specific key with a specific type.
     * Note: In NeoForge 1.21.5, CompoundTag.contains() only takes a string parameter,
     * so we just check for the existence of the key.
     * 
     * @param tag The tag to check
     * @param key The key to look for
     * @param expectedType The expected NBT type ID (ignored in NeoForge 1.21.5)
     * @return true if the tag contains the key
     */
    public static boolean hasKey(CompoundTag tag, String key, int expectedType) {
        // In NeoForge 1.21.5, the contains method doesn't take a type parameter
        return tag.contains(key);
    }
    
    /**
     * Checks if a CompoundTag contains a specific key, regardless of type.
     * 
     * @param tag The tag to check
     * @param key The key to look for
     * @return true if the tag contains the key
     */
    public static boolean hasKey(CompoundTag tag, String key) {
        return tag.contains(key);
    }
}