package com.astroframe.galactic.machinery.blocks.custom;

import com.astroframe.galactic.machinery.GalacticMachinery;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

/**
 * The assembler block for creating modular machines.
 */
public class AssemblerBlock extends Block {

    // State properties
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    /**
     * Creates a new AssemblerBlock with the given properties.
     *
     * @param properties The block properties
     */
    public AssemblerBlock(Properties properties) {
        // Don't use noLootTable() in NeoForge 1.21.5 as it's causing initialization issues
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }
}