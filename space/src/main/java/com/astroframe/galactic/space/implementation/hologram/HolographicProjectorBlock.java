package com.astroframe.galactic.space.implementation.hologram;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * Holographic Projector block for displaying 3D holograms of rockets.
 * This block will display a holographic preview of the rocket being assembled
 * in the Rocket Assembly Table.
 */
public class HolographicProjectorBlock extends Block implements EntityBlock {
    
    // Block state properties
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    
    // Shape of the block
    private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
    
    /**
     * Constructor for the Holographic Projector block.
     *
     * @param properties The block properties
     */
    public HolographicProjectorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, Boolean.FALSE)
                .setValue(ACTIVE, Boolean.FALSE));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, ACTIVE);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
    
    /**
     * Handles player interaction with the block.
     * Right-clicking will toggle the hologram display.
     * 
     * Method updated for NeoForge 1.21.5
     */
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, 
                              InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof HolographicProjectorBlockEntity holographicProjector) {
            // Toggle the active state
            boolean newActiveState = !state.getValue(ACTIVE);
            level.setBlock(pos, state.setValue(ACTIVE, newActiveState), 3);
            
            // Update the block entity
            holographicProjector.setActive(newActiveState);
            
            return InteractionResult.CONSUME;
        }
        
        return InteractionResult.PASS;
    }
    
    /**
     * Called when a neighboring block changes.
     * Updates the powered state based on redstone signal.
     * 
     * Method updated for NeoForge 1.21.5
     */
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean powered = level.hasNeighborSignal(pos);
            
            // Only update if the powered state has changed
            if (state.getValue(POWERED) != powered) {
                level.setBlock(pos, state.setValue(POWERED, powered), 3);
                
                // If powered, activate the hologram
                if (powered) {
                    level.setBlock(pos, state.setValue(POWERED, true).setValue(ACTIVE, true), 3);
                    
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof HolographicProjectorBlockEntity holographicProjector) {
                        holographicProjector.setActive(true);
                    }
                }
            }
        }
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HolographicProjectorBlockEntity(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (lvl, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof HolographicProjectorBlockEntity holographicProjector) {
                holographicProjector.tick(lvl, pos, blockState);
            }
        };
    }
}