package com.astroframe.galactic.space.implementation;

import com.astroframe.galactic.core.api.space.ModularRocket;
import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Utility class to handle spawning rocket entities for space travel.
 */
public class RocketEntitySpawner {
    
    /**
     * Spawn a rocket entity for the player and initiate the rocket launch sequence.
     * 
     * @param level The level
     * @param player The player
     * @param rocket The rocket data
     * @return true if rocket spawning was successful
     */
    public static boolean spawnRocketEntity(Level level, Player player, ModularRocket rocket) {
        if (level.isClientSide) {
            return false;
        }
        
        if (player instanceof ServerPlayer serverPlayer) {
            GalacticSpace.LOGGER.info("Initiating rocket launch for player {}", player.getName().getString());
            
            // Set the player's destination to the space station for now
            // In the future, this could be determined by player choice
            return GalacticSpace.getSpaceTravelManager().initiateTravelWithRocket(
                    serverPlayer, 
                    SpaceBodies.SPACE_STATION, 
                    rocket
            );
        }
        
        return false;
    }
}