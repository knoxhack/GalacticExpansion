package com.astroframe.galactic.energy.registry;

import com.astroframe.galactic.core.registry.Registry;
import com.astroframe.galactic.core.registry.RegistryEntry;
import com.astroframe.galactic.core.registry.RegistryManager;
import com.astroframe.galactic.energy.GalacticEnergy;
import com.astroframe.galactic.energy.api.EnergyNetwork;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.implementation.BaseEnergyStorage;
import com.astroframe.galactic.energy.implementation.CachedEnergyNetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

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
        
        storageRegistry = manager.<EnergyStorage>getRegistry("energy_storage")
                .orElseGet(() -> manager.<EnergyStorage>createRegistry("energy_storage"));
        
        networkRegistry = manager.<EnergyNetwork>getRegistry("energy_network")
                .orElseGet(() -> manager.<EnergyNetwork>createRegistry("energy_network"));
        
        // Networks will be created when accessed with a valid Level reference
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
     * @param level The level (dimension) this network exists in
     * @param type The type of energy this network handles
     * @return The registered network
     */
    public EnergyNetwork createNetwork(String id, Level level, EnergyType type) {
        ResourceLocation networkId = new ResourceLocation(GalacticEnergy.MOD_ID, id);
        // Create a cached energy network
        EnergyNetwork network = new CachedEnergyNetwork(type, level);
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
            ResourceLocation networkId = new ResourceLocation(GalacticEnergy.MOD_ID, id);
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
        for (EnergyNetwork network : networkRegistry.getValues()) {
            network.tick();
        }
    }
}