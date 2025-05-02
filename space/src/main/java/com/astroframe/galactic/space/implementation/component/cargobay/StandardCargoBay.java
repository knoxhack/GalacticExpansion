package com.astroframe.galactic.space.implementation.component.cargobay;

import com.astroframe.galactic.core.api.space.component.ICargoBay;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.space.implementation.component.base.AbstractSpaceModuleComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard implementation of a cargo bay component.
 * Provides storage for items during space travel with configurable features
 * based on tier level.
 */
public class StandardCargoBay extends AbstractSpaceModuleComponent implements ICargoBay {
    
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
        super(id, RocketComponentType.CARGO_BAY, name, description, tier, mass);
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
        super.save(tag);
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
        super.load(tag);
        
        // Load items
        items.clear();
        if (tag.contains("Items", 9)) { // 9 is the tag type for a list
            ListTag itemsTag = tag.getList("Items", 10); // 10 is the tag type for a compound
            for (int i = 0; i < itemsTag.size(); i++) {
                CompoundTag itemTag = itemsTag.getCompound(i);
                String id = itemTag.getString("id");
                int count = itemTag.getInt("count");
                
                // In a real implementation, we would use the item registry to get the item
                // Since this is a simplified implementation, we'll just create a basic item stack
                ItemStack stack = new ItemStack(net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation(id)), count);
                
                // Load any tags
                if (itemTag.contains("tag", 10)) { // 10 is the tag type for a compound
                    stack.setTag(itemTag.getCompound("tag").copy());
                }
                
                items.add(stack);
            }
        }
    }
    
    @Override
    public String getDetailsString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getDetailsString()).append("\n");
        sb.append("Storage Capacity: ").append(maxCapacity).append(" kg\n");
        sb.append("Slot Count: ").append(maxSlots).append("\n");
        sb.append("Used Capacity: ").append(getCurrentUsedCapacity()).append(" kg\n");
        sb.append("Remaining Capacity: ").append(getRemainingCapacity()).append(" kg\n");
        sb.append("Security Features: ").append(securityFeatures ? "Yes" : "No").append("\n");
        sb.append("Environment Control: ").append(environmentControl ? "Yes" : "No").append("\n");
        sb.append("Automated Loading: ").append(automatedLoading ? "Yes" : "No");
        return sb.toString();
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