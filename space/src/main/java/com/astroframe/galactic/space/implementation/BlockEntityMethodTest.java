package com.astroframe.galactic.space.implementation;

import com.astroframe.galactic.core.api.block.BlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Test class to demonstrate standard implementation of BlockEntityBase
 * in NeoForge 1.21.5
 */
public class BlockEntityMethodTest extends BlockEntityBase {
    
    // Example private data that would be serialized
    private int testValue = 0;
    
    /**
     * Constructor for BlockEntityMethodTest.
     * 
     * @param pType The block entity type
     * @param pPos The block position
     * @param pBlockState The block state
     */
    public BlockEntityMethodTest(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }
    
    /**
     * Implementation of loadData from BlockEntityBase.
     * This method is called by loadAdditional to load entity data.
     * 
     * @param tag The tag to load data from
     */
    @Override
    protected void loadData(CompoundTag tag) {
        // Example of loading data
        if (tag.contains("TestValue")) {
            this.testValue = tag.getInt("TestValue").orElse(0);
        }
    }
    
    /**
     * Implementation of saveData from BlockEntityBase.
     * This method is called by saveAdditional to save entity data.
     * 
     * @param tag The tag to save data to
     */
    @Override
    protected void saveData(CompoundTag tag) {
        // Example of saving data
        tag.putInt("TestValue", this.testValue);
    }
    
    /**
     * Gets the test value.
     * 
     * @return The test value
     */
    public int getTestValue() {
        return testValue;
    }
    
    /**
     * Sets the test value.
     * 
     * @param value The test value
     */
    public void setTestValue(int value) {
        this.testValue = value;
        this.setChanged();
    }
}