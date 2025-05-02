package com.astroframe.galactic.space.attachment;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Registry for player space data attachment.
 * Manages attachment registration and provides static access methods.
 */
public class PlayerSpaceDataRegistry {
    public static final ResourceLocation PLAYER_SPACE_DATA_RL = 
        ResourceLocation.parse(GalacticSpace.MOD_ID + ":player_space_data");
    
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = 
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, GalacticSpace.MOD_ID);
    
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerSpaceDataAttachment>> PLAYER_SPACE_DATA = 
        ATTACHMENT_TYPES.register("player_space_data", 
            () -> {
                // Create a new basic attachment type with factory method
                return new AttachmentType<PlayerSpaceDataAttachment>(
                    // Constructor factory
                    PlayerSpaceDataAttachment::new, 
                    // Serialization handler
                    (attachment, tag) -> attachment.write(tag), 
                    // Deserialization handler
                    PlayerSpaceDataAttachment::read
                );
            });
    
    /**
     * Register the player space data attachment type.
     * This should be called during mod initialization.
     */
    public static void register() {
        ATTACHMENT_TYPES.register(GalacticSpace.MOD_EVENT_BUS);
        GalacticSpace.LOGGER.info("Registered player space data attachment type");
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