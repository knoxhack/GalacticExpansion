package com.astroframe.galactic.space.migration;

import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.attachment.PlayerSpaceDataAttachment;
import com.astroframe.galactic.space.attachment.PlayerSpaceDataRegistry;
import com.astroframe.galactic.space.util.TagHelper;
import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.core.api.space.ICelestialBody.CelestialBodyType;
import com.astroframe.galactic.core.api.space.ICelestialBody.TemperatureRange;
import com.astroframe.galactic.core.api.space.ICelestialBody.RadiationLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Utility class for migrating player space data from the old capability system
 * to the new attachment system.
 */
public class PlayerDataMigration {
    private static final String LEGACY_CAPABILITY_ID = 
        GalacticSpace.MOD_ID + ":player_space_data_capability";
    
    private static final String MIGRATION_MARKER = GalacticSpace.MOD_ID + ".data_migrated";
    
    /**
     * Check whether the player has been migrated to the new attachment system.
     * @param player The player to check
     * @return true if the player has been migrated
     */
    public static boolean isPlayerMigrated(Player player) {
        CompoundTag persistedData = player.getPersistentData();
        if (persistedData.contains("PlayerPersisted")) {
            CompoundTag playerPersisted = com.astroframe.galactic.space.util.TagHelper.getCompoundTag(persistedData, "PlayerPersisted");
            return com.astroframe.galactic.space.util.TagHelper.getBoolean(playerPersisted, MIGRATION_MARKER);
        }
        return false;
    }
    
    /**
     * Mark a player as migrated to the new attachment system.
     * @param player The player to mark
     */
    public static void markPlayerMigrated(Player player) {
        CompoundTag persistedData = player.getPersistentData();
        CompoundTag playerPersisted;
        if (persistedData.contains("PlayerPersisted")) {
            playerPersisted = com.astroframe.galactic.space.util.TagHelper.getCompoundTag(persistedData, "PlayerPersisted");
        } else {
            playerPersisted = new CompoundTag();
        }
        playerPersisted.putBoolean(MIGRATION_MARKER, true);
        persistedData.put("PlayerPersisted", playerPersisted);
    }
    
    /**
     * Attempt to migrate player data from the old capability system
     * to the new attachment system.
     * 
     * @param player The player to migrate
     * @return true if migration was successful or already migrated
     */
    public static boolean migratePlayerData(Player player) {
        // Check if already migrated
        if (isPlayerMigrated(player)) {
            return true;
        }
        
        // Log migration attempt
        GalacticSpace.LOGGER.info("Attempting to migrate space data for player: " + player.getDisplayName().getString());
        
        // Get legacy capability data if available
        try {
            CompoundTag legacyData = getLegacyCapabilityData(player);
            if (legacyData != null) {
                // Get the attachment
                PlayerSpaceDataAttachment attachment = PlayerSpaceDataRegistry.get(player);
                if (attachment != null) {
                    // Use the read method to populate from the legacy tag
                    PlayerSpaceDataAttachment migratedData = PlayerSpaceDataAttachment.read(legacyData);
                    
                    // Copy all fields from migratedData to the attachment
                    copyAttachmentData(migratedData, attachment);
                    
                    // Mark as migrated
                    markPlayerMigrated(player);
                    GalacticSpace.LOGGER.info("Successfully migrated space data for player: " + player.getDisplayName().getString());
                    return true;
                }
            }
        } catch (Exception e) {
            GalacticSpace.LOGGER.error("Failed to migrate player data: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Helper method to copy data between attachment instances.
     * @param source The source attachment
     * @param target The target attachment
     */
    private static void copyAttachmentData(PlayerSpaceDataAttachment source, PlayerSpaceDataAttachment target) {
        // Copy all discovered celestial bodies
        source.getDiscoveredBodies().forEach(bodyId -> {
            if (!target.getDiscoveredBodies().contains(bodyId)) {
                // We can't add directly to the list, so we need to create a mock body
                target.discoverCelestialBody(new MockCelestialBody(bodyId));
            }
        });
        
        // Copy last visited body
        if (source.getLastVisitedBodyId() != null) {
            target.setLastVisitedBody(new MockCelestialBody(source.getLastVisitedBodyId()));
        }
        
        // Copy experience
        target.addExperience(source.getExperience());
        
        // Copy space state
        target.setInSpace(source.isInSpace());
        
        // Copy current rocket
        target.setCurrentRocketId(source.getCurrentRocketId());
        
        // Copy current dimension
        target.setCurrentDimension(source.getCurrentDimension());
        
        // Copy dimension change state
        target.setChangingDimension(source.isChangingDimension());
    }
    
    /**
     * Helper method to get legacy capability data.
     * This is a placeholder that would need to be implemented based on the
     * actual capability registration and access.
     * 
     * @param player The player to get legacy data for
     * @return The legacy data as a CompoundTag, or null if not available
     */
    private static CompoundTag getLegacyCapabilityData(Player player) {
        // In a fully implemented version, this would use the capability API to get the data
        // For migration purposes, we can also check if the player has the data in NBT form
        
        CompoundTag persistedData = player.getPersistentData();
        if (persistedData.contains("ForgeCaps")) {
            CompoundTag forgeCaps = com.astroframe.galactic.space.util.TagHelper.getCompoundTag(persistedData, "ForgeCaps");
            if (forgeCaps.contains(LEGACY_CAPABILITY_ID)) {
                return com.astroframe.galactic.space.util.TagHelper.getCompoundTag(forgeCaps, LEGACY_CAPABILITY_ID);
            }
        }
        
        return null;
    }
    
    /**
     * Register event handlers for migration.
     */
    public static void registerEvents() {
        // Register player login event for migration
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(PlayerDataMigration::onPlayerLoggedIn);
    }
    
    /**
     * Handle player login events for migration.
     * @param event The player logged in event
     */
    private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player != null && !player.level().isClientSide()) {
            // Attempt migration on login
            migratePlayerData(player);
        }
    }
    
    /**
     * Simple mock implementation of ICelestialBody for migration purposes.
     */
    private static class MockCelestialBody implements com.astroframe.galactic.core.api.space.ICelestialBody {
        private final ResourceLocation id;
        
        public MockCelestialBody(ResourceLocation id) {
            this.id = id;
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
        
        @Override
        public String getName() {
            return id.toString();
        }
        
        @Override
        public float getDistance() {
            return 1.0f;
        }
        
        @Override
        public int getRequiredTier() {
            return 1;
        }
        
        @Override
        public float getGravity() {
            return 1.0f;
        }
        
        @Override
        public boolean hasAtmosphere() {
            return true;
        }
        
        @Override
        public float getAtmosphericPressure() {
            return 1.0f;
        }
        
        @Override
        public float getTemperature() {
            return 20.0f;
        }
        
        private boolean discovered = true;
        
        // Extra methods for compatibility with old code
        public boolean isDiscovered() {
            return discovered;
        }
        
        public void setDiscovered(boolean discovered) {
            this.discovered = discovered;
        }
    }
}