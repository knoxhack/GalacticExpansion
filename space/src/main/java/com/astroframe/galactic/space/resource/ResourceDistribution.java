package com.astroframe.galactic.space.resource;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a distribution of resources for a celestial body.
 */
public class ResourceDistribution {
    private final List<ResourceEntry> resources = new ArrayList<>();
    
    /**
     * Adds a resource to this distribution.
     *
     * @param item The item
     * @param chance The chance (0.0-1.0) of generating this resource
     * @param minAmount Minimum amount generated
     * @param maxAmount Maximum amount generated
     * @return This distribution for chaining
     */
    public ResourceDistribution addResource(Item item, float chance, int minAmount, int maxAmount) {
        resources.add(new ResourceEntry(item, chance, minAmount, maxAmount));
        return this;
    }
    
    /**
     * Generates resources based on this distribution.
     *
     * @param random The random source
     * @param bonusChance Additional chance modifier (0.0-1.0)
     * @return List of generated item stacks
     */
    public List<ItemStack> generateResources(RandomSource random, float bonusChance) {
        List<ItemStack> result = new ArrayList<>();
        
        for (ResourceEntry entry : resources) {
            float finalChance = Math.min(1.0f, entry.chance + bonusChance);
            
            if (random.nextFloat() < finalChance) {
                int amount = entry.minAmount;
                if (entry.maxAmount > entry.minAmount) {
                    amount += random.nextInt(entry.maxAmount - entry.minAmount + 1);
                }
                
                result.add(new ItemStack(entry.item, amount));
            }
        }
        
        return result;
    }
    
    /**
     * Internal class representing a single resource entry.
     */
    private static class ResourceEntry {
        private final Item item;
        private final float chance;
        private final int minAmount;
        private final int maxAmount;
        
        /**
         * Creates a new resource entry.
         *
         * @param item The item
         * @param chance The chance (0.0-1.0) of generating
         * @param minAmount Minimum amount generated
         * @param maxAmount Maximum amount generated
         */
        ResourceEntry(Item item, float chance, int minAmount, int maxAmount) {
            this.item = item;
            this.chance = chance;
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
        }
    }
}