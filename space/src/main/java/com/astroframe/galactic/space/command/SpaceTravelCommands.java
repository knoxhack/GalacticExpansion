package com.astroframe.galactic.space.command;

import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.ModularRocket;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.dimension.SpaceStationDimension;
import com.astroframe.galactic.space.dimension.SpaceStationTeleporter;
import com.astroframe.galactic.space.implementation.RocketLaunchController;
import com.astroframe.galactic.space.implementation.SpaceBodies;
import com.astroframe.galactic.space.implementation.SpaceTravelManager;
import com.astroframe.galactic.space.implementation.component.RocketComponentFactory;
import com.astroframe.galactic.space.item.ModularRocketItem;
import com.astroframe.galactic.space.item.SpaceItems;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

/**
 * Admin command tools for testing space travel functionality.
 */
public class SpaceTravelCommands {

    /**
     * Register all space travel commands.
     *
     * @param dispatcher The command dispatcher
     * @param context The command build context
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        GalacticSpace.LOGGER.info("Registering space travel commands");
        
        dispatcher.register(
            Commands.literal("spacetravel")
                .requires(source -> source.hasPermission(2)) // Require permission level 2 (op)
                .then(Commands.literal("teleport")
                    .then(Commands.literal("station")
                        .executes(SpaceTravelCommands::teleportToSpaceStation))
                    .then(Commands.literal("earth")
                        .executes(SpaceTravelCommands::teleportToEarth)))
                .then(Commands.literal("rocket")
                    .then(Commands.literal("give")
                        .then(Commands.argument("tier", IntegerArgumentType.integer(1, 3))
                            .executes(SpaceTravelCommands::giveRocket)))
                    .then(Commands.literal("fuel")
                        .executes(SpaceTravelCommands::fillRocketFuel)))
                .then(Commands.literal("suit")
                    .then(Commands.literal("give")
                        .executes(SpaceTravelCommands::giveSpaceSuit)))
                .then(Commands.literal("resources")
                    .then(Commands.literal("give")
                        .executes(SpaceTravelCommands::giveSpaceResources)))
        );
    }
    
    /**
     * Teleports a player to the space station.
     *
     * @param context The command context
     * @return Command result code
     */
    private static int teleportToSpaceStation(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("This command can only be used by a player"));
            return 0;
        }
        
        ServerLevel spaceStation = source.getServer().getLevel(SpaceStationDimension.SPACE_STATION_LEVEL_KEY);
        if (spaceStation == null) {
            source.sendFailure(Component.literal("Space station dimension not found"));
            return 0;
        }
        
        boolean success = SpaceStationTeleporter.teleportToSpaceStation(player);
        if (success) {
            source.sendSuccess(() -> Component.literal("Teleported to space station"), true);
            return 1;
        } else {
            source.sendFailure(Component.literal("Failed to teleport to space station"));
            return 0;
        }
    }
    
    /**
     * Teleports a player to Earth.
     *
     * @param context The command context
     * @return Command result code
     */
    private static int teleportToEarth(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("This command can only be used by a player"));
            return 0;
        }
        
        boolean success = SpaceStationTeleporter.teleportToEarth(player);
        if (success) {
            source.sendSuccess(() -> Component.literal("Teleported to Earth"), true);
            return 1;
        } else {
            source.sendFailure(Component.literal("Failed to teleport to Earth"));
            return 0;
        }
    }
    
    /**
     * Gives a rocket to the player.
     *
     * @param context The command context
     * @return Command result code
     */
    private static int giveRocket(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("This command can only be used by a player"));
            return 0;
        }
        
        int tier = IntegerArgumentType.getInteger(context, "tier");
        ItemStack rocketStack = createRocket(tier);
        
        if (!player.getInventory().add(rocketStack)) {
            player.drop(rocketStack, false);
        }
        
        source.sendSuccess(() -> Component.literal("Given tier " + tier + " rocket"), true);
        return 1;
    }
    
    /**
     * Creates a rocket item of the specified tier.
     *
     * @param tier The rocket tier
     * @return The rocket item
     */
    private static ItemStack createRocket(int tier) {
        tier = Math.max(1, Math.min(3, tier));
        
        ItemStack stack = new ItemStack(SpaceItems.MODULAR_ROCKET.get());
        ModularRocket rocket = new ModularRocket();
        
        // Add components for the requested tier
        rocket.addComponent(RocketComponentFactory.createEngine(RocketComponentType.ENGINE, tier));
        rocket.addComponent(RocketComponentFactory.createFuelTank(RocketComponentType.FUEL_TANK, tier));
        rocket.addComponent(RocketComponentFactory.createCockpit(RocketComponentType.COCKPIT, tier));
        rocket.addComponent(RocketComponentFactory.createStructure(RocketComponentType.STRUCTURE, tier));
        
        // Add optional advanced components for higher tiers
        if (tier >= 2) {
            rocket.addComponent(RocketComponentFactory.createNavigation(RocketComponentType.NAVIGATION, tier));
        }
        
        if (tier >= 3) {
            rocket.addComponent(RocketComponentFactory.createShielding(RocketComponentType.SHIELDING, tier));
            rocket.addComponent(RocketComponentFactory.createLifeSupport(RocketComponentType.LIFE_SUPPORT, tier));
        }
        
        // Set full fuel
        rocket.setFuelLevel(rocket.getFuelCapacity());
        
        // Save rocket to item
        ModularRocketItem.saveRocketToStack(stack, rocket);
        
        return stack;
    }
    
    /**
     * Fills the fuel of a rocket in the player's hand.
     *
     * @param context The command context
     * @return Command result code
     */
    private static int fillRocketFuel(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("This command can only be used by a player"));
            return 0;
        }
        
        ItemStack mainHand = player.getMainHandItem();
        if (!(mainHand.getItem() instanceof ModularRocketItem)) {
            source.sendFailure(Component.literal("You must be holding a rocket"));
            return 0;
        }
        
        IRocket rocket = ModularRocketItem.getRocketFromStack(mainHand);
        if (rocket == null) {
            source.sendFailure(Component.literal("Invalid rocket"));
            return 0;
        }
        
        // Fill fuel
        rocket.setFuelLevel(rocket.getFuelCapacity());
        
        // Save back to item
        ModularRocketItem.saveRocketToStack(mainHand, rocket);
        
        source.sendSuccess(() -> Component.literal("Rocket fuel filled"), true);
        return 1;
    }
    
    /**
     * Gives a space suit to the player.
     *
     * @param context The command context
     * @return Command result code
     */
    private static int giveSpaceSuit(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("This command can only be used by a player"));
            return 0;
        }
        
        // Give all space suit pieces
        player.getInventory().add(new ItemStack(SpaceItems.SPACE_SUIT_HELMET.get()));
        player.getInventory().add(new ItemStack(SpaceItems.SPACE_SUIT_CHESTPLATE.get()));
        player.getInventory().add(new ItemStack(SpaceItems.SPACE_SUIT_LEGGINGS.get()));
        player.getInventory().add(new ItemStack(SpaceItems.SPACE_SUIT_BOOTS.get()));
        
        source.sendSuccess(() -> Component.literal("Given space suit"), true);
        return 1;
    }
    
    /**
     * Gives space resources to the player.
     *
     * @param context The command context
     * @return Command result code
     */
    private static int giveSpaceResources(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("This command can only be used by a player"));
            return 0;
        }
        
        // Give all space resources
        player.getInventory().add(new ItemStack(SpaceItems.STELLAR_FRAGMENT.get(), 16));
        player.getInventory().add(new ItemStack(SpaceItems.LUNAR_DUST.get(), 32));
        player.getInventory().add(new ItemStack(SpaceItems.MOON_ROCK.get(), 16));
        
        source.sendSuccess(() -> Component.literal("Given space resources"), true);
        return 1;
    }
}