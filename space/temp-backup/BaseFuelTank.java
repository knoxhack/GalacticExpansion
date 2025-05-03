package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IFuelTank;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.core.api.space.component.enums.FuelType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the IFuelTank interface.
 * Stores fuel for rocket engines with leak and explosion resistance properties.
 */
public class BaseFuelTank implements IFuelTank {
    
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final int tier;
    private final int mass;
    private final int maxDurability;
    private int currentDurability;
    private final int maxFuelCapacity;
    private int currentFuelLevel;
    private final FuelType fuelType;
    private final float leakResistance;
    private final float explosionResistance;
    private final boolean isInsulated;
    private final float pressureRating;
    private Vec3 position;
    
    private BaseFuelTank(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.tier = builder.tier;
        this.mass = builder.mass;
        this.maxDurability = builder.maxDurability;
        this.currentDurability = this.maxDurability;
        this.maxFuelCapacity = builder.maxFuelCapacity;
        this.currentFuelLevel = builder.initialFuelLevel;
        this.fuelType = builder.fuelType;
        this.leakResistance = builder.leakResistance;
        this.explosionResistance = builder.explosionResistance;
        this.isInsulated = builder.isInsulated;
        this.pressureRating = builder.pressureRating;
        this.position = new Vec3(0, 0, 0);
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
    public RocketComponentType getType() {
        return RocketComponentType.FUEL_TANK;
    }
    
    @Override
    public int getMass() {
        // The mass increases with the fuel level
        int baseMass = mass;
        // Add the mass of the fuel (simplified calculation)
        int fuelMass = (int) (currentFuelLevel * 0.1f); 
        return baseMass + fuelMass;
    }
    
    @Override
    public int getMaxDurability() {
        return maxDurability;
    }
    
    @Override
    public int getCurrentDurability() {
        return currentDurability;
    }
    
    @Override
    public void damage(int amount) {
        int previousDurability = currentDurability;
        currentDurability = Math.max(0, currentDurability - amount);
        
        // If durability dropped significantly, fuel might leak
        int durabilityLost = previousDurability - currentDurability;
        if (durabilityLost > 0 && currentFuelLevel > 0) {
            // Calculate potential leak amount based on damage and leak resistance
            float leakProbability = 1.0f - leakResistance;
            int leakAmount = (int) (durabilityLost * leakProbability * currentFuelLevel * 0.01f);
            if (leakAmount > 0) {
                consumeFuel(leakAmount);
            }
        }
    }
    
    @Override
    public void repair(int amount) {
        currentDurability = Math.min(maxDurability, currentDurability + amount);
    }
    
    @Override
    public int getMaxFuelCapacity() {
        return maxFuelCapacity;
    }
    
    @Override
    public int getCurrentFuelLevel() {
        return currentFuelLevel;
    }
    
    @Override
    public int addFuel(int amount) {
        int spaceAvailable = maxFuelCapacity - currentFuelLevel;
        int amountToAdd = Math.min(amount, spaceAvailable);
        
        currentFuelLevel += amountToAdd;
        return amountToAdd;
    }
    
    @Override
    public int consumeFuel(int amount) {
        int amountToConsume = Math.min(amount, currentFuelLevel);
        
        currentFuelLevel -= amountToConsume;
        return amountToConsume;
    }
    
    @Override
    public FuelType getFuelType() {
        return fuelType;
    }
    
    @Override
    public float getLeakResistance() {
        return leakResistance;
    }
    
    @Override
    public float getExplosionResistance() {
        return explosionResistance;
    }
    
    @Override
    public Vec3 getPosition() {
        return position;
    }
    
    @Override
    public void setPosition(Vec3 position) {
        this.position = position;
    }
    
    @Override
    public void save(CompoundTag tag) {
        // Save basic component properties
        tag.putString("ID", getId().toString());
        tag.putString("Type", getType().name());
        tag.putString("Name", getName());
        tag.putString("Description", getDescription());
        tag.putInt("Tier", getTier());
        tag.putInt("Mass", getMass());
        tag.putInt("MaxDurability", getMaxDurability());
        tag.putInt("CurrentDurability", getCurrentDurability());
        
        // Save fuel tank specific properties
        tag.putInt("CurrentFuelLevel", currentFuelLevel);
        tag.putInt("MaxFuelCapacity", maxFuelCapacity);
        tag.putFloat("LeakResistance", leakResistance);
        tag.putFloat("ExplosionResistance", explosionResistance);
        tag.putString("FuelType", fuelType.name());
        tag.putBoolean("Insulated", isInsulated);
        tag.putFloat("PressureRating", pressureRating);
        
        // Save position vector
        tag.putDouble("PosX", position.x());
        tag.putDouble("PosY", position.y());
        tag.putDouble("PosZ", position.z());
    }
    
    @Override
    public void load(CompoundTag tag) {
        // Load basic component properties that can change
        if (tag.contains("CurrentDurability")) {
            this.currentDurability = tag.getInt("CurrentDurability").orElse(this.maxDurability);
        }
        
        // Load fuel tank specific properties
        if (tag.contains("CurrentFuelLevel")) {
            this.currentFuelLevel = tag.getInt("CurrentFuelLevel").orElse(0);
        }
        
        // Load position if present - direct approach for NeoForge 1.21.5
        if (tag.contains("PosX") && tag.contains("PosY") && tag.contains("PosZ")) {
            double x = tag.getDouble("PosX").orElse(0.0);
            double y = tag.getDouble("PosY").orElse(0.0);
            double z = tag.getDouble("PosZ").orElse(0.0);
            this.position = new Vec3(x, y, z);
        }
        
        // Note: We don't need to load constants like maxFuelCapacity, leakResistance, etc.
        // as they are already set in the constructor
    }
    
    /**
     * Checks if this fuel tank is insulated.
     * Insulated tanks can better maintain fuel temperatures.
     * @return true if insulated
     */
    public boolean isInsulated() {
        return isInsulated;
    }
    
    /**
     * Gets the pressure rating of this fuel tank.
     * Higher values allow for more compression of fuel.
     * @return The pressure rating
     */
    public float getPressureRating() {
        return pressureRating;
    }
    
    /**
     * Gets the fill percentage of this fuel tank.
     * @return The fill percentage (0-100)
     */
    public float getFillPercentage() {
        return maxFuelCapacity > 0 ? (currentFuelLevel * 100f / maxFuelCapacity) : 0f;
    }
    
    /**
     * Gets a list of tooltip lines for this component.
     * @param detailed Whether to include detailed information
     * @return The tooltip lines
     */
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal(name));
        tooltip.add(Component.literal("Tier: " + tier));
        tooltip.add(Component.literal("Fuel: " + currentFuelLevel + "/" + maxFuelCapacity));
        tooltip.add(Component.literal("Type: " + fuelType.name()));
        
