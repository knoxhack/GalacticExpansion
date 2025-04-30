package com.astroframe.galactic.example;

import com.astroframe.galactic.core.api.AbstractModuleIntegration;
import com.astroframe.galactic.core.registry.annotation.RegistryScanner;
import com.astroframe.galactic.energy.api.EnergyNetwork;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.registry.EnergyRegistry;
import com.astroframe.galactic.machinery.api.Machine;
import com.astroframe.galactic.machinery.api.MachineType;
import com.astroframe.galactic.machinery.implementation.GeneratorMachine;
import com.astroframe.galactic.machinery.implementation.ProcessorMachine;
import com.astroframe.galactic.machinery.registry.MachineRegistry;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Optional;

// Temporary mock classes for development without NeoForge dependencies
// These will be removed once we have the actual NeoForge dependencies
class FMLJavaModLoadingContext {
    public static FMLJavaModLoadingContext get() { return new FMLJavaModLoadingContext(); }
    public ModEventBus getModEventBus() { return new ModEventBus(); }
}
class ModEventBus {
    public <T> void addListener(Consumer<T> listener) {}
}
interface Consumer<T> {
    void accept(T t);
}

/**
 * Example mod that uses the Galactic Expansion framework.
 * This demonstrates how to use the core, energy, and machinery modules together.
 */
@Mod(GalacticExample.MOD_ID)
public class GalacticExample extends AbstractModuleIntegration {
    
    /**
     * The mod ID for the example module.
     */
    public static final String MOD_ID = "galacticexpansion_example";
    
    /**
     * Constructor for the example module.
     * Initializes the mod and sets up event listeners.
     */
    public GalacticExample() {
        super(MOD_ID, "Galactic Expansion Example");
        
        info("Initializing Galactic Expansion Example");
        
        // Register event listeners
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        
        info("Galactic Expansion Example initialized");
    }
    
    @Override
    protected void configureRegistryMappings(RegistryScanner scanner) {
        // No special registry mappings needed for the example
    }
    
    /**
     * Common setup event handler.
     * This is called during mod initialization.
     * 
     * @param event The common setup event
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        info("Running Galactic Expansion Example common setup");
        
        // Create a complete energy and machinery example system
        createExampleSystem();
        
        info("Galactic Expansion Example common setup complete");
    }
    
    /**
     * Create an example system that demonstrates the integration between energy and machinery.
     */
    private void createExampleSystem() {
        info("Creating example energy and machinery system");
        
        // Get registry instances
        EnergyRegistry energyRegistry = EnergyRegistry.getInstance();
        MachineRegistry machineRegistry = MachineRegistry.getInstance();
        
        // Create energy networks for different energy types
        EnergyNetwork electricalNetwork = energyRegistry.createNetwork("example_electrical_network", EnergyType.ELECTRICAL);
        EnergyNetwork steamNetwork = energyRegistry.createNetwork("example_steam_network", EnergyType.STEAM);
        
        // Create energy storage devices
        EnergyStorage electricalStorage = energyRegistry.createStorage(
                "example_electrical_storage", 20000, 1000, 1000, EnergyType.ELECTRICAL);
        
        EnergyStorage steamStorage = energyRegistry.createStorage(
                "example_steam_storage", 15000, 800, 800, EnergyType.STEAM);
        
        // Add storage to networks
        electricalNetwork.addStorage(electricalStorage);
        steamNetwork.addStorage(steamStorage);
        
        // Create custom machines specific to this example
        GeneratorMachine advancedGenerator = machineRegistry.createGenerator(
                "example_advanced_generator",
                "Advanced Electrical Generator",
                EnergyType.ELECTRICAL,
                15000,  // capacity
                0.9f,   // efficiency
                80,     // processing time
                700,    // energy produced per cycle
                1500    // max fuel
        );
        
        ProcessorMachine advancedProcessor = machineRegistry.createProcessor(
                "example_advanced_processor",
                "Advanced Electrical Processor",
                EnergyType.ELECTRICAL,
                10000,  // capacity
                0.85f,  // efficiency
                150,    // processing time
                30,     // energy per tick
                3,      // input slots
                2       // output slots
        );
        
        // Tag these machines
        addToTag("machine_type", "generator", advancedGenerator);
        addToTag("machine_type", "processor", advancedProcessor);
        addToTag("machine_capability", "energy_producer", advancedGenerator);
        addToTag("machine_capability", "energy_consumer", advancedProcessor);
        addToTag("machine_capability", "item_processor", advancedProcessor);
        
        // Simulate a complete energy system
        simulateSystemOperation();
    }
    
