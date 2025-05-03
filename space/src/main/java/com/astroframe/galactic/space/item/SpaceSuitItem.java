package com.astroframe.galactic.space.item;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.UUID;

/**
 * Space suit armor item with special properties for space survival.
 */
public class SpaceSuitItem extends Item {
    
    private final ArmorItem.Type armorType;
    
    /**
     * Create a new space suit item.
     *
     * @param armorType The armor type (helmet, chestplate, etc.)
     * @param properties The item properties
     */
    public SpaceSuitItem(ArmorItem.Type armorType, Properties properties) {
        super(properties);
        this.armorType = armorType;
    }
    
    /**
     * Get the armor type this suit is for.
     * 
     * @return The armor type
     */
    public ArmorItem.Type getArmorType() {
        return this.armorType;
    }
    
    /**
     * Get the equipment slot this armor is for.
     * 
     * @return The equipment slot
     */
    public EquipmentSlot getSlot() {
        return this.armorType.getSlot();
    }
    
    // Enchantment behavior methods
    // These methods are from Item class so they are valid overrides
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
    
    public int getEnchantmentValue() {
        return 15;
    }
    
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        ResourceLocation enchLocation = null;
        try {
            // In NeoForge 1.21.5, we need to get it directly using reflection for compatibility
            try {
                // Try direct method to get registry key for the enchantment
                // Looking up the registry path first by inspecting the class name
                String className = enchantment.getClass().getName();
                // Get the registry name from common registry paths
                if (className.contains("minecraft")) {
                    // Check for built-in enchantments
                    try {
                        // Try to use reflection to find the ENCHANTMENT registry
                        // The field name might vary in NeoForge 1.21.5
                        java.lang.reflect.Field field = null;
                        try {
                            field = net.minecraft.core.registries.BuiltInRegistries.class.getDeclaredField("ENCHANTMENT");
                        } catch (NoSuchFieldException e) {
                            // Try alternative field names
                            try {
                                field = net.minecraft.core.registries.BuiltInRegistries.class.getDeclaredField("ENCHANTMENTS");
                            } catch (NoSuchFieldException e2) {
                                // Try Registries class instead
                                field = net.minecraft.core.registries.Registries.class.getDeclaredField("ENCHANTMENT");
                            }
                        }
                        field.setAccessible(true);
                        net.minecraft.core.Registry<?> registry = (net.minecraft.core.Registry<?>) field.get(null);
                        
                        // Now try to call getKey method on the registry
                        java.lang.reflect.Method getKeyMethod = registry.getClass().getMethod("getKey", Object.class);
                        enchLocation = (ResourceLocation) getKeyMethod.invoke(registry, enchantment);
                    } catch (Exception ex) {
                        // Fallback below if reflection fails
                    }
                }
            } catch (Exception ex) {
                // Reflection failed, will use fallback below
            }
            if (enchLocation == null) {
                // If not in registry, try getting from class name as fallback
                String simpleName = enchantment.getClass().getSimpleName().toLowerCase();
                // Use ResourceLocation.of() instead of constructor in NeoForge 1.21.5
                enchLocation = ResourceLocation.parse("minecraft:" + simpleName);
            }
        } catch (Exception e) {
            return false;
        }
        
        if (enchLocation != null) {
            String path = enchLocation.getPath();
            
            // For helmet-specific enchantments
            if (this.armorType == ArmorItem.Type.HELMET && 
                (path.contains("respiration") || path.contains("aqua_affinity"))) {
                return true;
            }
        }
        
        // Use basic item compatibility check
        return enchantment.canEnchant(stack);
    }
    
    /**
     * Checks if a player is wearing a full space suit.
     *
     * @param player The player to check
     * @return True if wearing a full space suit
     */
    public static boolean hasFullSpaceSuit(Player player) {
        // Check all armor equipment slots
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            // Skip non-armor slots
            if (!slot.isArmor()) continue;
            
            ItemStack stack = player.getItemBySlot(slot);
            // Check if empty or not a SpaceSuitItem by item registry ID
            if (stack.isEmpty() || !isSpaceSuit(stack)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if an ItemStack is a space suit item
     * 
     * @param stack The item stack to check
     * @return True if it's a space suit
     */
    private static boolean isSpaceSuit(ItemStack stack) {
        if (stack.isEmpty()) return false;
        
        Item item = stack.getItem();
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
        return itemId != null && itemId.getPath().contains("space_suit");
    }
    
    /**
     * Get the minimum tier of any piece of space suit armor.
     * Returns 0 if no space suit is worn.
     *
     * @param player The player to check
     * @return The minimum tier (1-3) or 0 if no suit
     */
    public static int getMinimumSuitTier(Player player) {
        int minTier = 3; // Start with maximum
        boolean hasSuit = false;
        
        // Check each armor slot
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmor()) continue;
            
            ItemStack stack = player.getItemBySlot(slot);
            if (!stack.isEmpty() && isSpaceSuit(stack)) {
                hasSuit = true;
                int tier = 1; // Default to tier 1
                minTier = Math.min(minTier, tier);
            }
        }
        
        return hasSuit ? minTier : 0;
    }
    
    /**
     * Checks if a full space suit is being worn by comparing armor items.
     */
    public static boolean isFullSpaceSuit(ItemStack helmet, ItemStack chestplate, 
            ItemStack leggings, ItemStack boots) {
        
        return !helmet.isEmpty() && isSpaceSuit(helmet) && 
                !chestplate.isEmpty() && isSpaceSuit(chestplate) &&
                !leggings.isEmpty() && isSpaceSuit(leggings) && 
                !boots.isEmpty() && isSpaceSuit(boots);
    }
}