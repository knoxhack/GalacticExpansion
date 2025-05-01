package com.astroframe.galactic.core.api.weapon;

import com.astroframe.galactic.core.api.energy.IEnergyHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Interface for weapons in the Weaponry module.
 * This covers energy weapons, firearms, and explosives.
 */
public interface IWeapon {
    
    /**
     * Gets the unique identifier for this weapon instance.
     * 
     * @return The weapon UUID
     */
    UUID getWeaponId();
    
    /**
     * Gets the registry ID for this weapon type.
     * 
     * @return The weapon type ID
     */
    ResourceLocation getWeaponType();
    
    /**
     * Gets the category of this weapon.
     * 
     * @return The weapon category
     */
    WeaponCategory getCategory();
    
    /**
     * Gets the tier of this weapon.
     * Higher tiers have better performance.
     * 
     * @return The weapon tier (1-5)
     */
    int getTier();
    
    /**
     * Gets the ammo type this weapon uses.
     * 
     * @return The ammo type, or null if it uses energy instead
     */
    ResourceLocation getAmmoType();
    
    /**
     * Gets the energy handler if this is an energy weapon.
     * 
     * @return The energy handler, or null if not an energy weapon
     */
    IEnergyHandler getEnergyHandler();
    
    /**
     * Gets the current ammo count in this weapon.
     * 
     * @return The ammo count
     */
    int getAmmoCount();
    
    /**
     * Gets the maximum ammo capacity of this weapon.
     * 
     * @return The ammo capacity
     */
    int getAmmoCapacity();
    
    /**
     * Loads ammo into this weapon.
     * 
     * @param ammo The ammo item stack
     * @return The remaining ammo that couldn't be loaded
     */
    ItemStack loadAmmo(ItemStack ammo);
    
    /**
     * Gets the energy consumed per shot for energy weapons.
     * 
     * @return The energy per shot
     */
    int getEnergyPerShot();
    
    /**
     * Gets the base damage for a single shot from this weapon.
     * 
     * @return The base damage
     */
    float getBaseDamage();
    
    /**
     * Gets the damage type of this weapon.
     * 
     * @return The damage type
     */
    DamageType getDamageType();
    
    /**
     * Gets the fire rate in shots per second.
     * 
     * @return The fire rate
     */
    float getFireRate();
    
    /**
     * Gets the cooldown time between shots in ticks.
     * 
     * @return The cooldown time
     */
    int getCooldownTime();
    
    /**
     * Gets the remaining cooldown time in ticks.
     * 
     * @return The remaining cooldown
     */
    int getRemainingCooldown();
    
    /**
     * Gets the effective range of this weapon in blocks.
     * 
     * @return The effective range
     */
    float getEffectiveRange();
    
    /**
     * Gets the maximum range of this weapon in blocks.
     * 
     * @return The maximum range
     */
    float getMaximumRange();
    
    /**
     * Gets the spread angle of this weapon in degrees.
     * 
     * @return The spread angle
     */
    float getSpreadAngle();
    
    /**
     * Gets the recoil force applied when firing.
     * 
     * @return The recoil force
     */
    float getRecoil();
    
    /**
     * Gets the accuracy of this weapon (0.0 to 1.0).
     * 
     * @return The accuracy
     */
    float getAccuracy();
    
    /**
     * Gets the reload time in ticks.
     * 
     * @return The reload time
     */
    int getReloadTime();
    
    /**
     * Gets the current reload progress (0.0 to 1.0).
     * 
     * @return The reload progress, or 0.0 if not reloading
     */
    float getReloadProgress();
    
    /**
     * Starts reloading this weapon.
     * 
     * @return Whether reloading was started successfully
     */
    boolean startReloading();
    
    /**
     * Cancels reloading this weapon.
     */
    void cancelReloading();
    
    /**
     * Checks if this weapon is currently reloading.
     * 
     * @return Whether the weapon is reloading
     */
    boolean isReloading();
    
    /**
     * Fires this weapon.
     * 
     * @param shooter The entity firing the weapon
     * @param level The world level
     * @param position The firing position
     * @param direction The firing direction
     * @return Whether the weapon was fired successfully
     */
    boolean fire(LivingEntity shooter, Level level, Vec3 position, Vec3 direction);
    
    /**
     * Gets the fire mode of this weapon.
     * 
     * @return The fire mode
     */
    FireMode getFireMode();
    
    /**
     * Sets the fire mode of this weapon.
     * 
     * @param mode The new fire mode
     * @return Whether the fire mode was set successfully
     */
    boolean setFireMode(FireMode mode);
    
    /**
     * Gets all fire modes this weapon supports.
     * 
     * @return A set of supported fire modes
     */
    Set<FireMode> getSupportedFireModes();
    
    /**
     * Gets all attachments installed on this weapon.
     * 
     * @return A list of installed attachments
     */
    List<IWeaponAttachment> getAttachments();
    
    /**
     * Installs an attachment on this weapon.
     * 
     * @param attachment The attachment to install
     * @return Whether the attachment was installed successfully
     */
    boolean installAttachment(IWeaponAttachment attachment);
    
    /**
     * Removes an attachment from this weapon.
     * 
     * @param attachmentId The ID of the attachment to remove
     * @return The removed attachment, or null if not found
     */
    IWeaponAttachment removeAttachment(UUID attachmentId);
    
    /**
     * Saves this weapon's state to NBT.
     * 
     * @param tag The tag to save to
     * @return The updated tag
     */
    CompoundTag save(CompoundTag tag);
    
    /**
     * Loads this weapon's state from NBT.
     * 
     * @param tag The tag to load from
     */
    void load(CompoundTag tag);
    
    /**
     * Types of weapons by category.
     */
    enum WeaponCategory {
        PISTOL,
        RIFLE,
        SHOTGUN,
        SNIPER,
        HEAVY,
        ENERGY_SMALL,
        ENERGY_MEDIUM,
        ENERGY_HEAVY,
        EXPLOSIVE,
        MELEE,
        SPECIAL
    }
    
    /**
     * Types of damage a weapon can inflict.
     */
    enum DamageType {
        KINETIC,
        ENERGY,
        THERMAL,
        EXPLOSIVE,
        PLASMA,
        LASER,
        CRYO,
        ACID,
        EMP,
        ANTIMATTER
    }
    
    /**
     * Fire modes for weapons.
     */
    enum FireMode {
        SINGLE,
        BURST,
        AUTO,
        CHARGED
    }
}