    /**
     * Simulate the operation of the energy and machinery system.
     * This demonstrates how all components work together.
     */
    private void simulateSystemOperation() {
        info("Simulating system operation");
        
        // Get registry instances
        EnergyRegistry energyRegistry = EnergyRegistry.getInstance();
        MachineRegistry machineRegistry = MachineRegistry.getInstance();
        
        // Get an example generator
        Optional<Machine> generator = machineRegistry.getMachine("example_advanced_generator");
        
        if (generator.isPresent() && generator.get() instanceof GeneratorMachine) {
            GeneratorMachine genMachine = (GeneratorMachine) generator.get();
            
            // Add fuel to the generator
            int fuelAdded = genMachine.addFuel(100);
            info("Added {} units of fuel to generator", fuelAdded);
            
            // Start the generator (will start automatically in tick() if it has fuel)
            genMachine.start();
            info("Started generator");
            
            // Simulate ticks to generate energy
            for (int i = 0; i < 10; i++) {
                genMachine.tick();
            }
            
            // Check energy storage
            genMachine.getEnergyStorage().ifPresent(storage -> {
                info("Generator energy storage: {}/{} ({} %)",
                    storage.getEnergy(),
                    storage.getMaxEnergy(),
                    Math.round(storage.getFillLevel() * 100));
            });
        }
        
        // Get an example processor
        Optional<Machine> processor = machineRegistry.getMachine("example_advanced_processor");
        
        if (processor.isPresent() && processor.get() instanceof ProcessorMachine) {
            ProcessorMachine procMachine = (ProcessorMachine) processor.get();
            
            // Set up a simple recipe validator and processor
            procMachine.setRecipeValidator(() -> {
                // In a real implementation, this would check if inputs are valid
                return true;
            });
            
            procMachine.setRecipeProcessor(() -> {
                // In a real implementation, this would transform inputs to outputs
                info("Processor completed a recipe");
            });
            
            // Transfer energy from generator to processor if needed
            Optional<Machine> gen = machineRegistry.getMachine("example_advanced_generator");
            
            if (gen.isPresent() && gen.get() instanceof GeneratorMachine) {
                GeneratorMachine genMachine = (GeneratorMachine) gen.get();
                
                genMachine.getEnergyStorage().ifPresent(genStorage -> {
                    procMachine.getEnergyStorage().ifPresent(procStorage -> {
                        if (genStorage.getEnergy() > 0 && procStorage.getEnergy() < procStorage.getMaxEnergy()) {
                            int energyToTransfer = Math.min(1000, genStorage.getEnergy());
                            energyToTransfer = Math.min(energyToTransfer, procStorage.getMaxEnergy() - procStorage.getEnergy());
                            
                            int extracted = genStorage.extractEnergy(energyToTransfer, false);
                            int received = procStorage.receiveEnergy(extracted, false);
                            
                            info("Transferred {} energy from generator to processor", received);
                        }
                    });
                });
            }
            
            // Start the processor
            procMachine.start();
            info("Started processor");
            
            // Simulate ticks to process items
            for (int i = 0; i < 10; i++) {
                procMachine.tick();
            }
            
            // Check progress
            info("Processor progress: {} %", Math.round(procMachine.getProgress() * 100));
            
            // Check energy storage
            procMachine.getEnergyStorage().ifPresent(storage -> {
                info("Processor energy storage: {}/{} ({} %)",
                    storage.getEnergy(),
                    storage.getMaxEnergy(),
                    Math.round(storage.getFillLevel() * 100));
            });
        }
        
        // Demonstrate finding machines by type using the tag system
        info("Finding all generators using tags:");
        getTagManager().getTag("machine_type", "generator").ifPresent(tag -> {
            tag.getValues().forEach(machine -> {
                if (machine instanceof Machine) {
                    info("  Found generator: {}", ((Machine) machine).getName());
                }
            });
        });
        
        info("System simulation complete");
    }
}