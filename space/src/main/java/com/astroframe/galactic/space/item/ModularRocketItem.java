package com.astroframe.galactic.space.item;

import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.ModularRocket;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.implementation.RocketLaunchController;
import com.astroframe.galactic.space.implementation.component.RocketComponentFactory;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Item representing a functional, craftable rocket for space travel.
 */
public class ModularRocketItem extends Item {
    
    /**
     * Creates a new modular rocket item.
     */
    public ModularRocketItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .fireResistant());
    }
    
    // Adds tooltip information
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, 
                              TooltipFlag flag) {
        IRocket rocket = getRocketFromStack(stack);
        if (rocket != null) {
            tooltip.add(Component.translatable("item.galactic-space.modular_rocket.tier", rocket.getTier())
                    .withStyle(ChatFormatting.GRAY));
            
            tooltip.add(Component.translatable("item.galactic-space.modular_rocket.fuel", 
                                             rocket.getFuelLevel(), 
                                             rocket.getFuelCapacity())
                    .withStyle(ChatFormatting.BLUE));
            
            // Add component information
            tooltip.add(Component.translatable("item.galactic-space.modular_rocket.components")
                    .withStyle(ChatFormatting.GOLD));
            
            for (RocketComponentType type : RocketComponentType.values()) {
                if (rocket.hasComponent(type)) {
                    tooltip.add(Component.literal(" - ")
                            .append(Component.translatable("component.galactic-space." + type.toString().toLowerCase()))
                            .withStyle(ChatFormatting.GRAY));
                }
            }
        } else {
            tooltip.add(Component.translatable("item.galactic-space.modular_rocket.invalid")
                    .withStyle(ChatFormatting.RED));
        }
    }
    
    // Handles right-click usage of the rocket
    @Override
    public net.minecraft.world.item.InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (level.isClientSide()) {
            return net.minecraft.world.item.InteractionResultHolder.success(stack);
        }
        
        IRocket rocket = getRocketFromStack(stack);
        if (rocket != null && player instanceof ServerPlayer serverPlayer) {
            // Attempt to launch the rocket
            RocketLaunchController launchController = new RocketLaunchController(serverPlayer, rocket);
            
            if (launchController.canLaunch()) {
                GalacticSpace.LOGGER.info("Player {} starting rocket launch sequence", player.getName().getString());
                launchController.startLaunchSequence();
                return net.minecraft.world.item.InteractionResultHolder.consume(stack);
            } else {
                // Report why launch failed
                Component reason = launchController.getCannotLaunchReason();
                serverPlayer.displayClientMessage(reason, false);
                return net.minecraft.world.item.InteractionResultHolder.fail(stack);
            }
        }
        
        return net.minecraft.world.item.InteractionResultHolder.pass(stack);
    }
    
    /**
     * Creates a new rocket item with default components.
     *
     * @return A new rocket item
     */
    public static ItemStack createBasicRocket() {
        ItemStack stack = new ItemStack(SpaceItems.MODULAR_ROCKET.get());
        
        // Create a basic rocket using builder pattern
        ModularRocket.Builder builder = new ModularRocket.Builder(
                net.minecraft.resources.ResourceLocation.parse("galactic:basic_rocket"));
        
        // Add basic components for a Tier 1 rocket
        builder.commandModule(RocketComponentFactory.createCockpit(RocketComponentType.COCKPIT, 1));
        builder.addEngine(RocketComponentFactory.createEngine(RocketComponentType.ENGINE, 1));
        builder.addFuelTank(RocketComponentFactory.createFuelTank(RocketComponentType.FUEL_TANK, 1));
        
        // We need at least these components for a valid rocket
        ModularRocket rocket = builder.build();
        
        // Set full fuel
        rocket.setFuelLevel(rocket.getFuelCapacity());
        
        // Save rocket to item
        saveRocketToStack(stack, rocket);
        
        return stack;
    }
    
    /**
     * Gets the rocket from an item stack.
     *
     * @param stack The item stack
     * @return The rocket, or null if invalid
     */
    // Cache for storing tags with ItemStacks (workaround for compatibility)
    private static final Map<Integer, CompoundTag> tagCache = new HashMap<>();
    
    /**
     * Gets the tag for an ItemStack, or null if none exists.
     * This is a compatibility method for NeoForge 1.21.5.
     */
    private static CompoundTag getItemTag(ItemStack stack) {
        return tagCache.get(System.identityHashCode(stack));
    }
    
    /**
     * Sets the tag for an ItemStack.
     * This is a compatibility method for NeoForge 1.21.5.
     */
    private static void setItemTag(ItemStack stack, CompoundTag tag) {
        if (tag == null) {
            tagCache.remove(System.identityHashCode(stack));
        } else {
            tagCache.put(System.identityHashCode(stack), tag);
        }
    }
    
    /**
     * Gets or creates a tag for an ItemStack.
     * This is a compatibility method for NeoForge 1.21.5.
     */
    private static CompoundTag getOrCreateItemTag(ItemStack stack) {
        CompoundTag tag = getItemTag(stack);
        if (tag == null) {
            tag = new CompoundTag();
            setItemTag(stack, tag);
        }
        return tag;
    }
    
    @Nullable
    public static IRocket getRocketFromStack(ItemStack stack) {
        if (stack.getItem() instanceof ModularRocketItem) {
            CompoundTag tag = getItemTag(stack);
            if (tag == null) {
                return null;
            }
            
            if (tag.contains("rocket")) {
                CompoundTag rocketTag = tag.getCompound("rocket");
                return ModularRocket.fromTag(rocketTag);
            }
        }
        
        return null;
    }
    
    /**
     * Saves a rocket to an item stack.
     *
     * @param stack The item stack
     * @param rocket The rocket
     */
    public static void saveRocketToStack(ItemStack stack, IRocket rocket) {
        if (stack.getItem() instanceof ModularRocketItem) {
            CompoundTag tag = getOrCreateItemTag(stack);
            
            CompoundTag rocketTag = new CompoundTag();
            rocket.saveToTag(rocketTag);
            tag.put("rocket", rocketTag);
        }
    }
}