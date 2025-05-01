package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registers the creative tabs for the Space module.
 */
public class SpaceCreativeTabs {
    
    // Create a deferred register for creative mode tabs
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = 
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GalacticSpace.MOD_ID);
    
    // Define the main creative tab for space items
    public static final Supplier<CreativeModeTab> SPACE_TAB = CREATIVE_TABS.register("space_tab", 
            () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + GalacticSpace.MOD_ID + ".space_tab"))
                .icon(() -> SpaceItems.TIER_1_ROCKET.get().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    // Add all space items to the tab
                    output.accept(SpaceItems.ROCKET_PART.get());
                    output.accept(SpaceItems.MOON_DUST.get());
                    output.accept(SpaceItems.MARS_ROCK.get());
                    output.accept(SpaceItems.SPACE_HELMET.get());
                    output.accept(SpaceItems.OXYGEN_TANK.get());
                    output.accept(SpaceItems.TIER_1_ROCKET.get());
                    output.accept(SpaceItems.TIER_2_ROCKET.get());
                    output.accept(SpaceItems.TIER_3_ROCKET.get());
                })
                .build()
    );
    
    /**
     * Initialize and register all creative tabs.
     * @param modEventBus The mod event bus to register tabs on
     */
    public static void initialize(IEventBus modEventBus) {
        CREATIVE_TABS.register(modEventBus);
    }
}