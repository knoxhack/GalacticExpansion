package com.astroframe.galactic.core.api.space;

import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.core.api.util.TagHelper;
import net.minecraft.nbt.CompoundTag;

/**
 * Represents a component of a modular rocket.
 */
public class RocketComponent {
    private RocketComponentType type;
    private int tier;
    private float mass;
    private float durability;
    private float maxDurability;
    private float efficiency;
    
    /**
     * Creates a new rocket component.
     */
    public RocketComponent() {
        this(RocketComponentType.ENGINE, 1);
    }
    
    /**
     * Creates a new rocket component with the specified type and tier.
     *
     * @param type The component type
     * @param tier The component tier
     */
    public RocketComponent(RocketComponentType type, int tier) {
        this.type = type;
        this.tier = tier;
        this.mass = calculateMass();
        this.maxDurability = calculateDurability();
        this.durability = this.maxDurability;
        this.efficiency = calculateEfficiency();
    }
    
    /**
     * Gets the component type.
     *
     * @return The component type
     */
    public RocketComponentType getType() {
        return type;
    }
    
    /**
     * Gets the component tier.
     *
     * @return The component tier
     */
    public int getTier() {
        return tier;
    }
    
    /**
     * Gets the component mass.
     *
     * @return The component mass
     */
    public float getMass() {
        return mass;
    }
    
    /**
     * Gets the current durability.
     *
     * @return The current durability
     */
    public float getDurability() {
        return durability;
    }
    
    /**
     * Gets the maximum durability.
     *
     * @return The maximum durability
     */
    public float getMaxDurability() {
        return maxDurability;
    }
    
    /**
     * Gets the efficiency rating.
     *
     * @return The efficiency rating (0.0 to 1.0)
     */
    public float getEfficiency() {
        return efficiency;
    }
    
    /**
     * Sets the component type.
     *
     * @param type The new component type
     */
    public void setType(RocketComponentType type) {
        this.type = type;
        recalculateStats();
    }
    
    /**
     * Sets the component tier.
     *
     * @param tier The new component tier
     */
    public void setTier(int tier) {
        this.tier = Math.max(1, Math.min(5, tier));
        recalculateStats();
    }
    
    /**
     * Sets the component's current durability.
     *
     * @param durability The new durability
     */
    public void setDurability(float durability) {
        this.durability = Math.max(0, Math.min(maxDurability, durability));
    }
    
    /**
     * Damages the component by the specified amount.
     *
     * @param amount The amount of damage to apply
     * @return True if the component is still functional
     */
    public boolean damage(float amount) {
        durability = Math.max(0, durability - amount);
        return durability > 0;
    }
    
    /**
     * Repairs the component by the specified amount.
     *
     * @param amount The amount to repair
     */
    public void repair(float amount) {
        durability = Math.min(maxDurability, durability + amount);
    }
    
    /**
     * Calculates the mass based on component type and tier.
     *
     * @return The calculated mass
     */
    private float calculateMass() {
        float baseMass;
        
        // Use switch on name instead of direct enum reference to avoid comparison issues
        String typeName = type.name();
        
        if (typeName.equals("ENGINE")) {
            baseMass = 1000.0f;
        } else if (typeName.equals("COCKPIT")) {
            baseMass = 750.0f;
        } else if (typeName.equals("FUEL_TANK")) {
            baseMass = 500.0f;
        } else if (typeName.equals("CARGO_BAY")) {
            baseMass = 600.0f;
        } else if (typeName.equals("LANDING_GEAR")) {
            baseMass = 300.0f;
        } else if (typeName.equals("HEAT_SHIELD")) {
            baseMass = 250.0f;
        } else if (typeName.equals("SOLAR_PANEL")) {
            baseMass = 100.0f;
        } else if (typeName.equals("COMMUNICATION")) {
            baseMass = 50.0f;
        } else if (typeName.equals("NAVIGATION")) {
            baseMass = 75.0f;
        } else if (typeName.equals("LIFE_SUPPORT")) {
            baseMass = 400.0f;
        } else {
            baseMass = 200.0f; // Default value for unknown component types
        }
        
        // Higher tier components are lighter (better materials)
        float tierModifier = 1.0f - ((tier - 1) * 0.1f);
        return baseMass * tierModifier;
    }
    
