package com.astroframe.galactic.machinery.registry;

import com.astroframe.galactic.core.registry.annotation.Register;
import com.astroframe.galactic.core.registry.tag.annotation.TaggedWith;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.machinery.api.Machine;
import com.astroframe.galactic.machinery.implementation.GeneratorMachine;
import com.astroframe.galactic.machinery.implementation.ProcessorMachine;

/**
 * Registry entries for the machinery module.
 * This class contains all the machinery-related objects that should be registered automatically.
 */
public class MachineRegistrations {
    
    /**
     * Create and register standard machines with annotation-based registration.
     * This allows for a declarative approach to machine registration.
     */
    public static void initialize() {
        MachineRegistry registry = MachineRegistry.getInstance();
        
        // Machines will be registered when the class is scanned for annotations
    }
    
    /**
     * Register a small electrical generator.
     * This demonstrates using annotations for registration.
     */
    @Register(registry = "machine", domain = "galacticexpansion_machinery", path = "basic_electrical_generator")
    @TaggedWith(typeKey = "machine_type", tagId = "generator")
    @TaggedWith(typeKey = "machine_capability", tagId = "energy_producer")
    public static final Machine BASIC_ELECTRICAL_GENERATOR = 
            MachineRegistry.getInstance().createGenerator(
                "basic_electrical_generator",
                "Basic Electrical Generator",
                EnergyType.ELECTRICAL,
                10000,  // capacity
                0.8f,   // efficiency
                100,    // processing time
                500,    // energy produced per cycle
                1000    // max fuel
            );
    
    /**
     * Register a small steam generator.
     */
    @Register(registry = "machine", domain = "galacticexpansion_machinery", path = "basic_steam_generator")
    @TaggedWith(typeKey = "machine_type", tagId = "generator")
    @TaggedWith(typeKey = "machine_capability", tagId = "energy_producer")
    public static final Machine BASIC_STEAM_GENERATOR = 
            MachineRegistry.getInstance().createGenerator(
                "basic_steam_generator",
                "Basic Steam Generator",
                EnergyType.STEAM,
                5000,   // capacity
                0.7f,   // efficiency
                80,     // processing time
                300,    // energy produced per cycle
                800     // max fuel
            );
    
    /**
     * Register a small electrical processor.
     */
    @Register(registry = "machine", domain = "galacticexpansion_machinery", path = "basic_electrical_processor")
    @TaggedWith(typeKey = "machine_type", tagId = "processor")
    @TaggedWith(typeKey = "machine_capability", tagId = "energy_consumer")
    @TaggedWith(typeKey = "machine_capability", tagId = "item_processor")
    public static final Machine BASIC_ELECTRICAL_PROCESSOR = 
            MachineRegistry.getInstance().createProcessor(
                "basic_electrical_processor",
                "Basic Electrical Processor",
                EnergyType.ELECTRICAL,
                5000,   // capacity
                0.8f,   // efficiency
                200,    // processing time
                25,     // energy per tick
                1,      // input slots
                1       // output slots
            );
    
    /**
     * Register a small steam processor.
     */
    @Register(registry = "machine", domain = "galacticexpansion_machinery", path = "basic_steam_processor")
    @TaggedWith(typeKey = "machine_type", tagId = "processor")
    @TaggedWith(typeKey = "machine_capability", tagId = "energy_consumer")
    @TaggedWith(typeKey = "machine_capability", tagId = "item_processor")
    public static final Machine BASIC_STEAM_PROCESSOR = 
            MachineRegistry.getInstance().createProcessor(
                "basic_steam_processor",
                "Basic Steam Processor",
                EnergyType.STEAM,
                3000,   // capacity
                0.6f,   // efficiency
                250,    // processing time
                20,     // energy per tick
                2,      // input slots
                1       // output slots
            );
}