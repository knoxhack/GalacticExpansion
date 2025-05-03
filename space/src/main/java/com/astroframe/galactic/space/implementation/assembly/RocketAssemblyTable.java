package com.astroframe.galactic.space.implementation.assembly;

import com.astroframe.galactic.space.registry.SpaceBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * The rocket assembly table block.
 * This is used for constructing rockets from components.
 */
public class RocketAssemblyTable extends Block implements EntityBlock {
    
    /**
     * Creates a new rocket assembly table.
     *
     * @param properties The block properties
     */
    public RocketAssemblyTable(Properties properties) {
        super(properties);
    }
    
    /**
     * Creates the block entity for this block.
     *
     * @param pos The position
     * @param state The block state
     * @return The block entity
     */
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return SpaceBlockEntities.ROCKET_ASSEMBLY_TABLE.get().create(pos, state);
    }
    
    /**
     * Handles interaction with the block.
     * Opens the menu when the player right clicks on the block.
     *
     * @param state The block state
     * @param level The level
     * @param pos The position
     * @param player The player
     * @param hand The hand
     * @param hit The hit result
     * @return The interaction result
     */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, 
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            // Get the block entity
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RocketAssemblyTableBlockEntity) {
                // Open the menu
                player.openMenu((RocketAssemblyTableBlockEntity) blockEntity);
                return InteractionResult.CONSUME;
            }
        }
        
        return InteractionResult.SUCCESS;
    }
}