    /**
     * Calculates the durability based on component type and tier.
     *
     * @return The calculated durability
     */
    private float calculateDurability() {
        float baseDurability;
        
        // Use string comparison instead of enum switch
        String typeName = type.name();
        
        if (typeName.equals("ENGINE")) {
            baseDurability = 800.0f;
        } else if (typeName.equals("COCKPIT")) {
            baseDurability = 1000.0f;
        } else if (typeName.equals("FUEL_TANK")) {
            baseDurability = 900.0f;
        } else if (typeName.equals("CARGO_BAY")) {
            baseDurability = 700.0f;
        } else if (typeName.equals("LANDING_GEAR")) {
            baseDurability = 600.0f;
        } else if (typeName.equals("HEAT_SHIELD")) {
            baseDurability = 1200.0f;
        } else if (typeName.equals("SOLAR_PANEL")) {
            baseDurability = 300.0f;
        } else if (typeName.equals("COMMUNICATION")) {
            baseDurability = 400.0f;
        } else if (typeName.equals("NAVIGATION")) {
            baseDurability = 500.0f;
        } else if (typeName.equals("LIFE_SUPPORT")) {
            baseDurability = 850.0f;
        } else {
            baseDurability = 500.0f; // Default value for unknown component types
        }
        
        // Higher tier components have more durability
        float tierModifier = 1.0f + ((tier - 1) * 0.2f);
        return baseDurability * tierModifier;
    }
    
    /**
     * Calculates the efficiency based on component type and tier.
     *
     * @return The calculated efficiency (0.0 to 1.0)
     */
    private float calculateEfficiency() {
        float baseEfficiency = 0.6f;
        // Higher tier components are more efficient
        float tierBonus = (tier - 1) * 0.08f;
        return Math.min(1.0f, baseEfficiency + tierBonus);
    }
    
    /**
     * Recalculates all the component statistics.
     */
    private void recalculateStats() {
        this.mass = calculateMass();
        this.maxDurability = calculateDurability();
        this.durability = Math.min(durability, maxDurability);
        this.efficiency = calculateEfficiency();
    }
    
    /**
     * Saves the component data to an NBT tag.
     *
     * @param tag The tag to save to
     * @return The updated tag
     */
    public CompoundTag save(CompoundTag tag) {
        if (tag == null) {
            tag = new CompoundTag();
        }
        
        tag.putString("type", type.getId());
        tag.putInt("tier", tier);
        tag.putFloat("mass", mass);
        tag.putFloat("durability", durability);
        tag.putFloat("maxDurability", maxDurability);
        tag.putFloat("efficiency", efficiency);
        
        return tag;
    }
    
    /**
     * Loads the component data from an NBT tag.
     *
     * @param tag The tag to load from
     */
    public void load(CompoundTag tag) {
        if (tag == null) {
            return;
        }
        
        // Use TagHelper to safely get values with fallbacks for NeoForge 1.21.5
        String typeStr = TagHelper.getString(tag, "type", "");
        if (!typeStr.isEmpty()) {
            this.type = RocketComponentType.getById(typeStr);
        }
        
        // Get tier with validation
        this.tier = TagHelper.getInt(tag, "tier", 1);
        if (this.tier < 1 || this.tier > 5) {
            this.tier = 1;
        }
        
        // Get mass with calculated fallback
        float loadedMass = TagHelper.getFloat(tag, "mass", -1);
        this.mass = (loadedMass > 0) ? loadedMass : calculateMass();
        
        // Get durability with calculated fallback
        float loadedDurability = TagHelper.getFloat(tag, "durability", -1);
        this.durability = (loadedDurability >= 0) ? loadedDurability : calculateDurability();
        
        // Get maxDurability with calculated fallback
        float loadedMaxDurability = TagHelper.getFloat(tag, "maxDurability", -1);
        this.maxDurability = (loadedMaxDurability > 0) ? loadedMaxDurability : calculateDurability();
        
        // Get efficiency with calculated fallback and bounds checking
        float loadedEfficiency = TagHelper.getFloat(tag, "efficiency", -1);
        if (loadedEfficiency >= 0 && loadedEfficiency <= 1) {
            this.efficiency = loadedEfficiency;
        } else {
            this.efficiency = calculateEfficiency();
        }
    }
    
    /**
     * Creates a rocket component from an NBT tag.
     *
     * @param tag The tag to load from
     * @return The created component
     */
    public static RocketComponent fromTag(CompoundTag tag) {
        if (tag == null) {
            return new RocketComponent();
        }
        
        RocketComponent component = new RocketComponent();
        component.load(tag);
        return component;
    }
}