package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IRocketEngine;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.core.api.space.component.enums.EngineType;
import com.astroframe.galactic.core.api.space.component.enums.FuelType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of a rocket engine component.
 */
public class RocketEngineImpl implements IRocketEngine {
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final int tier;
    private final int mass;
    private final int maxDurability;
    private int currentDurability;
    private final int thrust;
    private final int fuelConsumptionRate;
    private final float efficiency;
    private final FuelType fuelType;
    private final boolean canOperateInAtmosphere;
    private final boolean canOperateInSpace;
    private final EngineType engineType;
    
    /**
     * Creates a new rocket engine.
     */
    public RocketEngineImpl(
            ResourceLocation id,
            String name,
            String description,
            int tier,
            int mass,
            int maxDurability,
            int thrust,
            int fuelConsumptionRate,
            float efficiency,
            FuelType fuelType,
            boolean canOperateInAtmosphere,
            boolean canOperateInSpace,
            EngineType engineType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.mass = mass;
        this.maxDurability = maxDurability;
        this.currentDurability = maxDurability;
        this.thrust = thrust;
        this.fuelConsumptionRate = fuelConsumptionRate;
        this.efficiency = efficiency;
        this.fuelType = fuelType;
        this.canOperateInAtmosphere = canOperateInAtmosphere;
        this.canOperateInSpace = canOperateInSpace;
        this.engineType = engineType;
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
        return RocketComponentType.ENGINE;
    }

    @Override
    public int getMass() {
        return mass;
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
        currentDurability = Math.max(0, currentDurability - amount);
    }
    
    @Override
    public void repair(int amount) {
        currentDurability = Math.min(maxDurability, currentDurability + amount);
    }

    // NeoForge 1.21.5 update: Changed return type to match IRocketEngine interface
    public double getThrust() {
        return thrust;
    }
    
    // Method for fuel consumption rate
    public double getFuelConsumptionRate() {
        return fuelConsumptionRate;
    }

    // NeoForge 1.21.5 update: Changed return type to match IRocketEngine interface
    public double getEfficiency() {
        return efficiency;
    }
    
    @Override
    public FuelType getFuelType() {
        return fuelType;
    }

    @Override
    public boolean canOperateInAtmosphere() {
        return canOperateInAtmosphere;
    }

    @Override
    public boolean canOperateInSpace() {
        return canOperateInSpace;
    }
    
    @Override
    public EngineType getEngineType() {
        return engineType;
    }
    
    @Override
    public double getHeatGeneration() {
        // Base heat generation is proportional to thrust and inversely related to efficiency
        return thrust * (1.5 - efficiency * 0.5) * 0.5;
    }
    
    @Override
    public List<FuelType> getCompatibleFuels() {
        // For simplicity, we're returning a list with just the primary fuel type
        // In a more advanced implementation, this could depend on engine type
        return Collections.singletonList(fuelType);
    }

    /**
     * Gets a list of tooltip lines for this component.
     * @param detailed Whether to include detailed information
     * @return The tooltip lines
     */
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        
        tooltip.add(Component.literal(name));
        tooltip.add(Component.literal("Thrust: " + thrust + " N"));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Fuel Type: " + fuelType.name()));
            tooltip.add(Component.literal("Fuel Consumption: " + fuelConsumptionRate + " units/sec"));
            tooltip.add(Component.literal("Efficiency: " + efficiency));
            tooltip.add(Component.literal("Atmosphere Capable: " + (canOperateInAtmosphere ? "Yes" : "No")));
            tooltip.add(Component.literal("Space Capable: " + (canOperateInSpace ? "Yes" : "No")));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Durability: " + maxDurability));
        }
        
        return tooltip;
    }
    
    /**
     * Builder for RocketEngineImpl.
     */
    public static class Builder {
        private final ResourceLocation id;
        private String name = "Rocket Engine";
        private String description = "A rocket engine for propulsion.";
        private int tier = 1;
        private int mass = 500;
        private int maxDurability = 100;
        private int thrust = 1000;
        private int fuelConsumptionRate = 10;
        private float efficiency = 1.0f;
        private FuelType fuelType = FuelType.CHEMICAL;
        private boolean canOperateInAtmosphere = true;
        private boolean canOperateInSpace = true;
        private EngineType engineType = EngineType.CHEMICAL;
        
        /**
         * Creates a new builder with required parameters.
         * @param id The component ID
         */
        public Builder(ResourceLocation id) {
            this.id = id;
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
         * Sets the mass.
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
         * Sets the thrust.
         * @param thrust The thrust in newtons
         * @return This builder
         */
        public Builder thrust(int thrust) {
            this.thrust = thrust;
            return this;
        }
        
        /**
         * Sets the fuel consumption rate.
         * @param fuelConsumptionRate The fuel consumption rate
         * @return This builder
         */
        public Builder fuelConsumptionRate(int fuelConsumptionRate) {
            this.fuelConsumptionRate = fuelConsumptionRate;
            return this;
        }
        
        /**
         * Sets the efficiency.
         * @param efficiency The efficiency (0.1-2.0)
         * @return This builder
         */
        public Builder efficiency(float efficiency) {
            this.efficiency = Math.max(0.1f, Math.min(2.0f, efficiency));
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
         * Sets whether this engine can operate in atmosphere.
         * @param canOperateInAtmosphere True if can operate in atmosphere
         * @return This builder
         */
        public Builder atmosphereCapable(boolean canOperateInAtmosphere) {
            this.canOperateInAtmosphere = canOperateInAtmosphere;
            return this;
        }
        
        /**
         * Sets whether this engine can operate in space.
         * @param canOperateInSpace True if can operate in space
         * @return This builder
         */
        public Builder spaceCapable(boolean canOperateInSpace) {
            this.canOperateInSpace = canOperateInSpace;
            return this;
        }
        
        /**
         * Sets the engine type.
         * @param engineType The engine type
         * @return This builder
         */
        public Builder engineType(EngineType engineType) {
            this.engineType = engineType;
            return this;
        }
        
        /**
         * Builds the rocket engine.
         * @return A new RocketEngineImpl
         */
        public RocketEngineImpl build() {
            return new RocketEngineImpl(
                    id, name, description, tier, mass, maxDurability,
                    thrust, fuelConsumptionRate, efficiency, fuelType,
                    canOperateInAtmosphere, canOperateInSpace, engineType
            );
        }
    }
}