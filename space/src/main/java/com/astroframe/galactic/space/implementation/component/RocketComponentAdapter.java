package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.RocketComponent;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

/**
 * Adapter class to convert between IRocketComponent and RocketComponent.
 * This adapter handles the compatibility between interface components and concrete implementation.
 */
public class RocketComponentAdapter extends RocketComponent {
    
    private final IRocketComponent component;
    
    /**
     * Create a new adapter for an existing component.
     * 
     * @param component The component to adapt
     */
    public RocketComponentAdapter(IRocketComponent component) {
        super(component.getType(), component.getTier());
        this.component = component;
        // Copy additional properties from component
        this.setDurability(component.getCurrentDurability());
    }
    
    /**
     * Gets the original component.
     * 
     * @return The original component
     */
    public IRocketComponent getWrappedComponent() {
        return component;
    }
    
    @Override
    public CompoundTag save(CompoundTag tag) {
        // Let the component save its data first
        component.save(tag);
        // Then save any additional RocketComponent specific data
        return super.save(tag);
    }
    
    @Override
    public void load(CompoundTag tag) {
        // Load component data
        component.load(tag);
        // Load RocketComponent specific data
        super.load(tag);
    }
    
    @Override
    public float getMass() {
        return component.getMass();
    }
    
    @Override
    public float getDurability() {
        return component.getCurrentDurability();
    }
    
    @Override
    public float getMaxDurability() {
        return component.getMaxDurability();
    }
    
    @Override
    public float getEfficiency() {
        if (component instanceof EngineComponent) {
            // Explicitly convert to float to avoid inference issues
            return (float)((EngineComponent) component).getEfficiency();
        }
        return super.getEfficiency();
    }
}