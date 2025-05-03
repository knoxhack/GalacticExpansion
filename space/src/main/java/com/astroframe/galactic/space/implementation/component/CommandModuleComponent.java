package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.ICommandModule;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.space.util.TagHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

/**
 * Implementation of a command module (cockpit) component.
 */
public class CommandModuleComponent extends BasicRocketComponent implements ICommandModule {
    
    private int crewCapacity;
    private int computingPower;
    private int sensorStrength;
    private float navigationAccuracy;
    private boolean hasAdvancedLifeSupport;
    private boolean hasAutomatedLanding;
    private boolean hasEmergencyEvacuation;
    
    /**
     * Create a new command module component.
     *
     * @param type The component type
     * @param tier The tier level
     */
    public CommandModuleComponent(RocketComponentType type, int tier) {
        super(type, tier);
        this.crewCapacity = calculateCrewCapacity(tier);
        this.computingPower = tier * 10;
        this.sensorStrength = tier * 5;
        this.navigationAccuracy = 0.6f + (tier * 0.1f);
        this.hasAdvancedLifeSupport = tier >= 2;
        this.hasAutomatedLanding = tier >= 2;
        this.hasEmergencyEvacuation = tier >= 3;
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
    public int getComputingPower() {
        return computingPower;
    }
    
    @Override
    public int getSensorStrength() {
        return sensorStrength;
    }
    
    @Override
    public float getNavigationAccuracy() {
        return navigationAccuracy;
    }
    
    @Override
    public int getCrewCapacity() {
        return crewCapacity;
    }
    
    @Override
    public boolean hasAdvancedLifeSupport() {
        return hasAdvancedLifeSupport;
    }
    
    @Override
    public boolean hasAutomatedLanding() {
        return hasAutomatedLanding;
    }
    
    @Override
    public boolean hasEmergencyEvacuation() {
        return hasEmergencyEvacuation;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putInt("crewCapacity", crewCapacity);
        tag.putInt("computingPower", computingPower);
        tag.putInt("sensorStrength", sensorStrength);
        tag.putFloat("navigationAccuracy", navigationAccuracy);
        tag.putBoolean("hasAdvancedLifeSupport", hasAdvancedLifeSupport);
        tag.putBoolean("hasAutomatedLanding", hasAutomatedLanding);
        tag.putBoolean("hasEmergencyEvacuation", hasEmergencyEvacuation);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        super.deserializeNBT(tag);
        this.crewCapacity = TagHelper.getInt(tag, "crewCapacity");
        this.computingPower = TagHelper.getInt(tag, "computingPower");
        this.sensorStrength = TagHelper.getInt(tag, "sensorStrength");
        this.navigationAccuracy = TagHelper.getFloat(tag, "navigationAccuracy");
        this.hasAdvancedLifeSupport = TagHelper.getBoolean(tag, "hasAdvancedLifeSupport");
        this.hasAutomatedLanding = TagHelper.getBoolean(tag, "hasAutomatedLanding");
        this.hasEmergencyEvacuation = TagHelper.getBoolean(tag, "hasEmergencyEvacuation");
    }
}