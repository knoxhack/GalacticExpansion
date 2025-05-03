package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IFuelTank;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.space.util.TagHelper;
import net.minecraft.nbt.CompoundTag;

/**
 * Implementation of a fuel tank component.
 */
public class FuelTankComponent extends BasicRocketComponent implements IFuelTank {
    
    private int capacity;
    
    /**
     * Create a new fuel tank component.
     *
     * @param type The component type
     * @param tier The tier level
     */
    public FuelTankComponent(RocketComponentType type, int tier) {
        super(type, tier);
        this.capacity = calculateCapacity(tier);
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
    public int getCapacity() {
        return capacity;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putInt("capacity", capacity);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        super.deserializeNBT(tag);
        this.capacity = TagHelper.getInt(tag, "capacity");
    }
}