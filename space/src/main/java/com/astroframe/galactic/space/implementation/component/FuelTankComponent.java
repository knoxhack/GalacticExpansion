package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IFuelTank;
import com.astroframe.galactic.core.api.space.RocketComponentType;
import com.astroframe.galactic.core.api.space.component.FuelType;
import com.astroframe.galactic.space.util.TagHelper;
import net.minecraft.nbt.CompoundTag;

/**
 * Implementation of a fuel tank component.
 */
public class FuelTankComponent extends BasicRocketComponent implements IFuelTank {
    
    private int maxFuelCapacity;
    private int currentFuelLevel;
    private FuelType fuelType;
    private float leakResistance;
    private float explosionResistance;
    
    /**
     * Create a new fuel tank component.
     *
     * @param type The component type
     * @param tier The tier level
     */
    public FuelTankComponent(RocketComponentType type, int tier) {
        super(type, tier);
        this.maxFuelCapacity = calculateCapacity(tier);
        this.currentFuelLevel = this.maxFuelCapacity; // Start with a full tank
        this.fuelType = tier <= 1 ? FuelType.CHEMICAL : (tier == 2 ? FuelType.ION : FuelType.ANTIMATTER);
        this.leakResistance = 0.5f + (tier * 0.15f);
        this.explosionResistance = 0.3f + (tier * 0.2f);
    }
    
    /**
     * Calculate capacity based on tier.
     *
     * @param tier The tier level
     * @return The capacity value
     */
    private int calculateCapacity(int tier) {
        return tier * 200;
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
        int spaceLeft = maxFuelCapacity - currentFuelLevel;
        int actualAmount = Math.min(amount, spaceLeft);
        currentFuelLevel += actualAmount;
        return actualAmount;
    }
    
    @Override
    public int consumeFuel(int amount) {
        int actualAmount = Math.min(amount, currentFuelLevel);
        currentFuelLevel -= actualAmount;
        return actualAmount;
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
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putInt("maxFuelCapacity", maxFuelCapacity);
        tag.putInt("currentFuelLevel", currentFuelLevel);
        tag.putString("fuelType", fuelType.name());
        tag.putFloat("leakResistance", leakResistance);
        tag.putFloat("explosionResistance", explosionResistance);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        super.deserializeNBT(tag);
        this.maxFuelCapacity = TagHelper.getInt(tag, "maxFuelCapacity");
        this.currentFuelLevel = TagHelper.getInt(tag, "currentFuelLevel");
        
        try {
            this.fuelType = FuelType.valueOf(TagHelper.getString(tag, "fuelType"));
        } catch (IllegalArgumentException e) {
            this.fuelType = FuelType.CHEMICAL;
        }
        
        this.leakResistance = TagHelper.getFloat(tag, "leakResistance");
        this.explosionResistance = TagHelper.getFloat(tag, "explosionResistance");
    }
}