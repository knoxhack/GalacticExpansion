package com.astroframe.galactic.energy.registry;

import com.astroframe.galactic.core.registry.Registry;
import com.astroframe.galactic.core.registry.RegistryEntry;
import com.astroframe.galactic.core.registry.RegistryManager;
import com.astroframe.galactic.energy.GalacticEnergy;
import com.astroframe.galactic.energy.api.EnergyNetwork;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.implementation.BaseEnergyNetwork;
import com.astroframe.galactic.energy.implementation.BaseEnergyStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Registry helper for the energy module.
 * This provides methods for registering and accessing energy-related objects.
 */
public class EnergyRegistry {
    
    private static final EnergyRegistry INSTANCE = new EnergyRegistry();
    
    private final Registry<EnergyStorage> storageRegistry;
    private final Registry<EnergyNetwork> networkRegistry;
    private final Map<EnergyType, EnergyNetwork> defaultNetworks = new HashMap<>();
    
    /**
     * Private constructor to enforce singleton pattern.
     */
    private EnergyRegistry() {
        RegistryManager manager = RegistryManager.getInstance();
        
        storageRegistry = manager.getRegistry("energy_storage")
                .orElseGet(() -> manager.createRegistry("energy_storage"));
        
        networkRegistry = manager.getRegistry("energy_network")
                .orElseGet(() -> manager.createRegistry("energy_network"));
        
        // Create default networks for each energy type
        for (EnergyType type : EnergyType.values()) {
            BaseEnergyNetwork network = new BaseEnergyNetwork(type);
            registerNetwork("default_" + type.getId(), network);
            defaultNetworks.put(type, network);
        }
    }
    
    /**
     * Get the singleton instance of the EnergyRegistry.
     * 
     * @return The EnergyRegistry instance
     */
    public static EnergyRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * Register an energy storage device.
     * 
     * @param id The identifier for the storage
     * @param storage The storage to register
     * @return The registry entry that was created
     */
    public RegistryEntry<EnergyStorage> registerStorage(String id, EnergyStorage storage) {
        return storageRegistry.register(GalacticEnergy.MOD_ID, id, storage);
    }
    
    /**
     * Create and register a basic energy storage device.
     * 
     * @param id The identifier for the storage
     * @param capacity The maximum amount of energy that can be stored
     * @param maxInput The maximum amount of energy that can be received per operation
     * @param maxOutput The maximum amount of energy that can be extracted per operation
     * @param type The type of energy this storage can handle
     * @return The registered storage
     */
    public EnergyStorage createStorage(String id, int capacity, int maxInput, int maxOutput, EnergyType type) {
        BaseEnergyStorage storage = new BaseEnergyStorage(capacity, maxInput, maxOutput, type);
        registerStorage(id, storage);
        return storage;
    }
    
    /**
     * Register an energy network.
     * 
     * @param id The identifier for the network
     * @param network The network to register
     * @return The registry entry that was created
     */
    public RegistryEntry<EnergyNetwork> registerNetwork(String id, EnergyNetwork network) {
        return networkRegistry.register(GalacticEnergy.MOD_ID, id, network);
    }
    
    /**
     * Create and register a basic energy network.
     * 
     * @param id The identifier for the network
     * @param type The type of energy this network handles
     * @return The registered network
     */
    public EnergyNetwork createNetwork(String id, EnergyType type) {
        BaseEnergyNetwork network = new BaseEnergyNetwork(type);
        registerNetwork(id, network);
        return network;
    }
    
    /**
     * Get an energy storage by its registry ID.
     * 
     * @param id The registry ID
     * @return An Optional containing the storage if found, or empty if not found
     */
    public Optional<EnergyStorage> getStorage(String id) {
        return storageRegistry.get(id);
    }
    
    /**
     * Get an energy network by its registry ID.
     * 
     * @param id The registry ID
     * @return An Optional containing the network if found, or empty if not found
     */
    public Optional<EnergyNetwork> getNetwork(String id) {
        return networkRegistry.get(id);
    }
    
    /**
     * Get the default network for a specific energy type.
     * 
     * @param type The energy type
     * @return The default network for that type
     */
    public EnergyNetwork getDefaultNetwork(EnergyType type) {
        return defaultNetworks.get(type);
    }
    
    /**
     * Process all registered networks for one tick.
     * This should be called once per game tick.
     */
    public void tickNetworks() {
        for (EnergyNetwork network : networkRegistry.getValues()) {
            network.tick();
        }
    }
}