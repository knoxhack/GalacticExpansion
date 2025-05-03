package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.space.util.TagHelper;
import net.minecraft.nbt.CompoundTag;

/**
 * Basic implementation of a rocket component.
 */
public class BasicRocketComponent implements IRocketComponent {

    private final RocketComponentType type;
    private final int tier;
    private int durability;
    private int maxDurability;
    private net.minecraft.resources.ResourceLocation id;
    
    /**
     * Create a new basic rocket component.
     *
     * @param type The component type
     * @param tier The tier level
     */
    public BasicRocketComponent(RocketComponentType type, int tier) {
        this.type = type;
        this.tier = tier;
        this.maxDurability = calculateMaxDurability(tier);
        this.durability = this.maxDurability;
        this.id = new net.minecraft.resources.ResourceLocation("galactic", 
            type.toString().toLowerCase() + "_t" + tier);
    }
    
    @Override
    public net.minecraft.resources.ResourceLocation getId() {
        return this.id;
    }
    
    /**
     * Calculate the maximum durability based on tier.
     *
     * @param tier The tier level
     * @return The maximum durability
     */
    private int calculateMaxDurability(int tier) {
        return tier * 100;
    }
    
    @Override
    public RocketComponentType getType() {
        return type;
    }
    
    @Override
    public int getTier() {
        return tier;
    }
    
    @Override
    public int getDurability() {
        return durability;
    }
    
    @Override
    public int getCurrentDurability() {
        return durability;
    }
    
    @Override
    public int getMaxDurability() {
        return maxDurability;
    }
    
    @Override
    public double getMass() {
        return 10.0 * tier;
    }
    
    @Override
    public void damage(int amount) {
        this.durability = Math.max(0, this.durability - amount);
    }
    
    @Override
    public void repair(int amount) {
        this.durability = Math.min(this.maxDurability, this.durability + amount);
    }
    
    @Override
    public boolean isBroken() {
        return this.durability <= 0;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", type.toString());
        tag.putInt("tier", tier);
        tag.putInt("durability", durability);
        tag.putInt("maxDurability", maxDurability);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        // For type and tier, we just verify they match what we expect
        // since these are immutable after creation
        String typeStr = TagHelper.getString(tag, "type");
        if (!typeStr.equals(type.toString())) {
            throw new IllegalArgumentException("Component type mismatch");
        }
        
        int tagTier = TagHelper.getInt(tag, "tier");
        if (tagTier != tier) {
            throw new IllegalArgumentException("Component tier mismatch");
        }
        
        this.durability = TagHelper.getInt(tag, "durability");
        this.maxDurability = TagHelper.getInt(tag, "maxDurability");
    }
    
    public static BasicRocketComponent fromTag(CompoundTag tag) {
        String typeStr = TagHelper.getString(tag, "type");
        RocketComponentType type = RocketComponentType.valueOf(typeStr);
        int tier = TagHelper.getInt(tag, "tier");
        
        BasicRocketComponent component = new BasicRocketComponent(type, tier);
        component.deserializeNBT(tag);
        return component;
    }
}