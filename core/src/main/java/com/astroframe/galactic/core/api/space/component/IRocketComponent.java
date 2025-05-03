package com.astroframe.galactic.core.api.space.component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

/**
 * Base interface for all rocket components.
 */
public interface IRocketComponent {
    
    /**
     * Gets the unique ID of this component.
     * @return The component ID
     */
    ResourceLocation getId();
    
    /**
     * Gets the type of this component.
     * @return The component type
     */
    RocketComponentType getType();
    
    /**
     * Gets the name of this component.
     * @return The component name
     */
    String getName();
    
    /**
     * Gets the description of this component.
     * @return The component description
     */
    String getDescription();
    
    /**
     * Gets the tier of this component (1-3).
     * @return The component tier
     */
    int getTier();
    
    /**
     * Gets the mass of this component.
     * @return The component mass
     */
    int getMass();
    
    /**
     * Gets the weight of this component (for validation calculations).
     * @return The component weight
     */
    default double getWeight() {
        return getMass() * 1.0; // Basic conversion from mass to weight
    }
    
    /**
     * Gets the power output or consumption of this component.
     * Positive values indicate power generation, negative values indicate consumption.
     * @return The power value
     */
    default double getPower() {
        return 0.0; // Default implementation returns 0 (no power generation/consumption)
    }
    
    /**
     * Gets the capacity of this component.
     * Used for fuel tanks, cargo bays, etc.
     * @return The capacity value
     */
    default double getCapacity() {
        return 0.0; // Default implementation returns 0 (no capacity)
    }
    
    /**
     * Gets the maximum durability of this component.
     * @return The maximum durability
     */
    int getMaxDurability();
    
    /**
     * Gets the current durability of this component.
     * @return The current durability
     */
    int getCurrentDurability();
    
    /**
     * Damages this component.
     * @param amount The amount of damage to apply
     */
    void damage(int amount);
    
    /**
     * Repairs this component.
     * @param amount The amount to repair
     */
    void repair(int amount);
    
    /**
     * Gets the health of this component as a percentage.
     * @return The component health
     */
    default float getHealth() {
        return ((float) getCurrentDurability() / getMaxDurability()) * 100;
    }
    
    /**
     * Whether this component is broken.
     * @return true if the component is broken
     */
    default boolean isBroken() {
        return getCurrentDurability() <= 0;
    }
    
    /**
     * Gets the position of this component within the rocket.
     * Used for rendering and placement.
     * 
     * @return The component position vector
     */
    default Vec3 getPosition() {
        return new Vec3(0, 0, 0);
    }
    
    /**
     * Gets the size (dimensions) of this component.
     * Used for rendering and collision detection.
     * 
     * @return The component size vector
     */
    default Vec3 getSize() {
        return new Vec3(0.5, 0.5, 0.5);
    }
    
    /**
     * Sets the position of this component within the rocket.
     * Used during rocket assembly.
     * 
     * @param position The new position
     */
    default void setPosition(Vec3 position) {
        // Default implementation does nothing
    }
    
    /**
     * Saves this component to a tag.
     * Default implementation saves basic properties.
     * Component implementations should override this to save additional properties.
     * 
     * @param tag The tag to save to
     */
    default void save(net.minecraft.nbt.CompoundTag tag) {
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
            // In NeoForge 1.21.5, we must make sure our tag values are safe
            tag.putDouble("PosX", pos.x);
            tag.putDouble("PosY", pos.y);
            tag.putDouble("PosZ", pos.z);
        }
    }
    
    /**
     * Loads this component from a tag.
     * Default implementation loads position if saved.
     * Component implementations should override this to load additional properties.
     * 
     * @param tag The tag to load from
     */
    default void load(net.minecraft.nbt.CompoundTag tag) {
        // Load position if saved
        if (tag.contains("PosX") && tag.contains("PosY") && tag.contains("PosZ")) {
            // In NeoForge 1.21.5, we need to use TagHelper to get direct values
            try {
                double x = tag.getDouble("PosX"); 
                double y = tag.getDouble("PosY");
                double z = tag.getDouble("PosZ");
                setPosition(new Vec3(x, y, z));
            } catch (Exception e) {
                // Fallback if the direct approach fails
                setPosition(new Vec3(0, 0, 0));
            }
        }
    }
}