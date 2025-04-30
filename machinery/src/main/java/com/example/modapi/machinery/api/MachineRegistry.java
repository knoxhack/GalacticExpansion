package com.example.modapi.machinery.api;

import com.example.modapi.core.api.ModRegistry;
import com.example.modapi.machinery.ModApiMachinery;
import com.example.modapi.machinery.common.ProcessingMachine;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Registry for machine types and related objects.
 * Manages registration of machine blocks, block entities, and recipes.
 */
public class MachineRegistry {
    private final Map<String, Supplier<? extends Machine>> machineTypes = new HashMap<>();
    private final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES;
    
    /**
     * Constructor for MachineRegistry.
     */
    public MachineRegistry() {
        BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ModApiMachinery.MOD_ID);
    }
    
    /**
     * Registers all content with the mod registry.
     * 
     * @param registry The mod registry
     */
    public void registerAllContent(ModRegistry registry) {
        registry.addRegister(BLOCK_ENTITY_TYPES);
    }
    
    /**
     * Registers all machine types.
     * Called during the setup phase.
     */
    public void registerMachineTypes() {
        // Register basic machine types
        registerMachineType("basic_processor", () -> new ProcessingMachine.Basic());
        registerMachineType("advanced_processor", () -> new ProcessingMachine.Advanced());
        
        ModApiMachinery.LOGGER.info("Registered " + machineTypes.size() + " machine types");
    }
    
    /**
     * Registers a machine type.
     * 
     * @param id The machine type ID
     * @param supplier The machine supplier
     */
    public void registerMachineType(String id, Supplier<? extends Machine> supplier) {
        machineTypes.put(id, supplier);
    }
    
    /**
     * Gets a machine type by ID.
     * 
     * @param id The machine type ID
     * @return The machine supplier
     */
    public Supplier<? extends Machine> getMachineType(String id) {
        return machineTypes.get(id);
    }
    
    /**
     * Gets the block entity types registry.
     * 
     * @return The block entity types registry
     */
    public DeferredRegister<BlockEntityType<?>> getBlockEntityTypes() {
        return BLOCK_ENTITY_TYPES;
    }
    
    /**
     * Registers a block entity type.
     * 
     * @param name The block entity type name
     * @param supplier The block entity type supplier
     * @param <T> The block entity type
     * @return A supplier for the registered block entity type
     */
    public <T extends BlockEntityType<?>> Supplier<T> registerBlockEntityType(String name, Supplier<T> supplier) {
        return BLOCK_ENTITY_TYPES.register(name, supplier);
    }
}
