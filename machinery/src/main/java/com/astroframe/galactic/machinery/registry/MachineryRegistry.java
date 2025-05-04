package com.astroframe.galactic.machinery.registry;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import com.astroframe.galactic.machinery.blockentity.MachineryBlockEntities;
import com.astroframe.galactic.machinery.items.MachineryItemBlocks;
import com.astroframe.galactic.machinery.items.MachineryItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry handler for the Machinery module.
 * This centralizes all registrations for the module.
 */
public class MachineryRegistry {

    // Deferred Registers
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, GalacticMachinery.MOD_ID);

    /**
     * Registers all registry objects with the given event bus.
     *
     * @param eventBus The mod event bus to register with
     */
    public static void register(IEventBus eventBus) {
        GalacticMachinery.LOGGER.info("Registering Machinery module objects");
        
        try {
            // Register in the correct order to prevent dependency issues
            // 1. First register blocks
            GalacticMachinery.LOGGER.info("Step 1: Registering machinery blocks");
            MachineryBlocks.init(eventBus);
            
            // 2. Then register items
            GalacticMachinery.LOGGER.info("Step 2: Registering machinery items");
            MachineryItems.init();
            ITEMS.register(eventBus);
            
            // 3. Wait until blocks are registered before creating block items
            GalacticMachinery.LOGGER.info("Step 3: Registering machinery block items");
            MachineryItemBlocks.init();
            
            // 4. Finally register block entities (which depend on blocks)
            GalacticMachinery.LOGGER.info("Step 4: Registering machinery block entities");
            MachineryBlockEntities.init(eventBus);
            
            GalacticMachinery.LOGGER.info("Machinery module registration complete");
        } catch (Exception e) {
            GalacticMachinery.LOGGER.error("Error during machinery registration", e);
            throw e;
        }
    }
}