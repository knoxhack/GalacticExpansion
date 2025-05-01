package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * Implementation of a structure component for rockets.
 */
public class StructureImpl implements IRocketComponent {
    
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final int tier;
    private final int mass;
    private int durability;
    private final int maxDurability;
    private final int structuralIntegrity;
    private final boolean damaged;
    
    /**
     * Creates a new structure implementation.
     * 
     * @param id The component ID
     * @param name The component name
     * @param description The component description
     * @param tier The component tier
     * @param mass The component mass
     * @param maxDurability The maximum durability
     * @param structuralIntegrity The structural integrity
     */
    private StructureImpl(ResourceLocation id, String name, String description, 
                      int tier, int mass, int maxDurability, int structuralIntegrity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.mass = mass;
        this.maxDurability = maxDurability;
        this.durability = maxDurability;
        this.structuralIntegrity = structuralIntegrity;
        this.damaged = false;
    }
    
    @Override
    public ResourceLocation getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    @Override
    public int getTier() {
        return tier;
    }
    
    @Override
    public int getMass() {
        return mass;
    }
    
    @Override
    public RocketComponentType getType() {
        return RocketComponentType.STRUCTURE;
    }
    
    @Override
    public boolean isValidFor(int rocketTier) {
        // Structure components are valid for any rocket tier that's equal or higher
        return rocketTier >= tier;
    }
    
    @Override
    public boolean isDamaged() {
        return damaged || durability < maxDurability;
    }
    
    @Override
    public void repair(int amount) {
        this.durability = Math.min(this.durability + amount, maxDurability);
    }
    
    @Override
    public void damage(int amount) {
        this.durability = Math.max(0, this.durability - amount);
    }
    
    @Override
    public int getDurability() {
        return durability;
    }
    
    @Override
    public int getMaxDurability() {
        return maxDurability;
    }
    
    /**
     * Gets the structural integrity of this structure component.
     * @return The structural integrity
     */
    public int getStructuralIntegrity() {
        return structuralIntegrity;
    }
    
    /**
     * Builder for creating structure components.
     */
    public static class Builder {
        private final ResourceLocation id;
        private String name = "Structure";
        private String description = "Standard structural component";
        private int tier = 1;
        private int mass = 100;
        private int maxDurability = 1000;
        private int structuralIntegrity = 100;
        
        /**
         * Creates a new structure builder.
         * @param id The component ID
         */
        public Builder(ResourceLocation id) {
            this.id = id;
        }
        
        /**
         * Sets the name of this component.
         * @param name The name
         * @return This builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Sets the description of this component.
         * @param description The description
         * @return This builder
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        /**
         * Sets the tier of this component.
         * @param tier The tier
         * @return This builder
         */
        public Builder tier(int tier) {
            this.tier = tier;
            return this;
        }
        
        /**
         * Sets the mass of this component.
         * @param mass The mass
         * @return This builder
         */
        public Builder mass(int mass) {
            this.mass = mass;
            return this;
        }
        
        /**
         * Sets the maximum durability of this component.
         * @param maxDurability The maximum durability
         * @return This builder
         */
        public Builder maxDurability(int maxDurability) {
            this.maxDurability = maxDurability;
            return this;
        }
        
        /**
         * Sets the structural integrity of this component.
         * @param structuralIntegrity The structural integrity
         * @return This builder
         */
        public Builder structuralIntegrity(int structuralIntegrity) {
            this.structuralIntegrity = structuralIntegrity;
            return this;
        }
        
        /**
         * Builds a new structure component.
         * @return The new component
         */
        public StructureImpl build() {
            return new StructureImpl(id, name, description, tier, mass, maxDurability, structuralIntegrity);
        }
    }
}