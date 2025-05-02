package com.astroframe.galactic.space.implementation.component.cargobay;

import com.astroframe.galactic.core.api.space.component.ICargoBay;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.space.implementation.component.ResourceLocationHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard implementation of a cargo bay component.
 * Provides storage for items during space travel with configurable features
 * based on tier level.
 */
public class StandardCargoBay implements ICargoBay {
    
    private final ResourceLocation id;
    private final RocketComponentType type;
    private final String name;
    private final String description;
    private final int tier;
    private final int mass;
    private int durability;
    private int maxDurability;
    
    private final int maxCapacity;
    private final int maxSlots;
    private final boolean securityFeatures;
    private final boolean environmentControl;
    private final boolean automatedLoading;
    private final List<ItemStack> items;
    
    /**
     * Creates a new standard cargo bay.
     *
     * @param id The component ID
     * @param name The component name
     * @param description The component description
     * @param tier The component tier
     * @param mass The component mass
     * @param maxCapacity The maximum weight capacity in kg
     * @param securityFeatures Whether the cargo bay has security features
     * @param environmentControl Whether the cargo bay has environment control
     * @param automatedLoading Whether the cargo bay has automated loading/unloading
     */
    public StandardCargoBay(ResourceLocation id, String name, String description, 
                          int tier, int mass, int maxCapacity, 
                          boolean securityFeatures, boolean environmentControl, 
                          boolean automatedLoading) {
        this.id = id;
        this.type = RocketComponentType.CARGO_BAY;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.mass = mass;
        this.maxDurability = 100 + (tier * 50); // Base 100 + 50 per tier
        this.durability = this.maxDurability;
        this.maxCapacity = maxCapacity;
        this.maxSlots = 9 * tier; // 9 slots per tier (like a chest)
        this.securityFeatures = securityFeatures;
        this.environmentControl = environmentControl;
        this.automatedLoading = automatedLoading;
        this.items = new ArrayList<>();
    }
    
    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    @Override
    public int getMaxSlots() {
        return maxSlots;
    }
    
