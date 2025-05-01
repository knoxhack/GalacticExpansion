package com.astroframe.galactic.core.api.weapon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

/**
 * Interface for weapon attachments in the Weaponry module.
 * This covers scopes, barrels, stocks, etc.
 */
public interface IWeaponAttachment {
    
    /**
     * Gets the unique identifier for this attachment instance.
     * 
     * @return The attachment UUID
     */
    UUID getAttachmentId();
    
    /**
     * Gets the registry ID for this attachment type.
     * 
     * @return The attachment type ID
     */
    ResourceLocation getAttachmentType();
    
    /**
     * Gets the category of this attachment.
     * 
     * @return The attachment category
     */
    AttachmentCategory getCategory();
    
    /**
     * Gets the tier of this attachment.
     * Higher tiers have better performance.
     * 
     * @return The attachment tier (1-5)
     */
    int getTier();
    
    /**
     * Gets the weight of this attachment in grams.
     * Higher weight affects weapon handling.
     * 
     * @return The attachment weight
     */
    float getWeight();
    
    /**
     * Modifies weapon accuracy when attached.
     * 
     * @param baseAccuracy The base weapon accuracy
     * @return The modified accuracy
     */
    float modifyAccuracy(float baseAccuracy);
    
    /**
     * Modifies weapon recoil when attached.
     * 
     * @param baseRecoil The base weapon recoil
     * @return The modified recoil
     */
    float modifyRecoil(float baseRecoil);
    
    /**
     * Modifies weapon damage when attached.
     * 
     * @param baseDamage The base weapon damage
     * @return The modified damage
     */
    float modifyDamage(float baseDamage);
    
    /**
     * Modifies weapon range when attached.
     * 
     * @param baseRange The base weapon range
     * @return The modified range
     */
    float modifyRange(float baseRange);
    
    /**
     * Modifies weapon fire rate when attached.
     * 
     * @param baseFireRate The base weapon fire rate
     * @return The modified fire rate
     */
    float modifyFireRate(float baseFireRate);
    
    /**
     * Modifies weapon spread angle when attached.
     * 
     * @param baseSpreadAngle The base weapon spread angle
     * @return The modified spread angle
     */
    float modifySpreadAngle(float baseSpreadAngle);
    
    /**
     * Called when the weapon with this attachment is fired.
     * 
     * @param weapon The weapon that was fired
     * @param shooter The entity firing the weapon
     * @param level The world level
     * @param position The firing position
     * @param direction The firing direction
     */
    void onWeaponFired(IWeapon weapon, LivingEntity shooter, Level level, Vec3 position, Vec3 direction);
    
    /**
     * Called when this attachment is installed on a weapon.
     * 
     * @param weapon The weapon this attachment was installed on
     */
    void onInstalled(IWeapon weapon);
    
    /**
     * Called when this attachment is removed from a weapon.
     * 
     * @param weapon The weapon this attachment was removed from
     */
    void onRemoved(IWeapon weapon);
    
    /**
     * Saves this attachment's state to NBT.
     * 
     * @param tag The tag to save to
     * @return The updated tag
     */
    CompoundTag save(CompoundTag tag);
    
    /**
     * Loads this attachment's state from NBT.
     * 
     * @param tag The tag to load from
     */
    void load(CompoundTag tag);
    
    /**
     * Types of weapon attachments by category.
     */
    enum AttachmentCategory {
        SCOPE,
        BARREL,
        STOCK,
        GRIP,
        MAGAZINE,
        UNDER_BARREL,
        MUZZLE,
        LASER_SIGHT,
        TACTICAL_LIGHT,
        ENERGY_CELL,
        COOLING_SYSTEM
    }
}