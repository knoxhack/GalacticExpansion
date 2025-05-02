package com.astroframe.galactic.space.items;

import com.astroframe.galactic.core.api.space.ModularRocket;
import com.astroframe.galactic.core.api.space.component.*;
import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.implementation.component.RocketComponentFactory;
import com.astroframe.galactic.space.implementation.component.ShieldFactory;
import com.astroframe.galactic.space.implementation.component.ResourceLocationHelper;
import com.astroframe.galactic.space.util.TagHelper;
import static com.astroframe.galactic.space.items.ItemStackHelper.getOrCreateTag;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Item representing a customizable modular rocket.
 */
public class ModularRocketItem extends Item {
    
    /**
     * Creates a new modular rocket item.
     * @param properties The item properties
     */
    public ModularRocketItem(Properties properties) {
        super(properties);
    }
    
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = getOrCreateTag(stack);
        
        if (hasValidRocket(stack)) {
            // Add basic rocket info
            int tier = getRocketTier(stack);
            tooltip.add(Component.translatable("item.galactic-space.modular_rocket.tier", tier)
                    .withStyle(getTierStyle(tier)));
            
            // Add component details if shift is pressed
            if (flag.isAdvanced()) {
                tooltip.add(Component.translatable("item.galactic-space.modular_rocket.components").withStyle(ChatFormatting.GRAY));
                
                // Command module
                String commandModuleId = TagHelper.getStringValue(tag, "commandModule");
                tooltip.add(Component.translatable("item.galactic-space.modular_rocket.command_module",
                        getComponentName(commandModuleId)).withStyle(ChatFormatting.BLUE));
                
                // Engines - get tag without using ifPresent
                ListTag enginesList = new ListTag();
                if (tag.contains("engines")) {
                    Tag engineTag = tag.get("engines");
                    if (engineTag != null && engineTag instanceof ListTag listTag) {
                        enginesList.addAll(listTag);
                    }
                }
                if (!enginesList.isEmpty()) {
                    tooltip.add(Component.translatable("item.galactic-space.modular_rocket.engines", 
                            enginesList.size()).withStyle(ChatFormatting.RED));
                }
                
                // Fuel tanks - get tag without using ifPresent
                ListTag fuelTanksList = new ListTag();
                if (tag.contains("fuelTanks")) {
                    Tag fuelTag = tag.get("fuelTanks");
                    if (fuelTag != null && fuelTag instanceof ListTag listTag) {
                        fuelTanksList.addAll(listTag);
                    }
                }
                if (!fuelTanksList.isEmpty()) {
                    tooltip.add(Component.translatable("item.galactic-space.modular_rocket.fuel_tanks", 
                            fuelTanksList.size()).withStyle(ChatFormatting.GREEN));
                }
                
                // Other component counts
                addComponentCountToTooltip(tooltip, tag, "cargoBays", "cargo_bays", ChatFormatting.YELLOW);
                addComponentCountToTooltip(tooltip, tag, "passengerCompartments", "passenger_compartments", ChatFormatting.AQUA);
                addComponentCountToTooltip(tooltip, tag, "shields", "shields", ChatFormatting.GOLD);
                addComponentCountToTooltip(tooltip, tag, "lifeSupports", "life_supports", ChatFormatting.LIGHT_PURPLE);
            } else {
                tooltip.add(Component.translatable("item.galactic-space.modular_rocket.shift_for_details")
                        .withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
            }
        } else {
            tooltip.add(Component.translatable("item.galactic-space.modular_rocket.incomplete")
                    .withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable("item.galactic-space.modular_rocket.use_rocket_builder")
                    .withStyle(ChatFormatting.GRAY));
        }
    }
    
    /**
     * Helper method to add component counts to tooltip.
     */
    private void addComponentCountToTooltip(List<Component> tooltip, CompoundTag tag, 
                                           String tagKey, String translationKey, ChatFormatting color) {
        // Get tag without using ifPresent
        ListTag list = new ListTag();
        if (tag.contains(tagKey)) {
            Tag tagElement = tag.get(tagKey);
            if (tagElement != null && tagElement instanceof ListTag listTag) {
                list.addAll(listTag);
            }
        }
        if (!list.isEmpty()) {
            tooltip.add(Component.translatable("item.galactic-space.modular_rocket." + translationKey, 
                    list.size()).withStyle(color));
        }
    }
    
