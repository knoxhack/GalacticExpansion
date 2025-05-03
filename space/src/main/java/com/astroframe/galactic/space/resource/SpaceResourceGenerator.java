package com.astroframe.galactic.space.resource;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.dimension.SpaceStationDimension;
import com.astroframe.galactic.space.registry.SpaceBodies;
import com.astroframe.galactic.space.registry.SpaceItems;
import com.astroframe.galactic.space.item.SpaceSuitItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles generation of space-exclusive resources.
 */
public class SpaceResourceGenerator {
    private static final Map<ResourceLocation, ResourceDistribution> CELESTIAL_BODY_RESOURCES = new HashMap<>();
    
    /**
     * Initialize the resource generator system.
     */
    public static void init() {
        GalacticSpace.LOGGER.info("Initializing space resource generator");
        
        // Register space station resources
        registerCelestialBodyResources(SpaceBodies.SPACE_STATION.getId(), new ResourceDistribution()
            .addResource(com.astroframe.galactic.space.registry.SpaceItems.MOON_DUST.get(), 0.15f, 1, 2)
            .addResource(com.astroframe.galactic.space.registry.SpaceItems.MARS_ROCK.get(), 0.6f, 1, 3));
        
        // Register earth (no special resources)
        registerCelestialBodyResources(SpaceBodies.EARTH.getId(), new ResourceDistribution());
        
        // Register block break handler
        NeoForge.EVENT_BUS.addListener(SpaceResourceGenerator::onBlockBreak);
        
        GalacticSpace.LOGGER.info("Space resource generator initialized");
    }
    
    /**
     * Registers resource distribution for a celestial body.
     *
     * @param celestialBodyId The celestial body ID
     * @param distribution The resource distribution
     */
    public static void registerCelestialBodyResources(ResourceLocation celestialBodyId, ResourceDistribution distribution) {
        CELESTIAL_BODY_RESOURCES.put(celestialBodyId, distribution);
    }
    
    /**
     * Handle block break events in space dimensions.
     *
     * @param event The block break event
     */
    private static void onBlockBreak(BlockEvent.BreakEvent event) {
        Level level = (Level)event.getLevel();
        Player player = event.getPlayer();
        
        // Only handle events on the server and in space dimensions
        if (level.isClientSide() || !isInSpaceDimension(level)) {
            return;
        }
        
        // Skip if the player doesn't have a space suit
        if (!SpaceSuitItem.hasFullSpaceSuit(player)) {
            return;
        }
        
        // Get the celestial body for this dimension
        ICelestialBody celestialBody = getCelestialBodyForDimension(level);
        if (celestialBody == null || !celestialBody.hasUniqueResources()) {
            return;
        }
        
        // Get the resource distribution for this celestial body
        ResourceDistribution distribution = CELESTIAL_BODY_RESOURCES.get(celestialBody.getId());
        if (distribution == null) {
            return;
        }
        
        // Check if we should generate resources based on the block broken
        BlockState state = event.getState();
        if (isValidResourceBlock(state)) {
            tryGenerateResources(distribution, (ServerPlayer) player, event.getPos());
        }
    }
    
    /**
     * Try to generate and give resources to a player.
     *
     * @param distribution The resource distribution
     * @param player The player
     * @param pos The block position
     */
    private static void tryGenerateResources(ResourceDistribution distribution, ServerPlayer player, BlockPos pos) {
        RandomSource random = player.getRandom();
        
        // Higher tier space suits have better resource collection chances
        float bonusChance = (SpaceSuitItem.getMinimumSuitTier(player) - 1) * 0.1f;
        
        distribution.generateResources(random, bonusChance).forEach(stack -> {
            // Give player the resource
            if (!player.getInventory().add(stack)) {
                // Drop item if inventory is full
                player.drop(stack, false);
            }
            
            // Notify player
            player.sendSystemMessage(Component.translatable("message.galactic-space.resource_found", 
                    stack.getCount(), stack.getHoverName()));
        });
    }
    
    /**
     * Checks if a block can drop space resources.
     *
     * @param state The block state
     * @return true if this is a valid resource block
     */
    private static boolean isValidResourceBlock(BlockState state) {
        // Stone-type blocks can drop space resources
        return state.is(Blocks.STONE) || 
               state.is(Blocks.DEEPSLATE) || 
               state.is(Blocks.IRON_ORE) ||
               state.is(Blocks.DEEPSLATE_IRON_ORE) ||
               state.is(Blocks.DIORITE) ||
               state.is(Blocks.ANDESITE) ||
               state.is(Blocks.GRANITE);
    }
    
    /**
     * Checks if a level is a space dimension.
     *
     * @param level The level
     * @return true if this is a space dimension
     */
    private static boolean isInSpaceDimension(Level level) {
        return SpaceStationDimension.isSpaceStation(level);
    }
    
    /**
     * Gets the celestial body for a dimension.
     *
     * @param level The level
     * @return The celestial body, or null if not found
     */
    private static ICelestialBody getCelestialBodyForDimension(Level level) {
        if (SpaceStationDimension.isSpaceStation(level)) {
            return SpaceBodies.SPACE_STATION;
        } else if (level.dimension() == Level.OVERWORLD) {
            return SpaceBodies.EARTH;
        }
        
        return null;
    }
}