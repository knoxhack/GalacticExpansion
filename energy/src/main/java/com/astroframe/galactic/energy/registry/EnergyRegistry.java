package com.astroframe.galactic.energy.registry;

import com.astroframe.galactic.energy.GalacticEnergy;
import com.astroframe.galactic.energy.api.EnergyNetwork;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.api.energynetwork.Level;
import com.astroframe.galactic.energy.implementation.BaseEnergyStorage;
import com.astroframe.galactic.energy.implementation.CachedEnergyNetwork;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Registry helper for the energy module.
 * This provides methods for registering and accessing energy-related objects.
 */
public class EnergyRegistry {
    
    private static final EnergyRegistry INSTANCE = new EnergyRegistry();
    
    private final Map<String, EnergyStorage> storages = new HashMap<>();
    private final Map<String, EnergyNetwork> networks = new HashMap<>();
    private final Map<EnergyType, EnergyNetwork> defaultNetworks = new HashMap<>();
    
    /**
     * Private constructor to enforce singleton pattern.
     */
    private EnergyRegistry() {
        // Initialize registry
        GalacticEnergy.LOGGER.info("Initializing Energy Registry");
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
     * @return The registered storage
     */
    public EnergyStorage registerStorage(String id, EnergyStorage storage) {
        String fullId = GalacticEnergy.MOD_ID + ":" + id;
        storages.put(fullId, storage);
        return storage;
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
        return registerStorage(id, storage);
    }
    
    /**
     * Register an energy network.
     * 
     * @param id The identifier for the network
     * @param network The network to register
     * @return The registered network
     */
    public EnergyNetwork registerNetwork(String id, EnergyNetwork network) {
        String fullId = GalacticEnergy.MOD_ID + ":" + id;
        networks.put(fullId, network);
        return network;
    }
    
    /**
     * Create and register a basic energy network.
     * 
     * @param id The identifier for the network
     * @param level The level (dimension) this network exists in
     * @param type The type of energy this network handles
     * @return The registered network
     */
    public EnergyNetwork createNetwork(String id, Level level, EnergyType type) {
        // Create a cached energy network
        EnergyNetwork network = new CachedEnergyNetwork(type, level);
        return registerNetwork(id, network);
    }
    
    /**
     * Get an energy storage by its registry ID.
     * 
     * @param id The registry ID
     * @return An Optional containing the storage if found, or empty if not found
     */
    public Optional<EnergyStorage> getStorage(String id) {
        String fullId = id.contains(":") ? id : GalacticEnergy.MOD_ID + ":" + id;
        return Optional.ofNullable(storages.get(fullId));
    }
    
    /**
     * Get an energy network by its registry ID.
     * 
     * @param id The registry ID
     * @return An Optional containing the network if found, or empty if not found
     */
    public Optional<EnergyNetwork> getNetwork(String id) {
        String fullId = id.contains(":") ? id : GalacticEnergy.MOD_ID + ":" + id;
        return Optional.ofNullable(networks.get(fullId));
    }
    
    /**
     * Get the default network for a specific energy type in a specific level.
     * If a default network does not exist for this type in this level, one will be created.
     * 
     * @param type The energy type
     * @param level The level (dimension) for this network
     * @return The default network for that type
     */
    public EnergyNetwork getDefaultNetwork(EnergyType type, Level level) {
        EnergyNetwork network = defaultNetworks.get(type);
        if (network == null) {
            String id = "default_" + type.getId();
            // Create a cached energy network
            EnergyNetwork newNetwork = new CachedEnergyNetwork(type, level);
            registerNetwork(id, newNetwork);
            defaultNetworks.put(type, newNetwork);
            return newNetwork;
        }
        return network;
    }
    
    /**
     * Process all registered networks for one tick.
     * This should be called once per game tick.
     */
    public void tickNetworks() {
        for (EnergyNetwork network : networks.values()) {
            network.tick();
        }
    }
    
    /**
     * Get all registered energy storages.
     * 
     * @return Collection of all registered energy storages
     */
    public Collection<EnergyStorage> getAllStorages() {
        return storages.values();
    }
    
    /**
     * Get all registered energy networks.
     * 
     * @return Collection of all registered energy networks
     */
    public Collection<EnergyNetwork> getAllNetworks() {
        return networks.values();
    }
}