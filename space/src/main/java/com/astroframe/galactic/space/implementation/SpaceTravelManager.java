package com.astroframe.galactic.space.implementation;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.core.api.space.ISpaceTravelManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the space travel manager.
 */
public class SpaceTravelManager implements ISpaceTravelManager {
    private static final String DISCOVERED_TAG = "GalacticDiscoveredBodies";
    
    private final Map<UUID, List<ResourceLocation>> discoveredBodies = new HashMap<>();
    
    @Override
    public boolean travelTo(ServerPlayer player, ICelestialBody destination) {
        // Get the destination's ID
        ResourceLocation destinationId = destination.getId();
        
        // Handle special case for the Space Station
        if (destinationId.equals(SpaceBodies.SPACE_STATION.getId())) {
            return com.astroframe.galactic.space.dimension.SpaceStationTeleporter.teleportToSpaceStation(player);
        }
        
        // Handle special case for Earth/Overworld
        if (destinationId.equals(SpaceBodies.EARTH.getId())) {
            return com.astroframe.galactic.space.dimension.SpaceStationTeleporter.teleportToOverworld(player);
        }
        
        // For other celestial bodies (to be implemented in the Exploration module)
        // Just log a message for now
        GalacticSpace.LOGGER.info("Player {} attempted to travel to {}, but this destination is not yet implemented",
                                   player.getName().getString(), destination.getName());
        player.sendSystemMessage(Component.translatable("message.galactic-space.destination_not_implemented")
                                .append(": " + destination.getName()));
        
        return false;
    }

    @Override
    public List<ICelestialBody> getDiscoveredCelestialBodies(Player player) {
        List<ICelestialBody> bodies = new ArrayList<>();
        List<ResourceLocation> discovered = discoveredBodies.getOrDefault(player.getUUID(), new ArrayList<>());
        
        for (ResourceLocation id : discovered) {
            getCelestialBody(id).ifPresent(bodies::add);
        }
        
        return bodies;
    }

    @Override
    public boolean hasDiscovered(Player player, ICelestialBody body) {
        return discoveredBodies.getOrDefault(player.getUUID(), new ArrayList<>())
                .contains(body.getId());
    }
    
    /**
     * Records a celestial body as discovered by a player.
     * 
     * @param player The player who made the discovery
     * @param body The celestial body that was discovered
     */
    @Override
    public void discoverCelestialBody(Player player, ICelestialBody body) {
        UUID playerId = player.getUUID();
        List<ResourceLocation> playerDiscoveries = discoveredBodies.computeIfAbsent(playerId, k -> new ArrayList<>());
        
        // Only add if not already discovered
        if (!playerDiscoveries.contains(body.getId())) {
            playerDiscoveries.add(body.getId());
            
            // Notify the player
            if (player instanceof ServerPlayer) {
                ((ServerPlayer) player).sendSystemMessage(
                    Component.translatable("message.galactic-space.discovered_body", body.getName())
                );
            }
            
            // Save the discovery to persistent data
            saveDiscoveries(player);
        }
    }

    @Override
    public Optional<ICelestialBody> getCurrentCelestialBody(Player player) {
        // In a full implementation, this would check the player's dimension
        // For now, just return Earth as the default
        return getCelestialBody(SpaceBodies.EARTH.getId());
    }

    @Override
    public void registerCelestialBody(ICelestialBody body) {
        CelestialBodyRegistry.register(body);
    }

    @Override
    public Optional<ICelestialBody> getCelestialBody(ResourceLocation id) {
        return CelestialBodyRegistry.get(id);
    }

    @Override
    public List<ICelestialBody> getAllCelestialBodies() {
        return new ArrayList<>(CelestialBodyRegistry.getAll());
    }

    @Override
    public int calculateFuelRequired(ICelestialBody origin, ICelestialBody destination) {
        // Simple fuel calculation based on distance
        return Math.abs(destination.getDistanceFromHome() - origin.getDistanceFromHome()) * 10;
    }

    @Override
    public boolean canTravelTo(Player player, ICelestialBody destination) {
        // In a full implementation, this would check if the player has the necessary equipment
        // For now, just check if the celestial body has been discovered
        return hasDiscovered(player, destination);
    }
    
    /**
     * Saves the player's discovered celestial bodies.
     * @param player The player
     */
    private void saveDiscoveries(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag data = persistentData.getCompound(Player.PERSISTED_NBT_TAG);
        
        List<ResourceLocation> discovered = discoveredBodies.getOrDefault(
                player.getUUID(), new ArrayList<>());
        
        ListTag list = new ListTag();
        for (ResourceLocation id : discovered) {
            list.add(StringTag.valueOf(id.toString()));
        }
        
        data.put(DISCOVERED_TAG, list);
        persistentData.put(Player.PERSISTED_NBT_TAG, data);
    }
    
    /**
     * Loads the player's discovered celestial bodies.
     * @param player The player
     */
    public void loadDiscoveries(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        
        if (persistentData.contains(Player.PERSISTED_NBT_TAG)) {
            CompoundTag data = persistentData.getCompound(Player.PERSISTED_NBT_TAG);
            
            if (data.contains(DISCOVERED_TAG)) {
                ListTag list = data.getList(DISCOVERED_TAG, 8); // 8 is the ID for StringTag
                List<ResourceLocation> discovered = new ArrayList<>();
                
                for (int i = 0; i < list.size(); i++) {
                    String idString = list.getString(i);
                    discovered.add(ResourceLocation.parse(idString));
                }
                
                discoveredBodies.put(player.getUUID(), discovered);
            }
        }
    }
}