    /**
     * Gets the chat formatting style for a tier.
     */
    private ChatFormatting getTierStyle(int tier) {
        switch (tier) {
            case 1:
                return ChatFormatting.GREEN;
            case 2:
                return ChatFormatting.BLUE;
            case 3:
                return ChatFormatting.LIGHT_PURPLE;
            default:
                return ChatFormatting.GRAY;
        }
    }
    
    /**
     * Gets the name of a component.
     */
    private String getComponentName(String componentId) {
        if (componentId.isEmpty()) {
            return "None";
        }
        
        // Try to get the component from the registry
        ResourceLocation resLoc = ResourceLocationHelper.of(componentId);
        Optional<IRocketComponent> component = RocketComponentRegistry.getComponent(resLoc);
        return component.map(c -> c.getName()).orElse("Unknown");
    }
    
    /**
     * Checks if the rocket has all required components.
     */
    public boolean hasValidRocket(ItemStack stack) {
        CompoundTag tag = getOrCreateTag(stack);
        
        // Must have command module
        if (!tag.contains("commandModule") || TagHelper.getStringValue(tag, "commandModule").isEmpty()) {
            return false;
        }
        
        // Must have at least one engine - get tag from Optional and handle ListTag type
        if (!tag.contains("engines")) {
            return false;
        }
        
        boolean hasEngines = false;
        if (tag.contains("engines")) {
            // Get tag without using ifPresent
            Tag engineTag = tag.get("engines");
            if (engineTag != null && engineTag instanceof ListTag listTag && !listTag.isEmpty()) {
                hasEngines = true;
            }
        }
        if (!hasEngines) {
            return false;
        }
        
        // Must have at least one fuel tank - get tag without using ifPresent
        if (!tag.contains("fuelTanks")) {
            return false;
        }
        
        boolean hasFuelTanks = false;
        if (tag.contains("fuelTanks")) {
            // Get tag without using ifPresent
            Tag fuelTag = tag.get("fuelTanks");
            if (fuelTag != null && fuelTag instanceof ListTag listTag && !listTag.isEmpty()) {
                hasFuelTanks = true;
            }
        }
        if (!hasFuelTanks) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets the tier of the rocket.
     */
    public int getRocketTier(ItemStack stack) {
        if (!hasValidRocket(stack)) {
            return 0;
        }
        
        CompoundTag tag = getOrCreateTag(stack);
        
        // Get command module tier
        String commandModuleId = TagHelper.getStringValue(tag, "commandModule");
        
        ResourceLocation cmdModRes = ResourceLocationHelper.of(commandModuleId);
        Optional<IRocketComponent> commandModule = RocketComponentRegistry.getComponent(cmdModRes);
        int commandModuleTier = commandModule.map(IRocketComponent::getTier).orElse(0);
        
        // Get highest engine tier
        int highestEngineTier = 0;
        // Get engine list without using ifPresent
        ListTag enginesList = new ListTag();
        if (tag.contains("engines")) {
            Tag engineTag = tag.get("engines");
            if (engineTag != null && engineTag instanceof ListTag listTag) {
                enginesList.addAll(listTag);
            }
        }
        
        for (int i = 0; i < enginesList.size(); i++) {
            String engineId = TagHelper.getStringValue(enginesList.get(i));
            
            if (!engineId.isEmpty()) {
                ResourceLocation engineRes = ResourceLocationHelper.of(engineId);
                Optional<IRocketComponent> engine = RocketComponentRegistry.getComponent(engineRes);
                highestEngineTier = Math.max(highestEngineTier, engine.map(IRocketComponent::getTier).orElse(0));
            }
        }
        
        // Return the minimum of the two (a rocket is limited by its weakest critical component)
        return Math.min(commandModuleTier, highestEngineTier);
    }
    
    /**
     * Creates a rocket from the item stack.
     */
    public ModularRocket createRocket(ItemStack stack, Level level) {
        if (!hasValidRocket(stack)) {
            return null;
        }
        
        CompoundTag tag = getOrCreateTag(stack);
        
        // Create a rocket builder
        ModularRocket.Builder builder = new ModularRocket.Builder(
                ResourceLocationHelper.of("player_rocket_" + UUID.randomUUID().toString())
        );
        
        // Add command module
        String commandModuleId = TagHelper.getStringValue(tag, "commandModule");
        
        if (commandModuleId.isEmpty()) {
            return null;
        }
        
        ResourceLocation cmdModRes = ResourceLocationHelper.of(commandModuleId);
        Optional<IRocketComponent> commandModuleOpt = RocketComponentRegistry.getComponent(cmdModRes);
        if (commandModuleOpt.isPresent() && commandModuleOpt.get() instanceof ICommandModule) {
            builder.commandModule((ICommandModule) commandModuleOpt.get());
        } else {
            return null;
        }
        
        // Add engines - get tag without using ifPresent
        ListTag enginesList = new ListTag();
        if (tag.contains("engines")) {
            Tag engineTag = tag.get("engines");
            if (engineTag != null && engineTag instanceof ListTag listTag) {
                enginesList.addAll(listTag);
            }
        }
        
        for (int i = 0; i < enginesList.size(); i++) {
            String engineId = TagHelper.getStringValue(enginesList.get(i));
            
            if (!engineId.isEmpty()) {
                ResourceLocation engineRes = ResourceLocationHelper.of(engineId);
                Optional<IRocketComponent> engineOpt = RocketComponentRegistry.getComponent(engineRes);
                if (engineOpt.isPresent() && engineOpt.get() instanceof IRocketEngine) {
                    builder.addEngine((IRocketEngine) engineOpt.get());
                }
            }
        }
        
        // Add fuel tanks - get tag without using ifPresent
        ListTag fuelTanksList = new ListTag();
        if (tag.contains("fuelTanks")) {
            Tag fuelTag = tag.get("fuelTanks");
            if (fuelTag != null && fuelTag instanceof ListTag listTag) {
                fuelTanksList.addAll(listTag);
            }
        }
        
        for (int i = 0; i < fuelTanksList.size(); i++) {
            String fuelTankId = TagHelper.getStringValue(fuelTanksList.get(i));
            
            if (!fuelTankId.isEmpty()) {
                ResourceLocation fuelTankRes = ResourceLocationHelper.of(fuelTankId);
                Optional<IRocketComponent> fuelTankOpt = RocketComponentRegistry.getComponent(fuelTankRes);
                if (fuelTankOpt.isPresent() && fuelTankOpt.get() instanceof IFuelTank) {
                    builder.addFuelTank((IFuelTank) fuelTankOpt.get());
                }
            }
        }
        
        // Add cargo bays - get tag without using ifPresent
        ListTag cargoBaysList = new ListTag();
        if (tag.contains("cargoBays")) {
            Tag cargoTag = tag.get("cargoBays");
            if (cargoTag != null && cargoTag instanceof ListTag listTag) {
                cargoBaysList.addAll(listTag);
            }
        }
        
        for (int i = 0; i < cargoBaysList.size(); i++) {
            String cargoBayId = TagHelper.getStringValue(cargoBaysList.get(i));
            
            if (!cargoBayId.isEmpty()) {
                ResourceLocation cargoBayRes = ResourceLocationHelper.of(cargoBayId);
                Optional<IRocketComponent> cargoBayOpt = RocketComponentRegistry.getComponent(cargoBayRes);
                if (cargoBayOpt.isPresent() && cargoBayOpt.get() instanceof ICargoBay) {
                    builder.addCargoBay((ICargoBay) cargoBayOpt.get());
                }
            }
        }
        
        // Add passenger compartments - get tag without using ifPresent
        ListTag passengerCompartmentsList = new ListTag();
        if (tag.contains("passengerCompartments")) {
            Tag passengersTag = tag.get("passengerCompartments");
            if (passengersTag != null && passengersTag instanceof ListTag listTag) {
                passengerCompartmentsList.addAll(listTag);
            }
        }
        
        for (int i = 0; i < passengerCompartmentsList.size(); i++) {
            String compartmentId = TagHelper.getStringValue(passengerCompartmentsList.get(i));
            
            if (!compartmentId.isEmpty()) {
                ResourceLocation compartmentRes = ResourceLocationHelper.of(compartmentId);
                Optional<IRocketComponent> compartmentOpt = RocketComponentRegistry.getComponent(compartmentRes);
                if (compartmentOpt.isPresent() && compartmentOpt.get() instanceof IPassengerCompartment) {
                    builder.addPassengerCompartment((IPassengerCompartment) compartmentOpt.get());
                }
            }
        }
        
        // Add shields - get tag without using ifPresent
        ListTag shieldsList = new ListTag();
        if (tag.contains("shields")) {
            Tag shieldsTag = tag.get("shields");
            if (shieldsTag != null && shieldsTag instanceof ListTag listTag) {
                shieldsList.addAll(listTag);
            }
        }
        
        for (int i = 0; i < shieldsList.size(); i++) {
            String shieldId = TagHelper.getStringValue(shieldsList.get(i));
            
            if (!shieldId.isEmpty()) {
                ResourceLocation shieldRes = ResourceLocationHelper.of(shieldId);
                Optional<IRocketComponent> shieldOpt = RocketComponentRegistry.getComponent(shieldRes);
                if (shieldOpt.isPresent() && shieldOpt.get() instanceof IShield) {
                    builder.addShield((IShield) shieldOpt.get());
                }
            }
        }
        
        // Add life support systems - get tag without using ifPresent
        ListTag lifeSupportsList = new ListTag();
        if (tag.contains("lifeSupports")) {
            Tag lifeSupportTag = tag.get("lifeSupports");
            if (lifeSupportTag != null && lifeSupportTag instanceof ListTag listTag) {
                lifeSupportsList.addAll(listTag);
            }
        }
        
        for (int i = 0; i < lifeSupportsList.size(); i++) {
            String lifeSupportId = TagHelper.getStringValue(lifeSupportsList.get(i));
            
            if (!lifeSupportId.isEmpty()) {
                ResourceLocation lifeSupportRes = ResourceLocationHelper.of(lifeSupportId);
                Optional<IRocketComponent> lifeSupportOpt = RocketComponentRegistry.getComponent(lifeSupportRes);
                if (lifeSupportOpt.isPresent() && lifeSupportOpt.get() instanceof ILifeSupport) {
                    builder.addLifeSupport((ILifeSupport) lifeSupportOpt.get());
                }
            }
        }
        
        // Build the rocket and return it
        try {
            return builder.build();
        } catch (IllegalStateException e) {
            GalacticSpace.LOGGER.error("Failed to build rocket: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Handles rocket use.
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (!hasValidRocket(stack)) {
            if (level.isClientSide) {
                player.displayClientMessage(
                        Component.translatable("message.galactic-space.rocket_incomplete")
                                .withStyle(ChatFormatting.RED),
                        true
                );
            }
            return InteractionResult.FAIL;
        }
        
        // Only process on server side
        if (!level.isClientSide) {
            // Create the rocket entity and launch it
            ModularRocket rocket = createRocket(stack, level);
            if (rocket != null) {
                // Handle rocket launch through the rocket entity
                boolean success = RocketEntitySpawner.spawnRocketEntity(level, player, rocket);
                
                if (success) {
                    // Reduce stack size if not in creative mode
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    
                    return InteractionResult.CONSUME;
                } else {
                    player.displayClientMessage(
                            Component.translatable("message.galactic-space.rocket_launch_failed")
                                    .withStyle(ChatFormatting.RED),
                            true
                    );
                }
            } else {
                player.displayClientMessage(
                        Component.translatable("message.galactic-space.rocket_invalid")
                                .withStyle(ChatFormatting.RED),
                        true
                );
            }
        }
        
        return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
    }
}