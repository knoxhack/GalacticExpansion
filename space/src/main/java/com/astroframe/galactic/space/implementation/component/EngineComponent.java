package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IRocketEngine;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.space.util.TagHelper;
import net.minecraft.nbt.CompoundTag;

/**
 * Implementation of a rocket engine component.
 */
public class EngineComponent extends BasicRocketComponent implements IRocketEngine {
    
    private float thrust;
    private float fuelEfficiency;
    
    /**
     * Create a new engine component.
     *
     * @param type The component type
     * @param tier The tier level
     */
    public EngineComponent(RocketComponentType type, int tier) {
        super(type, tier);
        this.thrust = calculateThrust(tier);
        this.fuelEfficiency = calculateFuelEfficiency(tier);
    }
    
    /**
     * Calculate thrust based on tier.
     *
     * @param tier The tier level
     * @return The thrust value
     */
    private float calculateThrust(int tier) {
        return tier * 100.0f;
    }
    
    /**
     * Calculate fuel efficiency based on tier.
     *
     * @param tier The tier level
     * @return The fuel efficiency value
     */
    private float calculateFuelEfficiency(int tier) {
        return 0.5f + (tier * 0.1f);
    }
    
    @Override
    public float getThrust() {
        return thrust;
    }
    
    @Override
    public float getFuelEfficiency() {
        return fuelEfficiency;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putFloat("thrust", thrust);
        tag.putFloat("fuelEfficiency", fuelEfficiency);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        super.deserializeNBT(tag);
        this.thrust = TagHelper.getFloat(tag, "thrust");
        this.fuelEfficiency = TagHelper.getFloat(tag, "fuelEfficiency");
    }
}