    @Override
    public int getCurrentUsedCapacity() {
        int usedCapacity = 0;
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                usedCapacity += calculateItemWeight(stack);
            }
        }
        return usedCapacity;
    }
    
    /**
     * Gets the remaining cargo capacity in kg
     * 
     * @return The remaining capacity
     */
    public int getRemainingCapacity() {
        return maxCapacity - getCurrentUsedCapacity();
    }
    
    @Override
    public List<ItemStack> getItems() {
        return new ArrayList<>(items);
    }
    
    @Override
    public boolean addItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        
        // Check if there's enough space
        if (items.size() >= maxSlots) {
            return false;
        }
        
        float itemWeight = calculateItemWeight(stack);
        if (getCurrentUsedCapacity() + itemWeight > maxCapacity) {
            return false;
        }
        
        // Add the item
        items.add(stack.copy());
        return true;
    }
    
    @Override
    public ItemStack removeItem(int index) {
        if (index < 0 || index >= items.size()) {
            return ItemStack.EMPTY;
        }
        
        return items.remove(index);
    }
    
    @Override
    public boolean hasSecurityFeatures() {
        return securityFeatures;
    }
    
    @Override
    public boolean hasEnvironmentControl() {
        return environmentControl;
    }
    
    @Override
    public boolean hasAutomatedLoading() {
        return automatedLoading;
    }
    
    @Override
    public void save(CompoundTag tag) {
        // Save component data
        tag.putString("ID", id.toString());
        tag.putString("Type", type.name());
        tag.putString("Name", name);
        tag.putString("Description", description);
        tag.putInt("Tier", tier);
        tag.putInt("Mass", mass);
        tag.putInt("Durability", durability);
        tag.putInt("MaxDurability", maxDurability);
        
        // Save cargo bay specific data
        tag.putInt("MaxCapacity", maxCapacity);
        tag.putInt("MaxSlots", maxSlots);
        tag.putBoolean("SecurityFeatures", securityFeatures);
        tag.putBoolean("EnvironmentControl", environmentControl);
        tag.putBoolean("AutomatedLoading", automatedLoading);
        
        // Save items
        ListTag itemsTag = new ListTag();
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putString("id", stack.getItem().toString());
                itemTag.putInt("count", stack.getCount());
                
                // Add any tags the item might have
                // Get item tag data and add it if not empty
                CompoundTag itemTagData = stack.getTag();
                if (itemTagData != null && !itemTagData.isEmpty()) {
                    itemTag.put("tag", itemTagData);
                }
                
                itemsTag.add(itemTag);
            }
        }
        
        tag.put("Items", itemsTag);
    }
    
    @Override
    public void load(CompoundTag tag) {
        // Not loading ID, type, etc. as those are set in constructor
        
        // Load durability if it exists
        if (tag.contains("Durability")) {
            this.durability = tag.getInt("Durability");
        }
        
        // Load items
        items.clear();
        if (tag.contains("Items", 9)) { // 9 is the tag type for a list
            ListTag itemsTag = tag.getList("Items", 10); // 10 is the tag type for compound
            for (int i = 0; i < itemsTag.size(); i++) {
                CompoundTag itemTag = itemsTag.getCompound(i);
                String id = itemTag.getString("id");
                int count = itemTag.getInt("count");
                
                // Use ItemHelper or similar to create item from ID
                ResourceLocation itemId = ResourceLocationHelper.create("minecraft", id);
                ItemStack stack = null;
                
                // Create the item stack - we need to handle potential null values properly
                try {
                    net.minecraft.world.item.Item item = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(itemId);
                    if (item != null && item != net.minecraft.world.item.Items.AIR) {
                        stack = new ItemStack(item, count);
                    } else {
                        continue; // Skip this invalid item
                    }
                } catch (Exception e) {
                    continue; // Skip this invalid item
                }
                
                // Load any tags
                if (itemTag.contains("tag", 10)) { // 10 is the tag type for compound
                    CompoundTag tagCompound = itemTag.getCompound("tag");
                    stack.getOrCreateTag().merge(tagCompound);
                }
                
                items.add(stack);
            }
        }
    }
    
    @Override
    public String getDetailsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(id).append("\n");
        sb.append("Type: ").append(type).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Description: ").append(description).append("\n");
        sb.append("Tier: ").append(tier).append("\n");
        sb.append("Mass: ").append(mass).append(" kg\n");
        sb.append("Durability: ").append(durability).append("/").append(maxDurability).append("\n");
        sb.append("Storage Capacity: ").append(maxCapacity).append(" kg\n");
        sb.append("Slot Count: ").append(maxSlots).append("\n");
        sb.append("Used Capacity: ").append(getCurrentUsedCapacity()).append(" kg\n");
        sb.append("Remaining Capacity: ").append(getRemainingCapacity()).append(" kg\n");
        sb.append("Security Features: ").append(securityFeatures ? "Yes" : "No").append("\n");
        sb.append("Environment Control: ").append(environmentControl ? "Yes" : "No").append("\n");
        sb.append("Automated Loading: ").append(automatedLoading ? "Yes" : "No");
        return sb.toString();
    }
    
    @Override
    public ResourceLocation getId() {
        return id;
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
    public int getDurability() {
        return durability;
    }
    
    @Override
    public int getMaxDurability() {
        return maxDurability;
    }
    
    @Override
    public void setDurability(int durability) {
        this.durability = Math.max(0, Math.min(durability, maxDurability));
    }
    
    @Override
    public boolean damage(int amount) {
        this.durability = Math.max(0, this.durability - amount);
        return this.durability > 0;
    }
    
    @Override
    public boolean repair(int amount) {
        if (this.durability < this.maxDurability) {
            this.durability = Math.min(this.maxDurability, this.durability + amount);
            return true;
        }
        return false;
    }
    
    @Override
    public Vec3 getPosition() {
        // Default implementation - for a full implementation,
        // we would store and return a proper position
        return new Vec3(0, 0, 0);
    }
    
    @Override
    public void setPosition(Vec3 position) {
        // Default implementation for a fixed component
        // Would normally store the position
    }
    
    /**
     * Creates a more accurate weight calculation based on material type
     * This implementation improves on the default by considering the item's properties
     */
    @Override
    public float calculateItemWeight(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0.0f;
        }
        
        // Base weight is 0.5kg per item
        float baseWeight = stack.getCount() * 0.5f;
        
        // Adjust weight based on item properties - this could be expanded with a full item weight system
        String itemId = stack.getItem().toString();
        
        // Heavier items
        if (itemId.contains("block") || itemId.contains("ore")) {
            baseWeight *= 2.0f; // Blocks and ores are heavier
        }
        // Lighter items
        else if (itemId.contains("sapling") || itemId.contains("seed") || itemId.contains("flower")) {
            baseWeight *= 0.2f; // Natural items are lighter
        }
        
        return baseWeight;
    }
}