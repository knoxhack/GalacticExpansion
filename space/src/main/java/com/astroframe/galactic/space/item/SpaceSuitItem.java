package com.astroframe.galactic.space.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;

/**
 * Space suit armor item with special properties for space survival.
 */
public class SpaceSuitItem extends ArmorItem {
    
    private static final SpaceSuitArmorMaterial MATERIAL = new SpaceSuitArmorMaterial();
    
    /**
     * Create a new space suit item.
     *
     * @param type The armor type (helmet, chestplate, etc.)
     * @param properties The item properties
     */
    public SpaceSuitItem(Type type, Properties properties) {
        super(MATERIAL, type, properties);
    }
    
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
    
    @Override
    public int getEnchantmentValue() {
        return 15;
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.RESPIRATION && getType() == Type.HELMET) {
            return true;
        }
        
        if (enchantment == Enchantments.AQUA_AFFINITY && getType() == Type.HELMET) {
            return true;
        }
        
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }
    
    /**
     * Checks if a full space suit is being worn by comparing armor items.
     *
     * @param helmet The helmet item stack
     * @param chestplate The chestplate item stack
     * @param leggings The leggings item stack
     * @param boots The boots item stack
     * @return Whether a full space suit is being worn
     */
    public static boolean isFullSpaceSuit(ItemStack helmet, ItemStack chestplate, 
            ItemStack leggings, ItemStack boots) {
        
        return helmet.getItem() instanceof SpaceSuitItem && 
                chestplate.getItem() instanceof SpaceSuitItem &&
                leggings.getItem() instanceof SpaceSuitItem && 
                boots.getItem() instanceof SpaceSuitItem;
    }
    
    /**
     * Private armor material implementation for the space suit.
     */
    private static class SpaceSuitArmorMaterial implements ArmorMaterial {
        
        private static final int[] DURABILITY_PER_SLOT = new int[]{13, 15, 16, 11};
        private static final int[] PROTECTION_PER_SLOT = new int[]{3, 6, 8, 3};
        
        @Override
        public int getDurabilityForType(Type type) {
            return DURABILITY_PER_SLOT[type.getSlot().getIndex()] * 25;
        }
        
        @Override
        public int getDefenseForType(Type type) {
            return PROTECTION_PER_SLOT[type.getSlot().getIndex()];
        }
        
        @Override
        public int getEnchantmentValue() {
            return 15;
        }
        
        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_IRON;
        }
        
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.EMPTY;
        }
        
        @Override
        public String getName() {
            return "space_suit";
        }
        
        @Override
        public float getToughness() {
            return 2.0F;
        }
        
        @Override
        public float getKnockbackResistance() {
            return 0.0F;
        }
    }
}