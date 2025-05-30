package com.astroframe.galactic.space.implementation.hologram;

import com.astroframe.galactic.core.api.block.BlockEntityBase;
import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.space.SpaceModule;
import com.astroframe.galactic.space.implementation.common.HolographicProjectorAccess;
import com.astroframe.galactic.space.implementation.common.RocketDataProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;



/**
 * BlockEntity for the holographic projector.
 * Manages the state and rendering of holographic rocket projections.
 * Implements HolographicProjectorAccess for interface-based access
 * to avoid circular dependencies.
 */
public class HolographicProjectorBlockEntity extends BlockEntityBase implements HolographicProjectorAccess {
    
    // Size of the hologram projection
    public static final double HOLOGRAM_HEIGHT = 4.0D;
    public static final double HOLOGRAM_WIDTH = 3.0D;
    public static final double HOLOGRAM_DEPTH = 3.0D;
    
    // Whether the projector is currently active
    private boolean active = false;
    
    // Rotation angle of the hologram (changes over time)
    private float rotationAngle = 0.0F;
    
    // The holographic rocket data (might be linked to an actual rocket assembly table)
    private IRocket rocketData = null;
    
    // The position of a linked rocket assembly table (if any)
    private BlockPos linkedTablePos = null;
    
    /**
     * Construct a new HolographicProjectorBlockEntity.
     *
     * @param pos The position of the block entity
     * @param state The block state
     */
    public HolographicProjectorBlockEntity(BlockPos pos, BlockState state) {
        super(SpaceModule.HOLOGRAPHIC_PROJECTOR_BLOCK_ENTITY.get(), pos, state);
    }
    
    /**
     * Tick method that's called each game tick to update the hologram.
     *
     * @param level The level
     * @param pos The position
     * @param state The block state
     */
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide && active) {
            // Update the rotation angle
            rotationAngle += 0.5F;
            if (rotationAngle >= 360.0F) {
                rotationAngle = 0.0F;
            }
            
            // Check if we need to update the rocket data from a linked table
            if (linkedTablePos != null) {
                if (level.getBlockEntity(linkedTablePos) instanceof RocketDataProvider provider) {
                    // Use the common interface to get rocket data
                    rocketData = provider.getRocketData();
                }
            }
            
            // Mark the block for re-render to update the hologram visuals
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }
    
    @Override
    protected void loadData(CompoundTag tag) {
        // Load active state - updated for NeoForge 1.21.5 compatibility
        if (tag.contains("Active")) {
            // Get boolean value directly in NeoForge 1.21.5
            active = tag.getBoolean("Active");
        }
        
        // Load rotation angle - updated for NeoForge 1.21.5 compatibility
        if (tag.contains("RotationAngle")) {
            // Get float value directly in NeoForge 1.21.5
            rotationAngle = tag.getFloat("RotationAngle");
        }
        
        // Load linked table position if it exists - updated approach for NeoForge 1.21.5
        if (tag.contains("LinkedTable")) {
            // Must get the tag as a generic Tag first and check its type
            Tag rawTag = tag.get("LinkedTable");
            // In NeoForge 1.21.5, we need to check if it's a CompoundTag by instanceof
            if (rawTag != null && rawTag instanceof CompoundTag) {
                // Now we can safely cast to CompoundTag
                CompoundTag linkedTag = (CompoundTag) rawTag;
                
                if (!linkedTag.isEmpty() &&
                    linkedTag.contains("X") && 
                    linkedTag.contains("Y") && 
                    linkedTag.contains("Z")) {
                    
                    // For NeoForge 1.21.5, get int values directly
                    int x = linkedTag.getInt("X");
                    int y = linkedTag.getInt("Y");
                    int z = linkedTag.getInt("Z");
                    
                    linkedTablePos = new BlockPos(x, y, z);
                } else {
                    linkedTablePos = null;
                }
            } else {
                linkedTablePos = null;
            }
        } else {
            linkedTablePos = null;
        }
    }
    
    @Override
    protected void saveData(CompoundTag tag) {
        // Save active state
        tag.putBoolean("Active", active);
        
        // Save rotation angle
        tag.putFloat("RotationAngle", rotationAngle);
        
        // Save linked table position if it exists
        if (linkedTablePos != null) {
            CompoundTag posTag = new CompoundTag();
            posTag.putInt("X", linkedTablePos.getX());
            posTag.putInt("Y", linkedTablePos.getY());
            posTag.putInt("Z", linkedTablePos.getZ());
            tag.put("LinkedTable", posTag);
        }
    }
    
    /**
     * Sets whether the projector is active.
     *
     * @param active The active state
     */
    public void setActive(boolean active) {
        this.active = active;
        setChanged();
        
        // Sync to client
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
    
    /**
     * Gets whether the projector is active.
     *
     * @return The active state
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * Gets the current rotation angle of the hologram.
     *
     * @return The rotation angle in degrees
     */
    public float getRotationAngle() {
        return rotationAngle;
    }
    
    /**
     * Links this projector to a rocket assembly table.
     *
     * @param tablePos The position of the assembly table
     */
    public void linkToAssemblyTable(BlockPos tablePos) {
        this.linkedTablePos = tablePos;
        setChanged();
    }
    
    /**
     * Gets the linked assembly table position.
     *
     * @return The position or null if not linked
     */
    public BlockPos getLinkedTablePos() {
        return linkedTablePos;
    }
    
    /**
     * Sets the rocket data to display.
     *
     * @param rocket The rocket data
     */
    public void setRocketData(IRocket rocket) {
        this.rocketData = rocket;
        setChanged();
    }
    
    /**
     * Gets the rocket data being displayed.
     *
     * @return The rocket data or null if none
     */
    public IRocket getRocketData() {
        return rocketData;
    }
    
    /**
     * Increase the render distance for this block entity.
     */
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition).inflate(HOLOGRAM_HEIGHT);
    }
}