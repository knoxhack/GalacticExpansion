package com.astroframe.galactic.space.implementation.assembly;

import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.ModularRocket;
import com.astroframe.galactic.core.api.space.component.*;
import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.implementation.component.RocketComponentFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

/**
 * Block entity for the Rocket Assembly Table.
 * Allows construction of modular rockets from individual components.
 */
public class RocketAssemblyTable extends BlockEntity {
    
    // The components that have been added to the assembly table
    private final List<IRocketComponent> components = new ArrayList<>();
    
    // The constructed rocket, if assembly is complete
    private ModularRocket assembledRocket = null;
    
    // Assembly status
    private AssemblyStatus status = AssemblyStatus.INCOMPLETE;
    
    /**
     * Creates a new rocket assembly table block entity.
     * 
     * @param pos The position
     * @param state The block state
     */
    public RocketAssemblyTable(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    /**
     * Adds a component to the assembly table.
     * 
     * @param component The component to add
     * @return true if the component was added successfully
     */
    public boolean addComponent(IRocketComponent component) {
        if (component == null) {
            return false;
        }
        
        // Can't add components if assembly is complete
        if (status == AssemblyStatus.COMPLETE) {
            return false;
        }
        
        // Add the component
        components.add(component);
        
        // Mark dirty to save changes
        this.setChanged();
        
        // Update status
        validateAssembly();
        
        return true;
    }
    
    /**
     * Removes a component from the assembly table.
     * 
     * @param index The index of the component to remove
     * @return The removed component, or null if the index is invalid
     */
    public IRocketComponent removeComponent(int index) {
        if (index < 0 || index >= components.size()) {
            return null;
        }
        
        // Can't remove components if assembly is complete
        if (status == AssemblyStatus.COMPLETE) {
            return null;
        }
        
        // Remove the component
        IRocketComponent removed = components.remove(index);
        
        // Mark dirty to save changes
        this.setChanged();
        
        // Update status
        validateAssembly();
        
        return removed;
    }
    
    /**
     * Gets all components in the assembly table.
     * 
     * @return An unmodifiable list of components
     */
    public List<IRocketComponent> getComponents() {
        return Collections.unmodifiableList(components);
    }
    
    /**
     * Gets the status of the assembly.
     * 
     * @return The assembly status
     */
    public AssemblyStatus getStatus() {
        return status;
    }
    
    /**
     * Gets any assembly errors.
     * 
     * @return A list of error messages
     */
    public List<Component> getAssemblyErrors() {
        List<Component> errors = new ArrayList<>();
        
        // Check for missing required components
        if (!hasCommandModule()) {
            errors.add(Component.translatable("message.galactic-space.assembly.missing_command_module"));
        }
        
        if (!hasEngine()) {
            errors.add(Component.translatable("message.galactic-space.assembly.missing_engine"));
        }
        
        if (!hasFuelTank()) {
            errors.add(Component.translatable("message.galactic-space.assembly.missing_fuel_tank"));
        }
        
        // Check for life support if we have passenger compartments
        if (hasPassengerCompartment() && !hasLifeSupport()) {
            errors.add(Component.translatable("message.galactic-space.assembly.missing_life_support"));
        }
        
        return errors;
    }
    
    /**
     * Attempts to complete the rocket assembly.
     * 
     * @return true if assembly was completed successfully
     */
    public boolean completeAssembly() {
        if (status == AssemblyStatus.COMPLETE) {
            return true; // Already complete
        }
        
        if (status == AssemblyStatus.INCOMPLETE) {
            return false; // Can't complete if not valid
        }
        
        try {
            // Create a new rocket with the components
            ResourceLocation id = ResourceLocation.parse("galactic:rocket_" + UUID.randomUUID().toString().substring(0, 8));
            ModularRocket.Builder builder = new ModularRocket.Builder(id);
            
            // Add command module
            ICommandModule commandModule = null;
            for (IRocketComponent component : components) {
                if (component instanceof ICommandModule) {
                    commandModule = (ICommandModule) component;
                    break;
                }
            }
            
            if (commandModule != null) {
                builder.commandModule(commandModule);
            }
            
            // Add engines
            for (IRocketComponent component : components) {
                if (component instanceof IRocketEngine) {
                    builder.addEngine((IRocketEngine) component);
                }
            }
            
            // Add fuel tanks
            for (IRocketComponent component : components) {
                if (component instanceof IFuelTank) {
                    builder.addFuelTank((IFuelTank) component);
                }
            }
            
            // Add cargo bays
            for (IRocketComponent component : components) {
                if (component instanceof ICargoBay) {
                    builder.addCargoBay((ICargoBay) component);
                }
            }
            
            // Add passenger compartments
            for (IRocketComponent component : components) {
                if (component instanceof IPassengerCompartment) {
                    builder.addPassengerCompartment((IPassengerCompartment) component);
                }
            }
            
            // Add shields
            for (IRocketComponent component : components) {
                if (component instanceof IShield) {
                    builder.addShield((IShield) component);
                }
            }
            
            // Add life support systems
            for (IRocketComponent component : components) {
                if (component instanceof ILifeSupport) {
                    builder.addLifeSupport((ILifeSupport) component);
                }
            }
            
            // Build the rocket
            assembledRocket = builder.build();
            
            // Set the rocket status to ready for launch
            assembledRocket.setStatus(IRocket.RocketStatus.READY_FOR_LAUNCH);
            
            // Update status
            status = AssemblyStatus.COMPLETE;
            
            // Mark dirty to save changes
            this.setChanged();
            
            return true;
            
        } catch (Exception e) {
            // Assembly failed
            GalacticSpace.LOGGER.error("Rocket assembly failed", e);
            return false;
        }
    }
    
    /**
     * Gets the assembled rocket, if assembly is complete.
     * 
     * @return The assembled rocket, or null if assembly is not complete
     */
    public ModularRocket getAssembledRocket() {
        return assembledRocket;
    }
    
    /**
     * Takes the assembled rocket, setting the assembly table back to empty.
     * 
     * @param player The player taking the rocket
     * @return The assembled rocket, or null if assembly is not complete
     */
    public ModularRocket takeRocket(Player player) {
        if (status != AssemblyStatus.COMPLETE || assembledRocket == null) {
            return null;
        }
        
        ModularRocket rocket = assembledRocket;
        
        // Reset the assembly table
        components.clear();
        assembledRocket = null;
        status = AssemblyStatus.INCOMPLETE;
        
        // Mark dirty to save changes
        this.setChanged();
        
        return rocket;
    }
    
    /**
     * Validates the current assembly to check if it can form a valid rocket.
     */
    private void validateAssembly() {
        // Check if we have all required components
        boolean hasCommandModule = hasCommandModule();
        boolean hasEngine = hasEngine();
        boolean hasFuelTank = hasFuelTank();
        boolean hasLifeSupport = hasLifeSupport();
        boolean hasPassengerCompartment = hasPassengerCompartment();
        
        // Basic requirements: command module, engine, fuel tank
        boolean basicRequirements = hasCommandModule && hasEngine && hasFuelTank;
        
        // If we have passenger compartments, we need life support
        boolean lifeSupportRequirement = !hasPassengerCompartment || hasLifeSupport;
        
        // Update status
        if (basicRequirements && lifeSupportRequirement) {
            status = AssemblyStatus.VALID;
        } else {
            status = AssemblyStatus.INCOMPLETE;
        }
    }
    
    /**
     * Checks if the assembly has a command module.
     * 
     * @return true if a command module is present
     */
    private boolean hasCommandModule() {
        for (IRocketComponent component : components) {
            if (component instanceof ICommandModule) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the assembly has an engine.
     * 
     * @return true if an engine is present
     */
    private boolean hasEngine() {
        for (IRocketComponent component : components) {
            if (component instanceof IRocketEngine) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the assembly has a fuel tank.
     * 
     * @return true if a fuel tank is present
     */
    private boolean hasFuelTank() {
        for (IRocketComponent component : components) {
            if (component instanceof IFuelTank) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the assembly has a passenger compartment.
     * 
     * @return true if a passenger compartment is present
     */
    private boolean hasPassengerCompartment() {
        for (IRocketComponent component : components) {
            if (component instanceof IPassengerCompartment) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the assembly has a life support system.
     * 
     * @return true if a life support system is present
     */
    private boolean hasLifeSupport() {
        for (IRocketComponent component : components) {
            if (component instanceof ILifeSupport) {
                return true;
            }
        }
        return false;
    }
    
    // Override the BlockEntity's load method
    @Override
    protected void loadAdditional(CompoundTag tag, BlockEntity.LoadMode loadMode) {
        super.loadAdditional(tag, loadMode);
        
        // Load components
        components.clear();
        if (tag.contains("Components")) {
            CompoundTag componentsTag = tag.getCompound("Components").orElse(new CompoundTag());
            int count = componentsTag.getInt("Count").orElse(0);
            
            for (int i = 0; i < count; i++) {
                CompoundTag componentTag = componentsTag.getCompound("Component" + i).orElse(new CompoundTag());
                String componentTypeString = componentTag.getString("Type").orElse("");
                String idStr = componentTag.getString("ID").orElse("");
                
                if (!componentTypeString.isEmpty() && !idStr.isEmpty()) {
                    ResourceLocation id = ResourceLocation.parse(idStr);
                    
                    // Try to recreate the component from the saved data
                    IRocketComponent component = RocketComponentFactory.createComponentFromTag(id, componentTag);
                    if (component != null) {
                        components.add(component);
                    }
                }
            }
        }
        
        // Load assembled rocket
        if (tag.contains("AssembledRocket")) {
            CompoundTag rocketTag = tag.getCompound("AssembledRocket").orElse(new CompoundTag());
            String idStr = rocketTag.getString("ID").orElse("");
            
            if (!idStr.isEmpty()) {
                ResourceLocation id = ResourceLocation.parse(idStr);
                
                try {
                    // Recreate the rocket from the saved data with our new fromTag method
                    assembledRocket = ModularRocket.fromTag(rocketTag);
                    
                    // Set the status to complete if we loaded a rocket
                    if (assembledRocket != null) {
                        status = AssemblyStatus.COMPLETE;
                    } else {
                        status = AssemblyStatus.INCOMPLETE;
                        validateAssembly();
                    }
                } catch (Exception e) {
                    GalacticSpace.LOGGER.error("Failed to load assembled rocket", e);
                    assembledRocket = null;
                    status = AssemblyStatus.INCOMPLETE;
                    validateAssembly();
                }
            }
        } else {
            // No assembled rocket, validate current components
            assembledRocket = null;
            status = AssemblyStatus.INCOMPLETE;
            validateAssembly();
        }
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.world.level.block.entity.BlockEntity.Provider provider) {
        super.saveAdditional(tag, provider);
        
        // Save components
        CompoundTag componentsTag = new CompoundTag();
        componentsTag.putInt("Count", components.size());
        
        for (int i = 0; i < components.size(); i++) {
            IRocketComponent component = components.get(i);
            CompoundTag componentTag = new CompoundTag();
            
            // Save component type and ID
            componentTag.putString("Type", component.getType().name());
            componentTag.putString("ID", component.getId().toString());
            
            // Add serialization for component data to IRocketComponent interface
            // For now, we'll just save the basic properties
            componentTag.putInt("Tier", component.getTier());
            componentTag.putInt("Mass", component.getMass());
            
            componentsTag.put("Component" + i, componentTag);
        }
        
        tag.put("Components", componentsTag);
        
        // Save assembled rocket, if present
        if (assembledRocket != null) {
            CompoundTag rocketTag = new CompoundTag();
            // We need to implement the save method for ModularRocket
            // For now, just save the ID
            rocketTag.putString("ID", assembledRocket.getId().toString());
            tag.put("AssembledRocket", rocketTag);
        }
    }
    
    /**
     * Enum for the assembly status.
     */
    public enum AssemblyStatus {
        /**
         * Assembly is incomplete or invalid.
         */
        INCOMPLETE,
        
        /**
         * Assembly is valid and can be completed.
         */
        VALID,
        
        /**
         * Assembly is complete and the rocket is built.
         */
        COMPLETE
    }
}