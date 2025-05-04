package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry for menu types in the Space module.
 * Currently placeholder with no actual menu registrations to avoid dependency issues.
 */
public class SpaceMenus {
    
    // Create a deferred register for menus
    public static final DeferredRegister<MenuType<?>> MENUS = 
            DeferredRegister.create(Registries.MENU, GalacticSpace.MOD_ID);
    
    // Placeholder for ROCKET_ASSEMBLY menu type
    // Not actually registered to avoid dependency issues
    public static final Supplier<MenuType<?>> ROCKET_ASSEMBLY = null;
    
    /**
     * Register the menu registry with the event bus.
     *
     * @param eventBus The event bus to register with
     */
    public static void initialize(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}