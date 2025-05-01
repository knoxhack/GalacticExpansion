package com.astroframe.galactic.space.item;

import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.dimension.SpaceStationDimension;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Space suit armor item, provides protection in space environments.
 */
public abstract class SpaceSuitItem extends ArmorItem {

    private static final SpaceSuitMaterial MATERIAL = new SpaceSuitMaterial();
    private final int tier;

    /**
     * Create a new space suit item.
     * 
     * @param slot The equipment slot
     * @param tier The tier/level of the space suit (1-3)
     */
    public SpaceSuitItem(EquipmentSlot slot, int tier) {
        super(MATERIAL, typeForSlot(slot), new Properties().stacksTo(1).fireResistant().durability(800));
        this.tier = Math.max(1, Math.min(3, tier)); // Clamp between 1-3
        
        // Register event handler for environmental damage protection
        NeoForge.EVENT_BUS.addListener(this::onLivingDamage);
    }
    
    /**
     * Converts an EquipmentSlot to the corresponding ArmorItem.Type
     */
    private static ArmorItem.Type typeForSlot(EquipmentSlot slot) {
        return switch(slot) {
            case HEAD -> ArmorItem.Type.HELMET;
            case CHEST -> ArmorItem.Type.CHESTPLATE;
            case LEGS -> ArmorItem.Type.LEGGINGS;
            case FEET -> ArmorItem.Type.BOOTS;
            default -> throw new IllegalArgumentException("Invalid armor slot: " + slot);
        };
    }
    
