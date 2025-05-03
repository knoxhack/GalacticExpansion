package com.astroframe.galactic.space.implementation.component.cargobay;

import com.astroframe.galactic.core.api.space.component.ICargoBay;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.space.util.ResourceLocationHelper;
import com.astroframe.galactic.space.items.ItemStackHelper;
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
        try {
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
                    try {
                        CompoundTag itemTag = new CompoundTag();
                        
                        // Save basic item data
                        ResourceLocation itemId = null;
                        try {
                            // Try to get the registry name of the item
                            itemId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem());
                            itemTag.putString("id", itemId.toString());
                        } catch (Exception e) {
                            // Fallback to a direct toString if the registry lookup fails
                            itemTag.putString("id", stack.getItem().toString());
                        }
                        
                        itemTag.putInt("count", stack.getCount());
                        
                        // Add any tags the item might have
                        CompoundTag itemTagData = ItemStackHelper.getTag(stack);
                        if (itemTagData != null && !itemTagData.isEmpty()) {
                            itemTag.put("tag", itemTagData);
                        }
                        
                        itemsTag.add(itemTag);
                    } catch (Exception e) {
                        // Skip this item on error
                    }
                }
            }
            
            tag.put("Items", itemsTag);
        } catch (Exception e) {
            // Catch any serialization errors to avoid crashing
        }
    }
    
    @Override
    public void load(CompoundTag tag) {
        // Not loading ID, type, etc. as those are set in constructor
        
        // Load durability if it exists
        if (tag.contains("Durability")) {
            this.durability = ItemStackHelper.getInt(tag, "Durability");
        }
        
        // Load items
        items.clear();
        if (tag.contains("Items")) {
            try {
                // Get the items list tag using our helper
                ListTag itemsTag = ItemStackHelper.getList(tag, "Items", 10); // 10 is CompoundTag type
                
                if (itemsTag == null) {
                    return; // No items to load
                }
                
                // Process each item in the list
                for (int i = 0; i < itemsTag.size(); i++) {
                    CompoundTag itemTag = ItemStackHelper.getCompound(itemsTag, i);
                    
                    try {
                        if (itemTag == null) {
                            continue;
                        }
                        
                        // Extract item data
                        String itemIdStr = ItemStackHelper.getString(itemTag, "id");
                        int count = ItemStackHelper.getInt(itemTag, "count");
                        
                        if (itemIdStr.isEmpty() || count <= 0) {
                            continue;
                        }
                        
                        // Create the stack using our helper
                        ResourceLocation itemId = null;
                        if (itemIdStr.contains(":")) {
                            // If it already contains a namespace, parse it directly
                            itemId = ResourceLocationHelper.parse(itemIdStr);
                        } else {
                            // Otherwise assume minecraft namespace
                            itemId = ResourceLocationHelper.create("minecraft", itemIdStr);
                        }
                        ItemStack stack = ItemStackHelper.createStack(itemId, count);
                        
                        if (stack.isEmpty()) {
                            continue;
                        }
                        
                        // Handle any item tags
                        if (itemTag.contains("tag")) {
                            CompoundTag tagData = ItemStackHelper.getCompound(itemTag, "tag");
                            if (tagData != null) {
                                ItemStackHelper.setTag(stack, tagData);
                            }
                        }
                        
                        // Add the item to our list
                        items.add(stack);
                    } catch (Exception e) {
                        // Skip this item on any error
                    }
                }
            } catch (Exception e) {
                // If anything goes wrong, just continue with an empty list
            }
        }
    }
    
    /**
     * Get a detailed string representation of this component.
     * This is not an override as it's not in the component interface.
     * 
     * @return A detailed string describing this component
     */
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
    
    /**
     * Gets the current durability of this component.
     * @return The current durability
     */
    @Override
    public int getCurrentDurability() {
        return durability;
    }
    
    @Override
    public int getMaxDurability() {
        return maxDurability;
    }
    
    /**
     * Sets the current durability to a specific value.
     * This is a utility method not in the interface.
     * 
     * @param durability The new durability value
     */
    public void setDurability(int durability) {
        this.durability = Math.max(0, Math.min(durability, maxDurability));
    }
    
    /**
     * Damages this component by the given amount.
     * 
     * @param amount The amount of damage to apply
     */
    @Override
    public void damage(int amount) {
        this.durability = Math.max(0, this.durability - amount);
    }
    
    /**
     * Repairs this component by the given amount.
     * 
     * @param amount The amount to repair
     */
    @Override
    public void repair(int amount) {
        if (this.durability < this.maxDurability) {
            this.durability = Math.min(this.maxDurability, this.durability + amount);
        }
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
        try {
            // Try to get the registry name of the item
            ResourceLocation itemId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem());
            String itemPath = itemId != null ? itemId.getPath() : "";
            
            // Heavier items
            if (itemPath.contains("block") || itemPath.contains("ore")) {
                baseWeight *= 2.0f; // Blocks and ores are heavier
            }
            // Lighter items
            else if (itemPath.contains("sapling") || itemPath.contains("seed") || itemPath.contains("flower")) {
                baseWeight *= 0.2f; // Natural items are lighter
            }
        } catch (Exception e) {
            // If we can't get the registry name, just use the base weight
        }
        
        return baseWeight;
    }
}