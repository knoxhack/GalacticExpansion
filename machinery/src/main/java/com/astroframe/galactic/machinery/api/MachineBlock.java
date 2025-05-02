package com.astroframe.galactic.machinery.api;

// Use the correct Minecraft imports for NeoForge 1.21.5
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;


import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

/**
 * Base block class for all machines.
 * Handles common machine block functionality like facing direction and active state.
 */
public abstract class MachineBlock extends Block implements EntityBlock {
    // Use the FACING property directly from HorizontalDirectionalBlock
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    
    private final Supplier<BlockEntityType<? extends MachineBlockEntity>> blockEntityType;
    
    /**
     * Constructor for MachineBlock.
     * 
     * @param properties The block properties
     * @param blockEntityType The block entity type supplier
     */
    public MachineBlock(Properties properties, Supplier<BlockEntityType<? extends MachineBlockEntity>> blockEntityType) {
        super(properties);
        this.blockEntityType = blockEntityType;
        
        // Set default state with horizontal direction
        registerDefaultState(stateDefinition.any()
                .setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                .setValue(ACTIVE, false));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HorizontalDirectionalBlock.FACING, ACTIVE);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection().getOpposite());
    }
    
    // This method implements Block.use
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        
        if (level.getBlockEntity(pos) instanceof MachineBlockEntity machine) {
            return machine.onBlockActivated(state, level, pos, player, hand, hit);
        }
        
        return InteractionResult.PASS;
    }
    
    // Method name might have changed in NeoForge 1.21.5
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof MachineBlockEntity machine) {
                machine.dropContents(level, pos);
            }
            
            // Call the block's parent onRemove method with appropriate parameters
            // In NeoForge 1.21.5, use a method that exists in the parent class
            super.playerWillDestroy(state, level, pos, null);
        }
    }
    
    /**
     * Creates a ticker for the machine block entity.
     * 
     * @param level The world
     * @param state The block state
     * @param type The block entity type
     * @param <T> The block entity type parameter
     * @return A ticker for the block entity
     */
    protected <T extends BlockEntity> BlockEntityTicker<T> createMachineTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide 
                ? null 
                : (lvl, pos, blockState, blockEntity) -> {
                    if (blockEntity instanceof MachineBlockEntity machine) {
                        machine.serverTick(lvl, pos, blockState);
                    }
                };
    }
    
    /**
     * Updates the active state of a machine.
     * 
     * @param active Whether the machine is active
     * @param level The world
     * @param pos The block position
     */
    public static void setActive(boolean active, Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getValue(ACTIVE) != active) {
            level.setBlock(pos, state.setValue(ACTIVE, active), 3);
        }
    }
    
    /**
     * Gets the block entity type for this machine.
     * 
     * @return The block entity type
     */
    public BlockEntityType<? extends MachineBlockEntity> getBlockEntityType() {
        return blockEntityType.get();
    }
}