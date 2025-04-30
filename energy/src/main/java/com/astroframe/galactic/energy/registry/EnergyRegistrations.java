package com.astroframe.galactic.energy.registry;

import com.astroframe.galactic.core.registry.annotation.Register;
import com.astroframe.galactic.core.registry.tag.annotation.TaggedWith;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.implementation.BaseEnergyStorage;

/**
 * Registry entries for the energy module.
 * This class contains all the energy-related objects that should be registered automatically.
 */
public class EnergyRegistrations {
    
    /**
     * Register a small electrical energy storage device.
     * This demonstrates using annotations for registration.
     */
    @Register(registry = "energy_storage", domain = "galacticexpansion_energy", path = "small_electrical_storage")
    @TaggedWith(typeKey = "energy_component", tagId = "storage")
    @TaggedWith(typeKey = "energy_type", tagId = "electrical")
    public static final EnergyStorage SMALL_ELECTRICAL_STORAGE =
            new BaseEnergyStorage(1000, 100, 100, EnergyType.ELECTRICAL);
    
    /**
     * Register a medium electrical energy storage device.
     */
    @Register(registry = "energy_storage", domain = "galacticexpansion_energy", path = "medium_electrical_storage")
    @TaggedWith(typeKey = "energy_component", tagId = "storage")
    @TaggedWith(typeKey = "energy_type", tagId = "electrical")
    public static final EnergyStorage MEDIUM_ELECTRICAL_STORAGE =
            new BaseEnergyStorage(5000, 250, 250, EnergyType.ELECTRICAL);
    
    /**
     * Register a large electrical energy storage device.
     */
    @Register(registry = "energy_storage", domain = "galacticexpansion_energy", path = "large_electrical_storage")
    @TaggedWith(typeKey = "energy_component", tagId = "storage")
    @TaggedWith(typeKey = "energy_type", tagId = "electrical")
    public static final EnergyStorage LARGE_ELECTRICAL_STORAGE =
            new BaseEnergyStorage(10000, 500, 500, EnergyType.ELECTRICAL);
    
    /**
     * Register a small steam energy storage device.
     */
    @Register(registry = "energy_storage", domain = "galacticexpansion_energy", path = "small_steam_storage")
    @TaggedWith(typeKey = "energy_component", tagId = "storage")
    @TaggedWith(typeKey = "energy_type", tagId = "steam")
    public static final EnergyStorage SMALL_STEAM_STORAGE =
            new BaseEnergyStorage(2000, 150, 150, EnergyType.STEAM);
    
    /**
     * Register a large steam energy storage device.
     */
    @Register(registry = "energy_storage", domain = "galacticexpansion_energy", path = "large_steam_storage")
    @TaggedWith(typeKey = "energy_component", tagId = "storage")
    @TaggedWith(typeKey = "energy_type", tagId = "steam")
    public static final EnergyStorage LARGE_STEAM_STORAGE =
            new BaseEnergyStorage(8000, 400, 400, EnergyType.STEAM);
}