package com.astroframe.galactic.space.implementation.component.command;

import com.astroframe.galactic.core.api.space.component.ICommandModule;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

/**
 * A basic command module implementation.
 * Provides essential navigation and crew support.
 */
public class BasicCommandModule implements ICommandModule {
    
    private static final String DEFAULT_NAME = "Basic Command Module";
    private static final String DEFAULT_DESCRIPTION = "A basic command module with essential navigation systems.";
    private static final int DEFAULT_TIER = 1;
    private static final int DEFAULT_MASS = 100;
    private static final int DEFAULT_MAX_DURABILITY = 200;
    private static final int DEFAULT_COMPUTING_POWER = 50;
    private static final int DEFAULT_SENSOR_STRENGTH = 40;
    private static final float DEFAULT_NAVIGATION_ACCURACY = 0.7f;
    private static final int DEFAULT_CREW_CAPACITY = 1;
    
    private final ResourceLocation id;
    private String name;
    private String description;
    private int tier;
    private int mass;
    private int maxDurability;
    private int currentDurability;
    private int computingPower;
    private int sensorStrength;
    private float navigationAccuracy;
    private int crewCapacity;
    private boolean advancedLifeSupport;
    private boolean automatedLanding;
    private boolean emergencyEvacuation;
    private Vec3 position;
    
    /**
     * Creates a basic command module with default values.
     * 
     * @param id The unique identifier
     */
    public BasicCommandModule(ResourceLocation id) {
        this.id = id;
        this.name = DEFAULT_NAME;
        this.description = DEFAULT_DESCRIPTION;
        this.tier = DEFAULT_TIER;
        this.mass = DEFAULT_MASS;
        this.maxDurability = DEFAULT_MAX_DURABILITY;
        this.currentDurability = maxDurability;
        this.computingPower = DEFAULT_COMPUTING_POWER;
        this.sensorStrength = DEFAULT_SENSOR_STRENGTH;
        this.navigationAccuracy = DEFAULT_NAVIGATION_ACCURACY;
        this.crewCapacity = DEFAULT_CREW_CAPACITY;
        this.advancedLifeSupport = false;
        this.automatedLanding = false;
        this.emergencyEvacuation = false;
        this.position = new Vec3(0, 0, 0);
    }
    
    /**
     * Creates a basic command module with custom values.
     * 
     * @param id The unique identifier
     * @param tier The technology tier (1-3)
     * @param name The display name
     * @param description The description
     */
    public BasicCommandModule(ResourceLocation id, int tier, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        
        // Scale properties based on tier
        this.mass = DEFAULT_MASS - (tier - 1) * 10; // Higher tier is lighter
        this.maxDurability = DEFAULT_MAX_DURABILITY + (tier - 1) * 50; // Higher tier is more durable
        this.currentDurability = maxDurability;
        this.computingPower = DEFAULT_COMPUTING_POWER + (tier - 1) * 25; // Higher tier has more computing power
        this.sensorStrength = DEFAULT_SENSOR_STRENGTH + (tier - 1) * 30; // Higher tier has better sensors
        this.navigationAccuracy = DEFAULT_NAVIGATION_ACCURACY + (tier - 1) * 0.1f; // Higher tier is more accurate
        this.crewCapacity = DEFAULT_CREW_CAPACITY + (tier - 1); // Higher tier holds more crew
        
        // Advanced features based on tier
        this.advancedLifeSupport = (tier >= 2);
        this.automatedLanding = (tier >= 2);
        this.emergencyEvacuation = (tier >= 3);
        
        this.position = new Vec3(0, 0, 0);
    }
    
    /**
     * Creates a fully customized command module.
     * 
     * @param id The unique identifier
     * @param name The display name
     * @param description The description
     * @param tier The technology tier
     * @param mass The mass in kg
     * @param maxDurability The maximum durability
     * @param computingPower The computing power
     * @param sensorStrength The sensor strength
     * @param navigationAccuracy The navigation accuracy
     * @param crewCapacity The crew capacity
     * @param advancedLifeSupport Whether it has advanced life support
     * @param automatedLanding Whether it has automated landing
     * @param emergencyEvacuation Whether it has emergency evacuation
     */
    public BasicCommandModule(ResourceLocation id, String name, String description, int tier, int mass,
                              int maxDurability, int computingPower, int sensorStrength, float navigationAccuracy,
                              int crewCapacity, boolean advancedLifeSupport, boolean automatedLanding,
                              boolean emergencyEvacuation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.mass = mass;
        this.maxDurability = maxDurability;
        this.currentDurability = maxDurability;
        this.computingPower = computingPower;
        this.sensorStrength = sensorStrength;
        this.navigationAccuracy = navigationAccuracy;
        this.crewCapacity = crewCapacity;
        this.advancedLifeSupport = advancedLifeSupport;
        this.automatedLanding = automatedLanding;
        this.emergencyEvacuation = emergencyEvacuation;
        this.position = new Vec3(0, 0, 0);
    }
    
