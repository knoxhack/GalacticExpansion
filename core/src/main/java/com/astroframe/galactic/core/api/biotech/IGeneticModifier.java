package com.astroframe.galactic.core.api.biotech;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for genetic modification of entities and crops.
 * This is a central concept for the Biological Engineering module.
 */
public interface IGeneticModifier {
    
    /**
     * Extracts genetic material from an entity or item.
     * 
     * @param source The source entity or item
     * @return The extracted genetic material, or null if extraction failed
     */
    GeneticMaterial extractGeneticMaterial(Object source);
    
    /**
     * Applies genetic material to an entity or item.
     * 
     * @param target The target entity or item
     * @param material The genetic material to apply
     * @param strength The strength of the modification (0.0 to 1.0)
     * @return Whether the application was successful
     */
    boolean applyGeneticMaterial(Object target, GeneticMaterial material, float strength);
    
    /**
     * Creates a hybrid genetic material from two parent materials.
     * 
     * @param parent1 The first parent material
     * @param parent2 The second parent material
     * @param dominanceFactor The dominance factor of parent1 (0.0 to 1.0)
     * @return The resulting hybrid material, or null if hybridization failed
     */
    GeneticMaterial createHybrid(GeneticMaterial parent1, GeneticMaterial parent2, float dominanceFactor);
    
    /**
     * Mutates genetic material randomly or with specific changes.
     * 
     * @param material The material to mutate
     * @param mutationFactor The intensity of mutation (0.0 to 1.0)
     * @param specificMutations Specific gene mutations to apply, or null for random
     * @return The mutated material, or null if mutation failed
     */
    GeneticMaterial mutateMaterial(GeneticMaterial material, float mutationFactor, Map<String, String> specificMutations);
    
    /**
     * Gets all registered genetic traits.
     * 
     * @return A set of all trait IDs
     */
    Set<ResourceLocation> getAllTraits();
    
    /**
     * Gets traits available for a specific entity type.
     * 
     * @param entityType The entity type
     * @return A set of available trait IDs
     */
    Set<ResourceLocation> getAvailableTraitsForEntityType(EntityType<?> entityType);
    
    /**
     * Gets information about a genetic trait.
     * 
     * @param traitId The trait ID
     * @return Information about the trait, or null if not found
     */
    TraitInfo getTraitInfo(ResourceLocation traitId);
    
    /**
     * Creates a sample container item containing genetic material.
     * 
     * @param material The genetic material
     * @return An item stack containing the material
     */
    ItemStack createSampleContainer(GeneticMaterial material);
    
    /**
     * Extracts genetic material from a sample container item.
     * 
     * @param container The sample container item
     * @return The extracted material, or null if extraction failed
     */
    GeneticMaterial extractFromSampleContainer(ItemStack container);
    
    /**
     * Gets all active genetic modifications on an entity.
     * 
     * @param entity The entity to check
     * @return A list of active modifications
     */
    List<GeneticModification> getActiveModifications(LivingEntity entity);
    
    /**
     * Updates an entity's capabilities based on its genetic modifications.
     * 
     * @param entity The entity to update
     */
    void updateEntityCapabilities(LivingEntity entity);
    
    /**
     * Clears all genetic modifications from an entity.
     * 
     * @param entity The entity to clear
     * @return Whether the clearing was successful
     */
    boolean clearModifications(LivingEntity entity);
    
    /**
     * Represents genetic material that can be extracted, modified, and applied.
     */
    interface GeneticMaterial {
        /**
         * Gets the source type of this genetic material.
         * 
         * @return The source type
         */
        SourceType getSourceType();
        
        /**
         * Gets the entity type if this material came from an entity.
         * 
         * @return The entity type, or null if not from an entity
         */
        EntityType<?> getEntityType();
        
        /**
         * Gets the item source if this material came from an item.
         * 
         * @return The source item, or null if not from an item
         */
        ItemStack getItemSource();
        
        /**
         * Gets all genes in this genetic material.
         * 
         * @return A map of gene names to values
         */
        Map<String, String> getGenes();
        
        /**
         * Gets all traits in this genetic material.
         * 
         * @return A map of trait IDs to strength values (0.0 to 1.0)
         */
        Map<ResourceLocation, Float> getTraits();
        
        /**
         * Gets the stability of this genetic material.
         * Lower stability means higher chance of mutation or degradation.
         * 
         * @return A value from 0.0 (unstable) to 1.0 (perfectly stable)
         */
        float getStability();
        
        /**
         * Gets the quality of this genetic material.
         * Higher quality leads to stronger trait expression.
         * 
         * @return A value from 0.0 (poor) to 1.0 (perfect)
         */
        float getQuality();
        
        /**
         * Saves this genetic material to NBT.
         * 
         * @return The NBT representation
         */
        CompoundTag toNBT();
        
        /**
         * Source types for genetic material.
         */
        enum SourceType {
            ENTITY,
            ITEM,
            SYNTHETIC,
            HYBRID
        }
    }
    
    /**
     * Represents an active genetic modification on an entity.
     */
    interface GeneticModification {
        /**
         * Gets the trait ID of this modification.
         * 
         * @return The trait ID
         */
        ResourceLocation getTraitId();
        
        /**
         * Gets the strength of this modification.
         * 
         * @return A value from 0.0 to 1.0
         */
        float getStrength();
        
        /**
         * Gets the stability of this modification.
         * 
         * @return A value from 0.0 (unstable) to 1.0 (stable)
         */
        float getStability();
        
        /**
         * Gets the source of this modification.
         * 
         * @return The source
         */
        String getSource();
        
        /**
         * Gets the duration of this modification in ticks, or -1 if permanent.
         * 
         * @return The duration
         */
        int getDuration();
        
        /**
         * Gets the remaining duration in ticks, or -1 if permanent.
         * 
         * @return The remaining duration
         */
        int getRemainingDuration();
        
        /**
         * Checks if this modification is permanent.
         * 
         * @return Whether the modification is permanent
         */
        boolean isPermanent();
        
        /**
         * Gets side effects of this modification.
         * 
         * @return A map of side effect names to severity values (0.0 to 1.0)
         */
        Map<String, Float> getSideEffects();
    }
    
    /**
     * Information about a genetic trait.
     */
    class TraitInfo {
        private ResourceLocation id;
        private String displayName;
        private String description;
        private List<String> effects;
        private Map<String, String> requiredGenes;
        private List<EntityType<?>> compatibleEntities;
        private float stabilityFactor;
        private Map<String, Float> potentialSideEffects;
        
        // Getters and setters
    }
}