package com.astroframe.galactic.space.items;

import com.astroframe.galactic.core.api.space.ModularRocket;
import com.astroframe.galactic.core.api.space.component.*;
import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.implementation.component.RocketComponentFactory;
import com.astroframe.galactic.space.implementation.component.ShieldFactory;
import com.astroframe.galactic.space.implementation.component.ResourceLocationHelper;
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
                String commandModuleId = tag.getString("commandModule").orElse("");
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
        if (!tag.contains("commandModule") || tag.getString("commandModule").orElse("").isEmpty()) {
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
        String commandModuleId = tag.getString("commandModule").orElse("");
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
            String engineId = enginesList.getString(i).orElse("");
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
        String commandModuleId = tag.getString("commandModule").orElse("");
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
            String engineId = enginesList.getString(i).orElse("");
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
            String fuelTankId = fuelTanksList.getString(i).orElse("");
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
            String cargoBayId = cargoBaysList.getString(i).orElse("");
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
            String compartmentId = passengerCompartmentsList.getString(i).orElse("");
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
            String shieldId = shieldsList.getString(i).orElse("");
            if (!shieldId.isEmpty()) {
                ResourceLocation shieldRes = ResourceLocationHelper.of(shieldId);
                Optional<IRocketComponent> shieldOpt = RocketComponentRegistry.getComponent(shieldRes);
                if (shieldOpt.isPresent() && shieldOpt.get() instanceof IShield) {
                    builder.addShield((IShield) shieldOpt.get());
                }
            }
        }
        
        // Add life support systems - safely get from Optional
        ListTag lifeSupportsList = new ListTag();
        if (tag.contains("lifeSupports")) {
            tag.get("lifeSupports").ifPresent(lifeSupportTag -> {
                if (lifeSupportTag instanceof ListTag listTag) {
                    lifeSupportsList.addAll(listTag);
                }
            });
        }
        
        for (int i = 0; i < lifeSupportsList.size(); i++) {
            String lifeSupportId = lifeSupportsList.getString(i).orElse("");
            if (!lifeSupportId.isEmpty()) {
                ResourceLocation lifeSupportRes = ResourceLocationHelper.of(lifeSupportId);
                Optional<IRocketComponent> lifeSupportOpt = RocketComponentRegistry.getComponent(lifeSupportRes);
                if (lifeSupportOpt.isPresent() && lifeSupportOpt.get() instanceof ILifeSupport) {
                    builder.addLifeSupport((ILifeSupport) lifeSupportOpt.get());
                }
            }
        }
        
        // Try to build the rocket
        try {
            return builder.build();
        } catch (IllegalStateException e) {
            GalacticSpace.LOGGER.error("Failed to build rocket: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Creates a pre-built tier 1 rocket.
     */
    public static ItemStack createTier1Rocket() {
        ItemStack stack = new ItemStack(SpaceItems.MODULAR_ROCKET.get());
        CompoundTag tag = getOrCreateTag(stack);
        
        // Add command module
        ICommandModule commandModule = RocketComponentFactory.createBasicCommandModule();
        tag.putString("commandModule", commandModule.getId().toString());
        
        // Add engines
        IRocketEngine engine = RocketComponentFactory.createChemicalEngine();
        ListTag enginesList = new ListTag();
        enginesList.add(StringTag.valueOf(engine.getId().toString()));
        tag.put("engines", enginesList);
        
        // Add fuel tanks
        IFuelTank fuelTank = RocketComponentFactory.createBasicFuelTank();
        ListTag fuelTanksList = new ListTag();
        fuelTanksList.add(StringTag.valueOf(fuelTank.getId().toString()));
        tag.put("fuelTanks", fuelTanksList);
        
        // Add cargo bay
        ICargoBay cargoBay = RocketComponentFactory.createBasicCargoBay();
        ListTag cargoBaysList = new ListTag();
        cargoBaysList.add(StringTag.valueOf(cargoBay.getId().toString()));
        tag.put("cargoBays", cargoBaysList);
        
        // Add passenger compartment
        IPassengerCompartment passengerCompartment = RocketComponentFactory.createBasicPassengerCompartment();
        ListTag passengerCompartmentsList = new ListTag();
        passengerCompartmentsList.add(StringTag.valueOf(passengerCompartment.getId().toString()));
        tag.put("passengerCompartments", passengerCompartmentsList);
        
        return stack;
    }
    
    /**
     * Creates a pre-built tier 2 rocket.
     */
    public static ItemStack createTier2Rocket() {
        ItemStack stack = new ItemStack(SpaceItems.MODULAR_ROCKET.get());
        CompoundTag tag = getOrCreateTag(stack);
        
        // Add command module
        ICommandModule commandModule = RocketComponentFactory.createStandardCommandModule();
        tag.putString("commandModule", commandModule.getId().toString());
        
        // Add engines
        IRocketEngine engine = RocketComponentFactory.createIonEngine();
        ListTag enginesList = new ListTag();
        enginesList.add(StringTag.valueOf(engine.getId().toString()));
        enginesList.add(StringTag.valueOf(engine.getId().toString())); // Add two engines
        tag.put("engines", enginesList);
        
        // Add fuel tanks
        IFuelTank fuelTank = RocketComponentFactory.createStandardFuelTank();
        ListTag fuelTanksList = new ListTag();
        fuelTanksList.add(StringTag.valueOf(fuelTank.getId().toString()));
        fuelTanksList.add(StringTag.valueOf(fuelTank.getId().toString())); // Add two fuel tanks
        tag.put("fuelTanks", fuelTanksList);
        
        // Add cargo bay
        ICargoBay cargoBay = RocketComponentFactory.createStandardCargoBay();
        ListTag cargoBaysList = new ListTag();
        cargoBaysList.add(StringTag.valueOf(cargoBay.getId().toString()));
        tag.put("cargoBays", cargoBaysList);
        
        // Add passenger compartment
        IPassengerCompartment passengerCompartment = RocketComponentFactory.createStandardPassengerCompartment();
        ListTag passengerCompartmentsList = new ListTag();
        passengerCompartmentsList.add(StringTag.valueOf(passengerCompartment.getId().toString()));
        tag.put("passengerCompartments", passengerCompartmentsList);
        
        // Add shield
        IShield shield = RocketComponentFactory.createThermalShield();
        ListTag shieldsList = new ListTag();
        shieldsList.add(StringTag.valueOf(shield.getId().toString()));
        tag.put("shields", shieldsList);
        
        // Add life support
        ILifeSupport lifeSupport = RocketComponentFactory.createStandardLifeSupport();
        ListTag lifeSupportsList = new ListTag();
        lifeSupportsList.add(StringTag.valueOf(lifeSupport.getId().toString()));
        tag.put("lifeSupports", lifeSupportsList);
        
        return stack;
    }
    
    /**
     * Creates a pre-built tier 3 rocket.
     */
    public static ItemStack createTier3Rocket() {
        ItemStack stack = new ItemStack(SpaceItems.MODULAR_ROCKET.get());
        CompoundTag tag = getOrCreateTag(stack);
        
        // Add command module
        ICommandModule commandModule = RocketComponentFactory.createAdvancedCommandModule();
        tag.putString("commandModule", commandModule.getId().toString());
        
        // Add engines
        IRocketEngine engine = RocketComponentFactory.createPlasmaEngine();
        ListTag enginesList = new ListTag();
        enginesList.add(StringTag.valueOf(engine.getId().toString()));
        enginesList.add(StringTag.valueOf(engine.getId().toString()));
        enginesList.add(StringTag.valueOf(engine.getId().toString())); // Add three engines
        tag.put("engines", enginesList);
        
        // Add fuel tanks
        IFuelTank fuelTank = RocketComponentFactory.createAdvancedFuelTank();
        ListTag fuelTanksList = new ListTag();
        fuelTanksList.add(StringTag.valueOf(fuelTank.getId().toString()));
        fuelTanksList.add(StringTag.valueOf(fuelTank.getId().toString()));
        fuelTanksList.add(StringTag.valueOf(RocketComponentFactory.createQuantumFuelTank().getId().toString()));
        tag.put("fuelTanks", fuelTanksList);
        
        // Add cargo bay
        ICargoBay cargoBay = RocketComponentFactory.createAdvancedCargoBay();
        ListTag cargoBaysList = new ListTag();
        cargoBaysList.add(StringTag.valueOf(cargoBay.getId().toString()));
        cargoBaysList.add(StringTag.valueOf(cargoBay.getId().toString())); // Add two cargo bays
        tag.put("cargoBays", cargoBaysList);
        
        // Add passenger compartment
        IPassengerCompartment passengerCompartment = RocketComponentFactory.createAdvancedPassengerCompartment();
        ListTag passengerCompartmentsList = new ListTag();
        passengerCompartmentsList.add(StringTag.valueOf(passengerCompartment.getId().toString()));
        passengerCompartmentsList.add(StringTag.valueOf(
                RocketComponentFactory.createScientificPassengerCompartment().getId().toString()));
        tag.put("passengerCompartments", passengerCompartmentsList);
        
        // Add shield
        IShield shield = RocketComponentFactory.createDeflectorShield();
        ListTag shieldsList = new ListTag();
        shieldsList.add(StringTag.valueOf(shield.getId().toString()));
        shieldsList.add(StringTag.valueOf(ShieldFactory.createQuantumShield().getId().toString()));
        tag.put("shields", shieldsList);
        
        // Add life support
        ILifeSupport lifeSupport = RocketComponentFactory.createAdvancedLifeSupport();
        ListTag lifeSupportsList = new ListTag();
        lifeSupportsList.add(StringTag.valueOf(lifeSupport.getId().toString()));
        lifeSupportsList.add(StringTag.valueOf(
                RocketComponentFactory.createBioregenerativeLifeSupport().getId().toString()));
        tag.put("lifeSupports", lifeSupportsList);
        
        return stack;
    }
    
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        
        if (!level.isClientSide && hasValidRocket(itemstack)) {
            // Here you would spawn the rocket entity or open a GUI
            player.displayClientMessage(
                    Component.translatable("item.galactic-space.modular_rocket.use_message"), true);
            
            // For now, just testing the rocket creation
            ModularRocket rocket = createRocket(itemstack, level);
            if (rocket != null) {
                player.displayClientMessage(
                        Component.literal("Created rocket with tier: " + rocket.getTier()), false);
            }
        }
        
        return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
    }
}