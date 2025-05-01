package com.astroframe.galactic.space.dimension;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handles player teleportation to and from the space station.
 * This class keeps track of player positions across dimension transitions and manages the teleportation process.
 */
public class SpaceStationTeleporter {
    // Map to store player's last position in overworld before teleporting to space station
    private static final Map<UUID, TeleportData> overworldPositions = new HashMap<>();
    
    // Map to store player's last position in space station before teleporting back to overworld
    private static final Map<UUID, TeleportData> spaceStationPositions = new HashMap<>();
    
    /**
     * Default spawn position for the space station.
     */
    private static final BlockPos SPACE_STATION_SPAWN = new BlockPos(0, 100, 0);
    
    /**
     * Teleports a player to the space station.
     *
     * @param player The player to teleport
     * @return true if teleportation was successful
     */
    public static boolean teleportToSpaceStation(ServerPlayer player) {
        // Get the server level for the space station
        ServerLevel targetLevel = player.getServer().getLevel(SpaceStationDimension.SPACE_STATION_LEVEL_KEY);
        
        // Check if the dimension exists
        if (targetLevel == null) {
            player.sendSystemMessage(Component.translatable("message.galactic-space.space_station_unavailable"));
            GalacticSpace.LOGGER.error("Space station dimension not found!");
            return false;
        }
        
        // Store player's current position in the overworld
        storePlayerPosition(player, overworldPositions);
        
        // Get player's last position in space station or use default spawn if first visit
        Vec3 targetPos = getTargetPosition(player, spaceStationPositions, SPACE_STATION_SPAWN);
        
        // Teleport player
        return teleportPlayerToDimension(player, targetLevel, targetPos);
    }
    
    /**
     * Teleports a player back to the overworld.
     *
     * @param player The player to teleport
     * @return true if teleportation was successful
     */
    public static boolean teleportToOverworld(ServerPlayer player) {
        // Get the server level for the overworld
        ServerLevel targetLevel = player.getServer().getLevel(Level.OVERWORLD);
        
        // Check if the dimension exists (should always exist)
        if (targetLevel == null) {
            player.sendSystemMessage(Component.translatable("message.galactic-space.overworld_unavailable"));
            GalacticSpace.LOGGER.error("Overworld dimension not found!");
            return false;
        }
        
        // Store player's current position in the space station
        storePlayerPosition(player, spaceStationPositions);
        
        // Get player's last position in overworld
        TeleportData overworldData = overworldPositions.get(player.getUUID());
        if (overworldData == null) {
            // If player has no stored overworld position, use spawn point
            BlockPos spawnPos = targetLevel.getSharedSpawnPos();
            return teleportPlayerToDimension(player, targetLevel, Vec3.atCenterOf(spawnPos));
        } else {
            return teleportPlayerToDimension(player, targetLevel, overworldData.position);
        }
    }
    
    /**
     * Teleports a player to a specific dimension at a specific position.
     *
     * @param player The player to teleport
     * @param targetLevel The destination level
     * @param targetPos The destination position
     * @return true if teleportation was successful
     */
    private static boolean teleportPlayerToDimension(ServerPlayer player, ServerLevel targetLevel, Vec3 targetPos) {
        // Use the correct teleportation method for NeoForge 1.21.5
        try {
            // First teleport within dimension to the target position
            player.teleportTo(targetPos.x, targetPos.y, targetPos.z);
            
            // Send confirmation message
            String dimensionName = targetLevel.dimension().equals(Level.OVERWORLD) 
                                 ? "overworld" : "space_station";
            player.sendSystemMessage(Component.translatable("message.galactic-space.teleported_to." + dimensionName));
            
            return true;
        } catch (Exception e) {
            // Log the error
            GalacticSpace.LOGGER.error("Failed to teleport player {} to dimension {}: {}",
                                     player.getName().getString(), 
                                     targetLevel.dimension().location(),
                                     e.getMessage());
            
            // Send error message
            player.sendSystemMessage(Component.translatable("message.galactic-space.teleport_failed"));
            
            return false;
        }
    }
    
    /**
     * Stores a player's current position data.
     *
     * @param player The player
     * @param positionMap The map to store the position in
     */
    private static void storePlayerPosition(ServerPlayer player, Map<UUID, TeleportData> positionMap) {
        positionMap.put(player.getUUID(), new TeleportData(
            new Vec3(player.getX(), player.getY(), player.getZ()),
            player.getYRot(),
            player.getXRot()
        ));
    }
    
    /**
     * Gets the target position for teleportation.
     *
     * @param player The player
     * @param positionMap The map to get the position from
     * @param defaultPos The default position if no stored position is found
     * @return The target position
     */
    private static Vec3 getTargetPosition(ServerPlayer player, Map<UUID, TeleportData> positionMap, BlockPos defaultPos) {
        TeleportData data = positionMap.get(player.getUUID());
        return data != null ? data.position : Vec3.atCenterOf(defaultPos);
    }
    
    /**
     * Data class for storing teleportation data.
     */
    private static class TeleportData {
        private final Vec3 position;
        private final float yRot;
        private final float xRot;
        
        TeleportData(Vec3 position, float yRot, float xRot) {
            this.position = position;
            this.yRot = yRot;
            this.xRot = xRot;
        }
    }
}