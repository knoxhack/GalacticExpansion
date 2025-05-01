package com.astroframe.galactic.core.api.vehicle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * Interface for vehicle modules that can be installed on vehicles.
 * These modules provide additional functionality or improvements.
 */
public interface IVehicleModule {
    
    /**
     * Gets the unique identifier for this module instance.
     * 
     * @return The module instance UUID
     */
    UUID getModuleId();
    
    /**
     * Gets the type identifier for this module.
     * 
     * @return The module type ID
     */
    ResourceLocation getModuleType();
    
    /**
     * Gets the display name of this module.
     * 
     * @return The display name
     */
    String getDisplayName();
    
    /**
     * Gets the description of this module.
     * 
     * @return The description
     */
    String getDescription();
    
    /**
     * Gets the tier of this module.
     * Higher tiers provide better performance.
     * 
     * @return The module tier (1-5)
     */
    int getTier();
    
    /**
     * Gets the slot type this module can be installed in.
     * 
     * @return The slot type
     */
    ModuleSlot getSlotType();
    
    /**
     * Gets the weight of this module in kilograms.
     * Heavier modules affect vehicle performance.
     * 
     * @return The weight
     */
    float getWeight();
    
    /**
     * Gets the energy consumption of this module per tick.
     * 
     * @return The energy consumption
     */
    int getEnergyConsumption();
    
    /**
     * Checks if this module is compatible with a specific vehicle.
     * 
     * @param vehicle The vehicle to check
     * @return Whether the module is compatible
     */
    boolean isCompatibleWith(IVehicle vehicle);
    
    /**
     * Gets the required module types that must be installed for this module to function.
     * 
     * @return A list of prerequisite module types
     */
    List<ResourceLocation> getPrerequisites();
    
    /**
     * Gets module types that conflict with this module.
     * 
     * @return A list of conflicting module types
     */
    List<ResourceLocation> getConflicts();
    
    /**
     * Gets all stats this module affects.
     * 
     * @return A list of affected stats
     */
    List<VehicleStat> getAffectedStats();
    
    /**
     * Gets the modifier this module applies to a specific stat.
     * 
     * @param stat The stat to check
     * @return The stat modifier (percentage change, positive or negative)
     */
    float getStatModifier(VehicleStat stat);
    
    /**
     * Called when this module is installed on a vehicle.
     * 
     * @param vehicle The vehicle
     */
    void onInstalled(IVehicle vehicle);
    
    /**
     * Called when this module is uninstalled from a vehicle.
     * 
     * @param vehicle The vehicle
     */
    void onUninstalled(IVehicle vehicle);
    
    /**
     * Called every tick while this module is installed.
     * 
     * @param vehicle The vehicle
     */
    void onTick(IVehicle vehicle);
    
    /**
     * Creates an item stack containing this module.
     * 
     * @return The item stack
     */
    ItemStack toItemStack();
    
    /**
     * Loads this module from an item stack.
     * 
     * @param stack The item stack
     * @return Whether loading was successful
     */
    boolean loadFromItemStack(ItemStack stack);
    
    /**
     * Saves this module's state to NBT.
     * 
     * @param tag The tag to save to
     * @return The updated tag
     */
    CompoundTag save(CompoundTag tag);
    
    /**
     * Loads this module's state from NBT.
     * 
     * @param tag The tag to load from
     */
    void load(CompoundTag tag);
    
    /**
     * Module slot types.
     */
    enum ModuleSlot {
        ENGINE,
        TRANSMISSION,
        CHASSIS,
        ARMOR,
        WEAPON,
        ENERGY,
        STORAGE,
        UTILITY,
        NAVIGATION,
        COMMUNICATION,
        STEALTH,
        SHIELD
    }
    
    /**
     * Vehicle stats that can be affected by modules.
     */
    enum VehicleStat {
        TOP_SPEED,
        ACCELERATION,
        HANDLING,
        BRAKING,
        FUEL_EFFICIENCY,
        ENERGY_EFFICIENCY,
        ARMOR,
        WEIGHT,
        CARGO_CAPACITY,
        PASSENGER_CAPACITY,
        STABILITY,
        MANEUVERABILITY,
        STEALTH,
        RADAR_RANGE,
        WEAPON_DAMAGE,
        WEAPON_RANGE,
        SHIELD_STRENGTH,
        SHIELD_RECHARGE
    }
}