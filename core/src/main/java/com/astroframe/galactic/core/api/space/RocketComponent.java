package com.astroframe.galactic.core.api.space;

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
        float baseMass = switch(type) {
            case ENGINE -> 1000.0f;
            case COCKPIT -> 750.0f;
            case FUEL_TANK -> 500.0f;
            case CARGO_BAY -> 600.0f;
            case LANDING_GEAR -> 300.0f;
            case HEAT_SHIELD -> 250.0f;
            case SOLAR_PANEL -> 100.0f;
            case COMMUNICATION -> 50.0f;
            case NAVIGATION -> 75.0f;
            case LIFE_SUPPORT -> 400.0f;
        };
        
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
        float baseDurability = switch(type) {
            case ENGINE -> 800.0f;
            case COCKPIT -> 1000.0f;
            case FUEL_TANK -> 900.0f;
            case CARGO_BAY -> 700.0f;
            case LANDING_GEAR -> 600.0f;
            case HEAT_SHIELD -> 1200.0f;
            case SOLAR_PANEL -> 300.0f;
            case COMMUNICATION -> 400.0f;
            case NAVIGATION -> 500.0f;
            case LIFE_SUPPORT -> 850.0f;
        };
        
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
        
        if (tag.contains("type")) {
            this.type = RocketComponentType.getById(tag.getString("type").orElse("engine"));
        }
        
        if (tag.contains("tier")) {
            this.tier = tag.getInt("tier").orElse(1);
        }
        
        if (tag.contains("mass")) {
            this.mass = tag.getFloat("mass").orElse(calculateMass());
        }
        
        if (tag.contains("durability")) {
            this.durability = tag.getFloat("durability").orElse(calculateDurability());
        }
        
        if (tag.contains("maxDurability")) {
            this.maxDurability = tag.getFloat("maxDurability").orElse(calculateDurability());
        }
        
        if (tag.contains("efficiency")) {
            this.efficiency = tag.getFloat("efficiency").orElse(calculateEfficiency());
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