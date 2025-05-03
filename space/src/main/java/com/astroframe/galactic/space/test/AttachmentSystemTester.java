package com.astroframe.galactic.space.test;

import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.attachment.PlayerSpaceDataAttachment;
import com.astroframe.galactic.space.attachment.PlayerSpaceDataRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.UUID;

/**
 * Utility class for testing the attachment system implementation.
 * This class subscribes to player events and performs tests on the attachment system.
 */
public class AttachmentSystemTester {
    
    /**
     * Called when a player logs in to test attachment functionality.
     * @param event The player login event
     */
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (player instanceof ServerPlayer) {
            testAttachmentSystem(player);
        }
    }
    
    /**
     * Test the attachment system by writing and reading data.
     * @param player The player to test with
     */
    private void testAttachmentSystem(Player player) {
        GalacticSpace.LOGGER.info("Testing attachment system for player: " + player.getName().getString());
        
        // Get the player's space data attachment
        PlayerSpaceDataAttachment attachment = PlayerSpaceDataRegistry.get(player);
        
        if (attachment == null) {
            GalacticSpace.LOGGER.error("Failed to get player space data attachment!");
            return;
        }
        
        GalacticSpace.LOGGER.info("Successfully retrieved player space data attachment");
        
        // Test setting and getting data
        testBasicDataOperations(attachment);
        
        // Test serialization/deserialization
        testSerialization(attachment);
        
        GalacticSpace.LOGGER.info("Attachment system tests completed");
    }
    
    /**
     * Test basic data operations on the attachment.
     * @param attachment The attachment to test
     */
    private void testBasicDataOperations(PlayerSpaceDataAttachment attachment) {
        // Experience
        int originalExp = attachment.getExperience();
        attachment.addExperience(100);
        int newExp = attachment.getExperience();
        
        GalacticSpace.LOGGER.info("Experience test: " + originalExp + " -> " + newExp);
        
        // Space state
        boolean originalInSpace = attachment.isInSpace();
        attachment.setInSpace(!originalInSpace);
        boolean newInSpace = attachment.isInSpace();
        
        GalacticSpace.LOGGER.info("Space state test: " + originalInSpace + " -> " + newInSpace);
        
        // Rocket ID
        UUID testRocketId = UUID.randomUUID();
        attachment.setCurrentRocketId(testRocketId);
        UUID retrievedRocketId = attachment.getCurrentRocketId();
        
        GalacticSpace.LOGGER.info("Rocket ID test: " + (retrievedRocketId != null && retrievedRocketId.equals(testRocketId)));
        
        // Dimension
        ResourceLocation testDimension = ResourceLocationHelper.parse("galactic-space:space_station");
        attachment.setCurrentDimension(testDimension);
        ResourceLocation retrievedDimension = attachment.getCurrentDimension();
        
        GalacticSpace.LOGGER.info("Dimension test: " + (retrievedDimension != null && retrievedDimension.equals(testDimension)));
    }
    
    /**
     * Test serialization and deserialization of the attachment.
     * @param attachment The attachment to test
     */
    private void testSerialization(PlayerSpaceDataAttachment attachment) {
        // Set up test data
        attachment.setInSpace(true);
        attachment.addExperience(500);
        UUID testRocketId = UUID.randomUUID();
        attachment.setCurrentRocketId(testRocketId);
        ResourceLocation testDimension = ResourceLocationHelper.parse("galactic-space:space_station");
        attachment.setCurrentDimension(testDimension);
        
        // Serialize to tag
        net.minecraft.nbt.CompoundTag tag = new net.minecraft.nbt.CompoundTag();
        attachment.write(tag);
        
        // Deserialize from tag
        PlayerSpaceDataAttachment deserialized = PlayerSpaceDataAttachment.read(tag);
        
        // Verify data integrity
        boolean experienceMatch = deserialized.getExperience() == attachment.getExperience();
        boolean inSpaceMatch = deserialized.isInSpace() == attachment.isInSpace();
        boolean rocketIdMatch = (deserialized.getCurrentRocketId() != null && 
                                 deserialized.getCurrentRocketId().equals(attachment.getCurrentRocketId()));
        boolean dimensionMatch = (deserialized.getCurrentDimension() != null && 
                                  deserialized.getCurrentDimension().equals(attachment.getCurrentDimension()));
        
        GalacticSpace.LOGGER.info("Serialization test results:");
        GalacticSpace.LOGGER.info("- Experience match: " + experienceMatch);
        GalacticSpace.LOGGER.info("- In space match: " + inSpaceMatch);
        GalacticSpace.LOGGER.info("- Rocket ID match: " + rocketIdMatch);
        GalacticSpace.LOGGER.info("- Dimension match: " + dimensionMatch);
    }
    
    /**
     * Register this class to the event bus.
     */
    public static void register() {
        GalacticSpace.MOD_EVENT_BUS.register(new AttachmentSystemTester());
    }
}