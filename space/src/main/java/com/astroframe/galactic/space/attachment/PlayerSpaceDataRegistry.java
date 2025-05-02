package com.astroframe.galactic.space.attachment;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.NeoForgeAttachmentTypes;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

/**
 * Registry for player space data attachment.
 * Manages attachment registration and provides static access methods.
 */
public class PlayerSpaceDataRegistry {
    public static final ResourceLocation PLAYER_SPACE_DATA_RL = 
        new ResourceLocation(GalacticSpace.MODID, "player_space_data");
    
    private static AttachmentType<PlayerSpaceDataAttachment> PLAYER_SPACE_DATA;
    
    /**
     * Register the player space data attachment type.
     * This should be called during the RegisterEvent for AttachmentTypes.
     */
    public static void register(RegisterEvent event) {
        if (event.getRegistryKey().equals(NeoForgeAttachmentTypes.ATTACHMENT_TYPES.getRegistryKey())) {
            PLAYER_SPACE_DATA = AttachmentType.serializable(PlayerSpaceDataAttachment::read)
                .factory(() -> new PlayerSpaceDataAttachment())
                .build();
                
            event.register(NeoForgeAttachmentTypes.ATTACHMENT_TYPES.getRegistryKey(), 
                           PLAYER_SPACE_DATA_RL, 
                           () -> PLAYER_SPACE_DATA);
            
            GalacticSpace.LOGGER.info("Registered player space data attachment type");
        }
    }
    
    /**
     * Get the player space data attachment for a player.
     * @param player The player to get the attachment for
     * @return The player's space data attachment
     */
    public static PlayerSpaceDataAttachment get(Player player) {
        if (player == null) {
            GalacticSpace.LOGGER.warn("Attempted to get PlayerSpaceDataAttachment for null player");
            return null;
        }
        
        if (PLAYER_SPACE_DATA == null) {
            GalacticSpace.LOGGER.error("PlayerSpaceDataAttachment type not registered yet");
            return null;
        }
        
        return player.getData(PLAYER_SPACE_DATA);
    }
    
    /**
     * Check if a player has the space data attachment.
     * @param player The player to check
     * @return true if the player has the attachment
     */
    public static boolean has(Player player) {
        if (player == null || PLAYER_SPACE_DATA == null) {
            return false;
        }
        
        return ((IAttachmentHolder)player).hasData(PLAYER_SPACE_DATA);
    }
}