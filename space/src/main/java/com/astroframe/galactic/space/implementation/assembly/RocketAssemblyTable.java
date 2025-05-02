package com.astroframe.galactic.space.implementation.assembly;

import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.space.SpaceModule;
import com.astroframe.galactic.space.implementation.hologram.HolographicProjectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * A block for assembling and customizing rockets.
 * Provides an interface for adding/removing rocket components and
 * connects to holographic projectors to display the rocket design.
 */
public class RocketAssemblyTable extends BaseEntityBlock {
    
    // Properties
    public static final Property<Direction> FACING = HorizontalDirectionalBlock.FACING;
    
    // Block shapes based on facing direction
    private static final VoxelShape SHAPE_NORTH = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SHAPE_SOUTH = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SHAPE_EAST = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SHAPE_WEST = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    
    /**
     * Constructor for the rocket assembly table.
     *
     * @param properties Block properties
     */
    public RocketAssemblyTable(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case EAST -> SHAPE_EAST;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }
    
    /**
     * Handles player interaction with the assembly table.
     *
     * @param state Current block state
     * @param level The level
     * @param pos Block position
     * @param player Interacting player
     * @param hand Used hand
     * @param hit Hit result
     * @return Interaction result
     */
    // Method for NeoForge 1.21.5
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, 
                               InteractionHand hand, BlockHitResult hit) {
        // Open the rocket assembly UI when right-clicked
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof RocketAssemblyTableBlockEntity assemblyTable) {
                // Open the container menu for this assembly table
                player.openMenu(assemblyTable);
                return InteractionResult.CONSUME;
            }
        }
        
        return InteractionResult.SUCCESS;
    }
    
    @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RocketAssemblyTableBlockEntity(pos, state);
    }
    
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (lvl, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof RocketAssemblyTableBlockEntity assemblyTable) {
                assemblyTable.tick(lvl, pos, blockState);
            }
        };
    }
    
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    
    /**
     * Called after this block is placed.
     * Searches for nearby holographic projectors to link with.
     */
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        
        if (!level.isClientSide) {
            // Look for holographic projectors in a 5x5x5 area
            BlockPos.betweenClosedStream(pos.offset(-2, -2, -2), pos.offset(2, 2, 2)).forEach(projectorPos -> {
                BlockState blockState = level.getBlockState(projectorPos);
                
                // If we find a holographic projector, link it to this assembly table
                if (blockState.is(SpaceModule.HOLOGRAPHIC_PROJECTOR.get())) {
                    BlockEntity blockEntity = level.getBlockEntity(projectorPos);
                    if (blockEntity instanceof HolographicProjectorBlockEntity projector) {
                        projector.linkToAssemblyTable(pos);
                        
                        // Get the rocket data to display in the projector
                        BlockEntity assemblyEntity = level.getBlockEntity(pos);
                        if (assemblyEntity instanceof RocketAssemblyTableBlockEntity assemblyTable) {
                            projector.setRocketData(assemblyTable.getRocketData());
                        }
                    }
                }
            });
        }
    }
    
    /**
     * Called when this block is removed.
     * Unlinks any connected holographic projectors.
     * Override from BaseEntityBlock for NeoForge 1.21.5
     */
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.is(newState.getBlock())) {
            // If it's the same block type, normal behavior applies
            super.onRemove(state, level, pos, newState, isMoving);
            return;
        }
        
        if (!level.isClientSide) {
            // Look for holographic projectors in a 5x5x5 area that might be linked to this
            BlockPos.betweenClosedStream(pos.offset(-2, -2, -2), pos.offset(2, 2, 2)).forEach(projectorPos -> {
                BlockEntity blockEntity = level.getBlockEntity(projectorPos);
                
                // If we find a linked projector, unlink it
                if (blockEntity instanceof HolographicProjectorBlockEntity projector) {
                    BlockPos linkedPos = projector.getLinkedTablePos();
                    if (linkedPos != null && linkedPos.equals(pos)) {
                        projector.linkToAssemblyTable(null);
                        projector.setRocketData(null);
                    }
                }
            });
        }
        
        // Must call this from BaseEntityBlock to properly remove the BlockEntity
        super.onRemove(state, level, pos, newState, isMoving);
    }
}