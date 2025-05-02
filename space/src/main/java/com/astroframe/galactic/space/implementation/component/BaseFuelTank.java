package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IFuelTank;
import com.astroframe.galactic.core.api.space.component.enums.ComponentType;
import com.astroframe.galactic.core.api.space.component.enums.FuelType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of the IFuelTank interface.
 */
public class BaseFuelTank implements IFuelTank {
    
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final int maxFuelCapacity;
    private final FuelType fuelType;
    private final float pressureRating;
    private final boolean isInsulated;
    
    private BaseFuelTank(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.tier = builder.tier;
        this.mass = builder.mass;
        this.maxHealth = builder.maxHealth;
        this.maxFuelCapacity = builder.maxFuelCapacity;
        this.fuelType = builder.fuelType;
        this.pressureRating = builder.pressureRating;
        this.isInsulated = builder.isInsulated;
    }
    
    // Implementation of IRocketComponent method
    public ResourceLocation getId() {
        return id;
    }
    
    // Implementation of IRocketComponent method
    public Component getDisplayName() {
        return displayName;
    }
    
    // Implementation of IRocketComponent method
    public int getTier() {
        return tier;
    }
    
    // Implementation of IRocketComponent method
    public com.astroframe.galactic.core.api.space.component.RocketComponentType getType() {
        return com.astroframe.galactic.core.api.space.component.RocketComponentType.FUEL_TANK;
    }
    
    // Implementation of IRocketComponent method
    public int getMass() {
        return mass;
    }
    
    // Implementation of IRocketComponent method
    public float getMaxHealth() {
        return maxHealth;
    }
    
    // Implementation of IRocketComponent method
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(displayName);
        tooltip.add(Component.literal("Fuel Type: " + fuelType.getDisplayName()));
        tooltip.add(Component.literal("Capacity: " + maxFuelCapacity));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Pressure Rating: " + pressureRating + " MPa"));
            tooltip.add(Component.literal("Insulated: " + (isInsulated ? "Yes" : "No")));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Max Health: " + maxHealth));
        }
        
        return tooltip;
    }
    
    // Implementation of IFuelTank method
    public int getMaxFuelCapacity() {
        return maxFuelCapacity;
    }
    
    // Implementation of IFuelType method
    public com.astroframe.galactic.core.api.space.component.IRocketEngine.FuelType getFuelType() {
        // Manual mapping from our enum to the interface enum
        switch (fuelType) {
            case CHEMICAL:
                return com.astroframe.galactic.core.api.space.component.IRocketEngine.FuelType.CHEMICAL;
            case ANTIMATTER:
                return com.astroframe.galactic.core.api.space.component.IRocketEngine.FuelType.ANTIMATTER;
            case ION:
            case HYDROGEN:
                return com.astroframe.galactic.core.api.space.component.IRocketEngine.FuelType.ELECTRICAL;
            case NUCLEAR:
            case FUSION:
            case EXOTIC:
                // These would be comparable to PLASMA in the target enum
                return com.astroframe.galactic.core.api.space.component.IRocketEngine.FuelType.PLASMA;
            default:
                return com.astroframe.galactic.core.api.space.component.IRocketEngine.FuelType.CHEMICAL;
        }
    }
    
    // Implementation of IFuelTank method
    public float getPressureRating() {
        return pressureRating;
    }
    
    // Implementation of IFuelTank method
    public boolean isInsulated() {
        return isInsulated;
    }
    
    // Implementation of IFuelTank method
    public float getExplosionResistance() {
        return 0.7f + (tier * 0.1f); // Increases with tier
    }
    
    // Implementation of IFuelTank method
    public float getLeakResistance() {
        return 0.8f + (tier * 0.1f); // Increases with tier
    }
    
    // Implementation of IFuelTank method
    public int getCurrentFuelLevel() {
        return maxFuelCapacity; // Mock implementation - always full for now
    }
    
    // Implementation of IFuelTank method
    public int addFuel(int amount) {
        return amount; // Mock implementation - pretend we added all fuel
    }
    
    // Implementation of IFuelTank method
    public int consumeFuel(int amount) {
        return amount; // Mock implementation - pretend we consumed all requested fuel
    }
    
    // Implementation of IRocketComponent methods
    public int getMaxDurability() {
        return (int)maxHealth;
    }
    
    public int getCurrentDurability() {
        return (int)maxHealth; // Currently always at max
    }
    
    public void damage(int amount) {
        // No-op until we implement durability
    }
    
    public boolean isBroken() {
        return false; // Default implementation always returns false until we implement durability
    }
    
    public void repair(int amount) {
        // No-op until we implement durability
    }
    
    public String getName() {
        return displayName.getString();
    }
    
    public String getDescription() {
        return "Fuel Tank - " + fuelType.getDisplayName() + " - " + maxFuelCapacity + " units";
    }
    
    /**
     * Builder for BaseFuelTank.
     */
    public static class Builder {
        private final ResourceLocation id;
        private final Component displayName;
        private int tier = 1;
        private int mass = 500;
        private float maxHealth = 100.0f;
        private int maxFuelCapacity = 1000;
        private FuelType fuelType = FuelType.CHEMICAL;
        private float pressureRating = 10.0f;
        private boolean isInsulated = false;
        
        /**
         * Creates a new builder with required parameters.
         * @param id The component ID
         * @param displayName The display name
         */
        public Builder(ResourceLocation id, Component displayName) {
            this.id = id;
            this.displayName = displayName;
        }
        
        /**
         * Sets the tier.
         * @param tier The tier level (1-3)
         * @return This builder
         */
        public Builder tier(int tier) {
            this.tier = Math.max(1, Math.min(3, tier));
            return this;
        }
        
        /**
         * Sets the mass.
         * @param mass The mass
         * @return This builder
         */
        public Builder mass(int mass) {
            this.mass = mass;
            return this;
        }
        
        /**
         * Sets the max health.
         * @param maxHealth The max health
         * @return This builder
         */
        public Builder maxHealth(float maxHealth) {
            this.maxHealth = maxHealth;
            return this;
        }
        
        /**
         * Sets the max fuel capacity.
         * @param maxFuelCapacity The max fuel capacity
         * @return This builder
         */
        public Builder maxFuelCapacity(int maxFuelCapacity) {
            this.maxFuelCapacity = maxFuelCapacity;
            return this;
        }
        
        /**
         * Sets the fuel type.
         * @param fuelType The fuel type
         * @return This builder
         */
        public Builder fuelType(FuelType fuelType) {
            this.fuelType = fuelType;
            return this;
        }
        
        /**
         * Sets the pressure rating.
         * @param pressureRating The pressure rating in MPa
         * @return This builder
         */
        public Builder pressureRating(float pressureRating) {
            this.pressureRating = pressureRating;
            return this;
        }
        
        /**
         * Sets whether the tank is insulated.
         * @param insulated True if insulated
         * @return This builder
         */
        public Builder insulated(boolean insulated) {
            this.isInsulated = insulated;
            return this;
        }
        
        /**
         * Builds the fuel tank.
         * @return A new BaseFuelTank
         */
        public BaseFuelTank build() {
            return new BaseFuelTank(this);
        }
    }
}