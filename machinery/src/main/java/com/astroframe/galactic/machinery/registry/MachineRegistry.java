package com.astroframe.galactic.machinery.registry;

import com.astroframe.galactic.core.registry.Registry;
import com.astroframe.galactic.core.registry.RegistryEntry;
import com.astroframe.galactic.core.registry.RegistryManager;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.registry.EnergyRegistry;
import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.api.Machine;
import com.astroframe.galactic.machinery.api.MachineType;
import com.astroframe.galactic.machinery.implementation.GeneratorMachine;
import com.astroframe.galactic.machinery.implementation.ProcessorMachine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Registry for machines in the Galactic Expansion mod.
 * This provides methods for registering and accessing machines.
 */
public class MachineRegistry {
    
    private static final MachineRegistry INSTANCE = new MachineRegistry();
    
    private final Registry<Machine> machineRegistry;
    private final Map<MachineType, Registry<Machine>> machineTypeRegistries = new HashMap<>();
    
    /**
     * Private constructor to enforce singleton pattern.
     */
    private MachineRegistry() {
        RegistryManager manager = RegistryManager.getInstance();
        
        // Get or create the main machine registry
        machineRegistry = manager.getRegistry("machine")
                .map(registry -> {
                    @SuppressWarnings("unchecked")
                    Registry<Machine> r = (Registry<Machine>) registry;
                    return r;
                })
                .orElseGet(() -> {
                    @SuppressWarnings("unchecked")
                    Registry<Machine> r = (Registry<Machine>) (Registry<?>) manager.createRegistry("machine");
                    return r;
                });
        
        // Create registries for each machine type
        for (MachineType type : MachineType.values()) {
            String registryName = "machine_" + type.getId();
            Registry<Machine> typeRegistry = manager.getRegistry(registryName)
                    .map(registry -> {
                        @SuppressWarnings("unchecked")
                        Registry<Machine> r = (Registry<Machine>) registry;
                        return r;
                    })
                    .orElseGet(() -> {
                        @SuppressWarnings("unchecked")
                        Registry<Machine> r = (Registry<Machine>) (Registry<?>) manager.createRegistry(registryName);
                        return r;
                    });
            machineTypeRegistries.put(type, typeRegistry);
        }
    }
    
    /**
     * Get the singleton instance of the MachineRegistry.
     * 
     * @return The MachineRegistry instance
     */
    public static MachineRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * Register a machine.
     * 
     * @param id The identifier for the machine
     * @param machine The machine to register
     * @return The registry entry that was created
     */
    public RegistryEntry<Machine> registerMachine(String id, Machine machine) {
        // Register in the main registry
        RegistryEntry<Machine> entry = machineRegistry.register(GalacticMachinery.MOD_ID, id, machine);
        
        // Also register in the type-specific registry if we know the type
        if (machine instanceof com.astroframe.galactic.machinery.implementation.BaseMachine) {
            MachineType type = ((com.astroframe.galactic.machinery.implementation.BaseMachine) machine).getType();
            Registry<Machine> typeRegistry = machineTypeRegistries.get(type);
            
            if (typeRegistry != null) {
                typeRegistry.register(GalacticMachinery.MOD_ID, id, machine);
            }
        }
        
        return entry;
    }
    
    /**
     * Create and register a generator machine.
     * 
     * @param id The identifier for the generator
     * @param name The name of the generator
     * @param energyType The type of energy this generator produces
     * @param capacity The energy storage capacity
     * @param efficiency The efficiency of this generator (0.0 to 1.0)
     * @param processingTime The base time (in ticks) it takes to complete one generation cycle
     * @param energyProduced The amount of energy produced per cycle
     * @param maxFuel The maximum amount of fuel this generator can hold
     * @return The registered generator machine
     */
    public GeneratorMachine createGenerator(String id, String name, EnergyType energyType,
                                         int capacity, float efficiency, int processingTime,
                                         int energyProduced, int maxFuel) {
        // Create an energy storage for the generator
        EnergyStorage storage = EnergyRegistry.getInstance()
                .createStorage(id + "_storage", capacity, capacity, 0, energyType);
        
        // Create the generator
        GeneratorMachine generator = new GeneratorMachine(
                name, storage, efficiency, processingTime, energyProduced, maxFuel);
        
        // Register the generator
        registerMachine(id, generator);
        
        return generator;
    }
    
    /**
     * Create and register a processor machine.
     * 
     * @param id The identifier for the processor
     * @param name The name of the processor
     * @param energyType The type of energy this processor consumes
     * @param capacity The energy storage capacity
     * @param efficiency The efficiency of this processor (0.0 to 1.0)
     * @param processingTime The base time (in ticks) it takes to complete one processing cycle
     * @param energyPerTick The amount of energy consumed per tick while active
     * @param inputSlots The number of input slots
     * @param outputSlots The number of output slots
     * @return The registered processor machine
     */
    public ProcessorMachine createProcessor(String id, String name, EnergyType energyType,
                                         int capacity, float efficiency, int processingTime,
                                         int energyPerTick, int inputSlots, int outputSlots) {
        // Create an energy storage for the processor
        EnergyStorage storage = EnergyRegistry.getInstance()
                .createStorage(id + "_storage", capacity, capacity, capacity, energyType);
        
        // Create the processor
        ProcessorMachine processor = new ProcessorMachine(
                name, storage, efficiency, processingTime, energyPerTick, inputSlots, outputSlots);
        
        // Register the processor
        registerMachine(id, processor);
        
        return processor;
    }
    
    /**
     * Get a machine by its registry ID.
     * 
     * @param id The registry ID
     * @return An Optional containing the machine if found, or empty if not found
     */
    public Optional<Machine> getMachine(String id) {
        return machineRegistry.get(id);
    }
    
    /**
     * Get all machines of a specific type.
     * 
     * @param type The machine type
     * @return A collection of machines of the specified type
     */
    public Collection<Machine> getMachinesByType(MachineType type) {
        Registry<Machine> typeRegistry = machineTypeRegistries.get(type);
        
        if (typeRegistry == null) {
            return java.util.Collections.emptyList();
        }
        
        return typeRegistry.getValues();
    }
    
    /**
     * Get all registered machines.
     * 
     * @return A collection of all machines
     */
    public Collection<Machine> getAllMachines() {
        return machineRegistry.getValues();
    }
    
    /**
     * Process all registered machines for one tick.
     * This should be called once per game tick.
     */
    public void tickMachines() {
        for (Machine machine : machineRegistry.getValues()) {
            machine.tick();
        }
    }
}