        if (detailed) {
            tooltip.add(Component.literal("Leak Resistance: " + String.format("%.2f", leakResistance)));
            tooltip.add(Component.literal("Explosion Resistance: " + String.format("%.2f", explosionResistance)));
            tooltip.add(Component.literal("Insulated: " + (isInsulated ? "Yes" : "No")));
            tooltip.add(Component.literal("Pressure Rating: " + String.format("%.2f", pressureRating)));
            tooltip.add(Component.literal("Mass (Empty): " + mass));
            tooltip.add(Component.literal("Mass (Current): " + getMass()));
            tooltip.add(Component.literal("Durability: " + currentDurability + "/" + maxDurability));
        }
        
        return tooltip;
    }
    
    /**
     * Builder for BaseFuelTank.
     */
    public static class Builder {
        private final ResourceLocation id;
        private String name = "Fuel Tank";
        private String description = "A tank for storing rocket fuel.";
        private int tier = 1;
        private int mass = 300;
        private int maxDurability = 100;
        private int maxFuelCapacity = 1000;
        private int initialFuelLevel = 0;
        private FuelType fuelType = FuelType.CHEMICAL;
        private float leakResistance = 0.8f;
        private float explosionResistance = 0.5f;
        private boolean isInsulated = false;
        private float pressureRating = 1.0f;
        
        /**
         * Creates a new builder with required parameters.
         * @param id The component ID
         * @param displayName The display name component
         */
        public Builder(ResourceLocation id, Component displayName) {
            this.id = id;
            this.name = displayName.getString();
        }
        
        /**
         * Sets the name.
         * @param name The name
         * @return This builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Sets the description.
         * @param description The description
         * @return This builder
         */
        public Builder description(String description) {
            this.description = description;
            return this;
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
         * Sets the mass (when empty).
         * @param mass The mass
         * @return This builder
         */
        public Builder mass(int mass) {
            this.mass = mass;
            return this;
        }
        
        /**
         * Sets the max durability.
         * @param maxDurability The max durability
         * @return This builder
         */
        public Builder maxDurability(int maxDurability) {
            this.maxDurability = maxDurability;
            return this;
        }
        
        /**
         * Sets the maximum fuel capacity.
         * @param maxFuelCapacity The maximum fuel capacity
         * @return This builder
         */
        public Builder maxFuelCapacity(int maxFuelCapacity) {
            this.maxFuelCapacity = maxFuelCapacity;
            return this;
        }
        
        /**
         * Sets the initial fuel level.
         * @param initialFuelLevel The initial fuel level
         * @return This builder
         */
        public Builder initialFuelLevel(int initialFuelLevel) {
            this.initialFuelLevel = Math.min(initialFuelLevel, maxFuelCapacity);
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
         * Sets the leak resistance.
         * @param leakResistance The leak resistance (0.0-1.0)
         * @return This builder
         */
        public Builder leakResistance(float leakResistance) {
            this.leakResistance = Math.max(0.0f, Math.min(1.0f, leakResistance));
            return this;
        }
        
        /**
         * Sets the explosion resistance.
         * @param explosionResistance The explosion resistance (0.0-1.0)
         * @return This builder
         */
        public Builder explosionResistance(float explosionResistance) {
            this.explosionResistance = Math.max(0.0f, Math.min(1.0f, explosionResistance));
            return this;
        }
        
        /**
         * Sets whether the tank is insulated.
         * @param isInsulated True if the tank is insulated
         * @return This builder
         */
        public Builder insulated(boolean isInsulated) {
            this.isInsulated = isInsulated;
            return this;
        }
        
        /**
         * Sets the pressure rating.
         * @param pressureRating The pressure rating
         * @return This builder
         */
        public Builder pressureRating(float pressureRating) {
            this.pressureRating = Math.max(1.0f, pressureRating);
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