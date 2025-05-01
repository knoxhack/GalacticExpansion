package com.astroframe.galactic.space.command;

import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.dimension.SpaceStationDimension;
import com.astroframe.galactic.space.dimension.SpaceStationHelper;
import com.astroframe.galactic.space.dimension.SpaceStationTeleporter;
import com.astroframe.galactic.space.implementation.SpaceBodies;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Commands for space travel and space station operations.
 */
@Mod.EventBusSubscriber(modid = GalacticSpace.MOD_ID)
public class SpaceTravelCommands {

    /**
     * Registers all space travel related commands.
     *
     * @param event The command registration event
     */
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        // Register the /spacestation command
        dispatcher.register(
            Commands.literal("spacestation")
                .requires(source -> source.hasPermission(2)) // Require permission level 2 (ops)
                .then(Commands.literal("teleport")
                    .executes(context -> teleportToSpaceStation(context, false))
                    .then(Commands.argument("buildPlatform", BoolArgumentType.bool())
                        .executes(context -> teleportToSpaceStation(context, BoolArgumentType.getBool(context, "buildPlatform"))))
                )
                .then(Commands.literal("return")
                    .executes(SpaceTravelCommands::returnFromSpaceStation)
                )
                .then(Commands.literal("buildplatform")
                    .executes(SpaceTravelCommands::buildSpaceStationPlatform)
                )
        );
        
        // Register the /galactictp command for any celestial body
        dispatcher.register(
            Commands.literal("galactictp")
                .requires(source -> source.hasPermission(2)) // Require permission level 2 (ops)
                .then(Commands.literal("spacestation")
                    .executes(context -> teleportToCelestialBody(context, SpaceBodies.SPACE_STATION))
                )
                .then(Commands.literal("earth")
                    .executes(context -> teleportToCelestialBody(context, SpaceBodies.EARTH))
                )
                // Additional planets can be added here when the Exploration module is implemented
        );
    }
    
    /**
     * Command handler to teleport a player to the space station.
     *
     * @param context The command context
     * @param buildPlatform Whether to build the platform after teleportation
     * @return Command result code
     */
    private static int teleportToSpaceStation(CommandContext<CommandSourceStack> context, boolean buildPlatform) {
        CommandSourceStack source = context.getSource();
        
        // Ensure the command is run by a player
        if (!source.isPlayer()) {
            source.sendFailure(Component.translatable("command.galactic-space.player_only"));
            return 0;
        }
        
        ServerPlayer player = source.getPlayerOrException();
        
        // Teleport the player to the space station
        boolean success = SpaceStationTeleporter.teleportToSpaceStation(player);
        
        if (success && buildPlatform) {
            // Get the space station dimension
            ServerLevel spaceStation = player.getServer().getLevel(SpaceStationDimension.SPACE_STATION_LEVEL_KEY);
            
            if (spaceStation != null) {
                // Build the platform
                SpaceStationHelper.buildSpaceStationPlatform(spaceStation);
                source.sendSuccess(() -> Component.translatable("command.galactic-space.platform_built"), true);
            }
        }
        
        return success ? 1 : 0;
    }
    
    /**
     * Command handler to return a player from the space station to the overworld.
     *
     * @param context The command context
     * @return Command result code
     */
    private static int returnFromSpaceStation(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        // Ensure the command is run by a player
        if (!source.isPlayer()) {
            source.sendFailure(Component.translatable("command.galactic-space.player_only"));
            return 0;
        }
        
        ServerPlayer player = source.getPlayerOrException();
        
        // Teleport the player back to the overworld
        boolean success = SpaceStationTeleporter.teleportToOverworld(player);
        
        return success ? 1 : 0;
    }
    
    /**
     * Command handler to build the space station platform.
     *
     * @param context The command context
     * @return Command result code
     */
    private static int buildSpaceStationPlatform(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        
        // Get the current level
        ServerLevel level = source.getLevel();
        
        // Check if we're in the space station dimension
        if (!SpaceStationDimension.isSpaceStation(level)) {
            source.sendFailure(Component.translatable("command.galactic-space.not_in_space_station"));
            return 0;
        }
        
        // Build the platform
        SpaceStationHelper.buildSpaceStationPlatform(level);
        
        // Send success message
        source.sendSuccess(() -> Component.translatable("command.galactic-space.platform_built"), true);
        
        return 1;
    }
    
    /**
     * Command handler to teleport a player to a specific celestial body.
     *
     * @param context The command context
     * @param destination The destination celestial body
     * @return Command result code
     */
    private static int teleportToCelestialBody(CommandContext<CommandSourceStack> context, 
                                             com.astroframe.galactic.core.api.space.ICelestialBody destination) {
        CommandSourceStack source = context.getSource();
        
        // Ensure the command is run by a player
        if (!source.isPlayer()) {
            source.sendFailure(Component.translatable("command.galactic-space.player_only"));
            return 0;
        }
        
        ServerPlayer player = source.getPlayerOrException();
        
        // Use the space travel manager to teleport the player
        boolean success = GalacticSpace.getSpaceTravelManager().travelTo(player, destination);
        
        if (success) {
            // Mark as discovered
            GalacticSpace.getSpaceTravelManager().discoverCelestialBody(player, destination);
        }
        
        return success ? 1 : 0;
    }
}