    /**
     * Gets the tier/level of this space suit piece.
     * 
     * @return The tier (1-3)
     */
    public int getTier() {
        return tier;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, 
                              TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        
        tooltip.add(Component.translatable("item.galactic-space.space_suit.tier", getTier())
                .withStyle(ChatFormatting.GRAY));
        
        tooltip.add(Component.translatable("item.galactic-space.space_suit.protection")
                .withStyle(ChatFormatting.BLUE));
        
        if (getTier() >= 2) {
            tooltip.add(Component.translatable("item.galactic-space.space_suit.radiation")
                    .withStyle(ChatFormatting.GREEN));
        }
        
        if (getTier() >= 3) {
            tooltip.add(Component.translatable("item.galactic-space.space_suit.temperature")
                    .withStyle(ChatFormatting.RED));
        }
    }
    
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        
        // Apply effects when in hostile environments
        if (entity instanceof Player player && entity.tickCount % 20 == 0) {
            // Only apply effects when in space
            if (SpaceStationDimension.isSpaceStation(level)) {
                applySpaceEffects(player);
            }
        }
    }
    
    /**
     * Applies effects to the player based on their space suit protection.
     * 
     * @param player The player
     */
    private void applySpaceEffects(Player player) {
        // Check if player has complete space suit
        if (!hasFullSpaceSuit(player)) {
            // Apply negative effects if not wearing a complete suit
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0));
            
            // Add suffocation if no helmet
            if (!hasHelmet(player)) {
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0));
                
                // Damage from no oxygen
                if (player.getRandom().nextInt(10) == 0) {
                    player.hurt(player.damageSources().drown(), 1.0F);
                }
            }
        } else {
            // Apply positive effects for complete suit
            int minTier = getMinimumSuitTier(player);
            
            // Tier 2+ benefits
            if (minTier >= 2) {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, true, false));
            }
            
            // Tier 3 benefits
            if (minTier >= 3) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 0, true, false));
            }
        }
    }
    
    /**
     * Handles protection from environmental damage.
     * 
     * @param event The damage event
     */
    private void onLivingDamage(LivingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        
        // Only protect in space environments
        if (!SpaceStationDimension.isSpaceStation(player.level())) {
            return;
        }
        
        // Protect against various damage types based on suit tier
        int minTier = getMinimumSuitTier(player);
        
        if (hasFullSpaceSuit(player)) {
            if (event.getSource().is(net.minecraft.world.damagesource.DamageTypes.DROWN)) {
                // Protect from suffocation with any tier
                event.setCanceled(true);
                return;
            }
            
            if (minTier >= 2 && event.getSource().is(net.minecraft.world.damagesource.DamageTypes.MAGIC)) {
                // Tier 2+ protects from radiation (magic damage)
                event.setAmount(event.getAmount() * 0.5f);
            }
            
            if (minTier >= 3) {
                if (event.getSource().is(net.minecraft.world.damagesource.DamageTypes.FREEZE) ||
                    event.getSource().is(net.minecraft.world.damagesource.DamageTypes.IN_FIRE) ||
                    event.getSource().is(net.minecraft.world.damagesource.DamageTypes.ON_FIRE)) {
                    // Tier 3 protects from temperature extremes
                    event.setCanceled(true);
                }
            }
        }
    }
    
    /**
     * Checks if a player has a complete space suit.
     * 
     * @param player The player
     * @return true if wearing a complete space suit
     */
    public static boolean hasFullSpaceSuit(Player player) {
        return hasHelmet(player) && hasChestplate(player) && hasLeggings(player) && hasBoots(player);
    }
    
    /**
     * Checks if a player has a space suit helmet.
     * 
     * @param player The player
     * @return true if wearing a space suit helmet
     */
    public static boolean hasHelmet(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof SpaceSuitItem.Helmet;
    }
    
    /**
     * Checks if a player has a space suit chestplate.
     * 
     * @param player The player
     * @return true if wearing a space suit chestplate
     */
    public static boolean hasChestplate(Player player) {
        return player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof SpaceSuitItem.Chestplate;
    }
    
    /**
     * Checks if a player has space suit leggings.
     * 
     * @param player The player
     * @return true if wearing space suit leggings
     */
    public static boolean hasLeggings(Player player) {
        return player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof SpaceSuitItem.Leggings;
    }
    
    /**
     * Checks if a player has space suit boots.
     * 
     * @param player The player
     * @return true if wearing space suit boots
     */
    public static boolean hasBoots(Player player) {
        return player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof SpaceSuitItem.Boots;
    }
    
    /**
     * Gets the minimum tier of all equipped space suit pieces.
     * 
     * @param player The player
     * @return The minimum tier, or 0 if no space suit
     */
    public static int getMinimumSuitTier(Player player) {
        if (!hasFullSpaceSuit(player)) {
            return 0;
        }
        
        int minTier = Integer.MAX_VALUE;
        
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor()) {
                ItemStack stack = player.getItemBySlot(slot);
                if (stack.getItem() instanceof SpaceSuitItem suit) {
                    minTier = Math.min(minTier, suit.getTier());
                }
            }
        }
        
        return minTier == Integer.MAX_VALUE ? 0 : minTier;
    }
    
    /**
     * Space suit material definition.
     */
    private static class SpaceSuitMaterial implements ArmorMaterial {
        private static final int[] DURABILITY_PER_SLOT = new int[] {200, 280, 260, 220};
        private static final int[] PROTECTION_PER_SLOT = new int[] {3, 6, 5, 3};
        
        @Override
        public int getDurabilityForType(ArmorItem.Type armorType) {
            return DURABILITY_PER_SLOT[armorType.getSlot().getFilterFlag()];
        }
        
        @Override
        public int getDefenseForType(ArmorItem.Type armorType) {
            return PROTECTION_PER_SLOT[armorType.getSlot().getFilterFlag()];
        }
        
        @Override
        public int getEnchantmentValue() {
            return 15;
        }
        
        @Override
        public net.minecraft.sounds.SoundEvent getEquipSound() {
            return net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_IRON;
        }
        
        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(Tags.Items.INGOTS_IRON);
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
            return 0.1F;
        }
    }
    
    /**
     * Space suit helmet implementation.
     */
    public static class Helmet extends SpaceSuitItem {
        public Helmet() {
            super(EquipmentSlot.HEAD, 1);
        }
    }
    
    /**
     * Space suit chestplate implementation.
     */
    public static class Chestplate extends SpaceSuitItem {
        public Chestplate() {
            super(EquipmentSlot.CHEST, 1);
        }
    }
    
    /**
     * Space suit leggings implementation.
     */
    public static class Leggings extends SpaceSuitItem {
        public Leggings() {
            super(EquipmentSlot.LEGS, 1);
        }
    }
    
    /**
     * Space suit boots implementation.
     */
    public static class Boots extends SpaceSuitItem {
        public Boots() {
            super(EquipmentSlot.FEET, 1);
        }
    }
}