    @Override
    public ResourceLocation getId() {
        return id;
    }
    
    @Override
    public RocketComponentType getType() {
        return RocketComponentType.COMMAND_MODULE;
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
        return advancedLifeSupport;
    }
    
    @Override
    public boolean hasAutomatedLanding() {
        return automatedLanding;
    }
    
    @Override
    public boolean hasEmergencyEvacuation() {
        return emergencyEvacuation;
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
    public Vec3 getSize() {
        // Command modules are typically somewhat spherical or capsule-shaped
        return new Vec3(0.8, 0.8, 0.8);
    }
    
    @Override
    public void save(CompoundTag tag) {
        // Save basic properties manually for NeoForge 1.21.5 compatibility
        tag.putString("ID", getId().toString());
        tag.putString("Type", getType().name());
        tag.putString("Name", getName());
        tag.putString("Description", getDescription());
        tag.putInt("Tier", getTier());
        tag.putInt("Mass", getMass());
        tag.putInt("MaxDurability", getMaxDurability());
        tag.putInt("CurrentDurability", getCurrentDurability());
        
        // Save position if component has a non-default position
        Vec3 pos = getPosition();
        if (pos.x != 0 || pos.y != 0 || pos.z != 0) {
            tag.putDouble("PosX", pos.x);
            tag.putDouble("PosY", pos.y);
            tag.putDouble("PosZ", pos.z);
        }
        
        // Save command module specific properties
        tag.putInt("ComputingPower", computingPower);
        tag.putInt("SensorStrength", sensorStrength);
        tag.putFloat("NavigationAccuracy", navigationAccuracy);
        tag.putInt("CrewCapacity", crewCapacity);
        tag.putBoolean("AdvancedLifeSupport", advancedLifeSupport);
        tag.putBoolean("AutomatedLanding", automatedLanding);
        tag.putBoolean("EmergencyEvacuation", emergencyEvacuation);
    }
    
    @Override
    public void load(CompoundTag tag) {
        // In NeoForge 1.21.5, we need to call parent methods through default interfaces
        super.setPosition(getPosition()); // Update position from parent
        
        // Load common properties manually
        if (tag.contains("PosX") && tag.contains("PosY") && tag.contains("PosZ")) {
            double x = tag.getDouble("PosX").orElse(0.0);
            double y = tag.getDouble("PosY").orElse(0.0);
            double z = tag.getDouble("PosZ").orElse(0.0);
            setPosition(new Vec3(x, y, z));
        }
        
        // Load command module specific properties
        if (tag.contains("ComputingPower")) {
            this.computingPower = tag.getInt("ComputingPower").orElse(DEFAULT_COMPUTING_POWER);
        }
        if (tag.contains("SensorStrength")) {
            this.sensorStrength = tag.getInt("SensorStrength").orElse(DEFAULT_SENSOR_STRENGTH);
        }
        if (tag.contains("NavigationAccuracy")) {
            this.navigationAccuracy = tag.getFloat("NavigationAccuracy").orElse(DEFAULT_NAVIGATION_ACCURACY);
        }
        if (tag.contains("CrewCapacity")) {
            this.crewCapacity = tag.getInt("CrewCapacity").orElse(DEFAULT_CREW_CAPACITY);
        }
        if (tag.contains("AdvancedLifeSupport")) {
            this.advancedLifeSupport = tag.getBoolean("AdvancedLifeSupport").orElse(false);
        }
        if (tag.contains("AutomatedLanding")) {
            this.automatedLanding = tag.getBoolean("AutomatedLanding").orElse(false);
        }
        if (tag.contains("EmergencyEvacuation")) {
            this.emergencyEvacuation = tag.getBoolean("EmergencyEvacuation").orElse(false);
        }
    }
}