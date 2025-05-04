package com.astroframe.galactic.energy.items;

import com.astroframe.galactic.energy.GalacticEnergy;
import com.astroframe.galactic.energy.registry.EnergyRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry for all items in the Energy module.
 * Items like energy crystals, specialized energy tools, etc.
 */
public class EnergyItems {

    // DeferredRegister for items
    private static final DeferredRegister<Item> ITEMS = EnergyRegistry.ITEMS;

    // Energy crystal item
    public static final Supplier<Item> ENERGY_CRYSTAL = ITEMS.register(
        "energy_crystal_item", 
        () -> new Item(EnergyRegistry.createStandardItemProperties())
    );
    
    // Energy stabilizer item
    public static final Supplier<Item> ENERGY_STABILIZER = ITEMS.register(
        "energy_stabilizer_item", 
        () -> new Item(EnergyRegistry.createStandardItemProperties())
    );
    
    // Energy lens item
    public static final Supplier<Item> ENERGY_LENS = ITEMS.register(
        "energy_lens_item", 
        () -> new Item(EnergyRegistry.createStandardItemProperties())
    );

    /**
     * Initialize item registry
     * @param eventBus Event bus
     */
    public static void init(IEventBus eventBus) {
        GalacticEnergy.LOGGER.info("Registering energy items (placeholders only)");
    }
}