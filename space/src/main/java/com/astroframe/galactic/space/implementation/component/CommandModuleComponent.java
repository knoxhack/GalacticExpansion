package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.ICommandModule;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import net.minecraft.nbt.CompoundTag;

/**
 * Implementation of a command module (cockpit) component.
 */
public class CommandModuleComponent extends BasicRocketComponent implements ICommandModule {
    
    private int crewCapacity;
    
    /**
     * Create a new command module component.
     *
     * @param type The component type
     * @param tier The tier level
     */
    public CommandModuleComponent(RocketComponentType type, int tier) {
        super(type, tier);
        this.crewCapacity = calculateCrewCapacity(tier);
    }
    
    /**
     * Calculate crew capacity based on tier.
     *
     * @param tier The tier level
     * @return The crew capacity
     */
    private int calculateCrewCapacity(int tier) {
        return tier;
    }
    
    @Override
    public int getCrewCapacity() {
        return crewCapacity;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putInt("crewCapacity", crewCapacity);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        super.deserializeNBT(tag);
        this.crewCapacity = tag.getInt("crewCapacity");
    }
}