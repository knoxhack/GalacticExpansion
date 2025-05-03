package com.astroframe.galactic.biotech.registry;

import com.astroframe.galactic.biotech.GalacticBiotech;
import com.astroframe.galactic.biotech.items.BiotechItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registers creative mode tabs for the Biotech module.
 */
public class BiotechCreativeTabs {
    
    // Create a deferred register for creative mode tabs
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = 
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GalacticBiotech.MOD_ID);
    
    // Define the biotech creative tab
    public static final Supplier<CreativeModeTab> BIOTECH_TAB = CREATIVE_TABS.register("biotech_tab", 
            () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + GalacticBiotech.MOD_ID + ".biotech_tab"))
                .icon(() -> BiotechItems.GENE_SPLICER.get().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    // Add Biotech items to the tab
                    output.accept(BiotechItems.GENE_SPLICER.get());
                    output.accept(BiotechItems.DNA_SAMPLER.get());
                    output.accept(BiotechItems.GROWTH_SERUM.get());
                    output.accept(BiotechItems.MUTATION_CATALYST.get());
                    output.accept(BiotechItems.GENETIC_STABILIZER.get());
                })
                .build()
    );
    
    /**
     * Initialize and register the creative tabs with the event bus.
     * @param modEventBus The mod event bus to register with
     */
    public static void initialize(IEventBus modEventBus) {
        CREATIVE_TABS.register(modEventBus);
    }
}