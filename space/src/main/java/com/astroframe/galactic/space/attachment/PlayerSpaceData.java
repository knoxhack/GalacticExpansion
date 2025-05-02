package com.astroframe.galactic.space.attachment;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.UUID;

/**
 * Utility class for working with player space data attachments.
 * Provides static methods for common operations.
 */
public class PlayerSpaceData {
    
    /**
     * Discover a celestial body for a player.
     * @param player The player
     * @param body The celestial body
     * @return true if the body was newly discovered
     */
    public static boolean discoverCelestialBody(Player player, ICelestialBody body) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            return data.discoverCelestialBody(body);
        }
        return false;
    }
    
    /**
     * Check if a player has discovered a celestial body.
     * @param player The player
     * @param body The celestial body
     * @return true if the player has discovered the body
     */
    public static boolean hasDiscovered(Player player, ICelestialBody body) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            return data.hasDiscovered(body);
        }
        return false;
    }
    
    /**
     * Get all celestial bodies a player has discovered.
     * @param player The player
     * @return A list of discovered celestial body IDs
     */
    public static List<ResourceLocation> getDiscoveredBodies(Player player) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            return data.getDiscoveredBodies();
        }
        return List.of();
    }
    
    /**
     * Set the last visited celestial body for a player.
     * @param player The player
     * @param body The celestial body
     */
    public static void setLastVisitedBody(Player player, ICelestialBody body) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            data.setLastVisitedBody(body);
        }
    }
    
    /**
     * Get the last visited celestial body ID for a player.
     * @param player The player
     * @return The celestial body ID, or null if none
     */
    public static ResourceLocation getLastVisitedBodyId(Player player) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            return data.getLastVisitedBodyId();
        }
        return null;
    }
    
    /**
     * Add space exploration experience for a player.
     * @param player The player
     * @param amount The amount to add
     */
    public static void addExperience(Player player, int amount) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            data.addExperience(amount);
        }
    }
    
    /**
     * Get the space exploration experience for a player.
     * @param player The player
     * @return The experience amount
     */
    public static int getExperience(Player player) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            return data.getExperience();
        }
        return 0;
    }
    
    /**
     * Check if a player is currently in space.
     * @param player The player
     * @return true if the player is in space
     */
    public static boolean isInSpace(Player player) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            return data.isInSpace();
        }
        return false;
    }
    
    /**
     * Set whether a player is in space.
     * @param player The player
     * @param inSpace true if the player is in space
     */
    public static void setInSpace(Player player, boolean inSpace) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            data.setInSpace(inSpace);
        }
    }
    
    /**
     * Get the ID of the rocket a player is currently in.
     * @param player The player
     * @return the rocket UUID, or null if not in a rocket
     */
    public static UUID getCurrentRocketId(Player player) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            return data.getCurrentRocketId();
        }
        return null;
    }
    
    /**
     * Set the ID of the rocket a player is currently in.
     * @param player The player
     * @param rocketId the rocket UUID, or null if not in a rocket
     */
    public static void setCurrentRocketId(Player player, UUID rocketId) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            data.setCurrentRocketId(rocketId);
        }
    }
    
    /**
     * Get the current dimension a player is in.
     * @param player The player
     * @return the dimension resource location
     */
    public static ResourceLocation getCurrentDimension(Player player) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            return data.getCurrentDimension();
        }
        return null;
    }
    
    /**
     * Set the current dimension a player is in.
     * @param player The player
     * @param dimension the dimension resource location
     */
    public static void setCurrentDimension(Player player, ResourceLocation dimension) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            data.setCurrentDimension(dimension);
        }
    }
    
    /**
     * Check if a player is currently changing dimensions.
     * @param player The player
     * @return true if the player is changing dimensions
     */
    public static boolean isChangingDimension(Player player) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            return data.isChangingDimension();
        }
        return false;
    }
    
    /**
     * Set whether a player is currently changing dimensions.
     * @param player The player
     * @param changing true if the player is changing dimensions
     */
    public static void setChangingDimension(Player player, boolean changing) {
        PlayerSpaceDataAttachment data = PlayerSpaceDataRegistry.get(player);
        if (data != null) {
            data.setChangingDimension(changing);
        }
    }
}