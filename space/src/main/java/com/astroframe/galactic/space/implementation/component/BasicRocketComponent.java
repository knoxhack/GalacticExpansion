package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.space.util.TagHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

/**
 * Basic implementation of a rocket component.
 */
public class BasicRocketComponent implements IRocketComponent {

    private final RocketComponentType type;
    private final int tier;
    private int durability;
    private int maxDurability;
    private int mass;
    private ResourceLocation id;
    private String name;
    private String description;
    private Vec3 position = new Vec3(0, 0, 0);
    
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
        this.mass = calculateMass(tier);
        this.id = ResourceLocation.parse("galactic:" + type.toString().toLowerCase() + "_t" + tier);
        this.name = "Tier " + tier + " " + type.toString();
        this.description = "A tier " + tier + " " + type.toString().toLowerCase() + " component";
    }
    
    /**
     * Create a new basic rocket component with a custom ID.
     *
     * @param id The component ID
     * @param type The component type
     * @param tier The tier level
     * @param mass The component mass
     */
    public BasicRocketComponent(ResourceLocation id, RocketComponentType type, int tier, int mass) {
        this.id = id;
        this.type = type;
        this.tier = tier;
        this.maxDurability = calculateMaxDurability(tier);
        this.durability = this.maxDurability;
        this.mass = mass;
        this.name = "Tier " + tier + " " + type.toString();
        this.description = "A tier " + tier + " " + type.toString().toLowerCase() + " component";
    }
    
    @Override
    public ResourceLocation getId() {
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
    
    /**
     * Calculate the mass based on tier.
     *
     * @param tier The tier level
     * @return The component mass
     */
    private int calculateMass(int tier) {
        return tier * 10;
    }
    
    @Override
    public RocketComponentType getType() {
        return type;
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
    public int getCurrentDurability() {
        return durability;
    }
    
    @Override
    public int getMaxDurability() {
        return maxDurability;
    }
    
    public int getDurability() {
        return durability;
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
    public Vec3 getPosition() {
        return position;
    }
    
    @Override
    public void setPosition(Vec3 position) {
        this.position = position;
    }
    
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", type.toString());
        tag.putInt("tier", tier);
        tag.putInt("durability", durability);
        tag.putInt("maxDurability", maxDurability);
        tag.putInt("mass", mass);
        tag.putString("id", id.toString());
        tag.putString("name", name);
        tag.putString("description", description);
        
        // Save position
        tag.putDouble("posX", position.x);
        tag.putDouble("posY", position.y);
        tag.putDouble("posZ", position.z);
        
        return tag;
    }
    
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
        
        // Only update these if they exist in the tag
        if (tag.contains("mass")) {
            this.mass = TagHelper.getInt(tag, "mass");
        }
        
        if (tag.contains("name")) {
            this.name = TagHelper.getString(tag, "name");
        }
        
        if (tag.contains("description")) {
            this.description = TagHelper.getString(tag, "description");
        }
        
        // Load position
        if (tag.contains("posX") && tag.contains("posY") && tag.contains("posZ")) {
            double x = TagHelper.getDouble(tag, "posX");
            double y = TagHelper.getDouble(tag, "posY");
            double z = TagHelper.getDouble(tag, "posZ");
            this.position = new Vec3(x, y, z);
        }
    }
    
    @Override
    public void save(CompoundTag tag) {
        tag.putString("ID", getId().toString());
        tag.putString("Type", getType().name());
        tag.putString("Name", getName());
        tag.putString("Description", getDescription());
        tag.putInt("Tier", getTier());
        tag.putInt("Mass", getMass());
        tag.putInt("MaxDurability", getMaxDurability());
        tag.putInt("CurrentDurability", getCurrentDurability());
        
        // Save position
        Vec3 pos = getPosition();
        if (pos.x != 0 || pos.y != 0 || pos.z != 0) {
            tag.putDouble("PosX", pos.x);
            tag.putDouble("PosY", pos.y);
            tag.putDouble("PosZ", pos.z);
        }
    }
    
    @Override
    public void load(CompoundTag tag) {
        // Load position if saved
        if (tag.contains("PosX") && tag.contains("PosY") && tag.contains("PosZ")) {
            try {
                double x = TagHelper.getDouble(tag, "PosX", 0.0);
                double y = TagHelper.getDouble(tag, "PosY", 0.0);
                double z = TagHelper.getDouble(tag, "PosZ", 0.0);
                setPosition(new Vec3(x, y, z));
            } catch (Exception e) {
                setPosition(new Vec3(0, 0, 0));
            }
        }
        
        if (tag.contains("CurrentDurability")) {
            this.durability = TagHelper.getInt(tag, "CurrentDurability", this.durability);
        }
        
        if (tag.contains("MaxDurability")) {
            this.maxDurability = TagHelper.getInt(tag, "MaxDurability", this.maxDurability);
        }
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