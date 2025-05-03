package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.implementation.assembly.menu.RocketAssemblyMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry for all menu types in the Space module
 */
public class SpaceMenus {
    
    // Create a deferred register for menu types
    public static final DeferredRegister<MenuType<?>> MENUS = 
            DeferredRegister.create(Registries.MENU, GalacticSpace.MOD_ID);
    
    // Register the rocket assembly menu
    public static final Supplier<MenuType<RocketAssemblyMenu>> ROCKET_ASSEMBLY = 
            MENUS.register("rocket_assembly", 
                    () -> IMenuTypeExtension.create(RocketAssemblyMenu::new));
    
    /**
     * Register the menu registry with the event bus.
     *
     * @param eventBus The event bus to register with
     */
    public static void initialize(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}