package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.space.SpaceModule;
import com.astroframe.galactic.space.implementation.assembly.menu.RocketAssemblyMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all menus in the Space module.
 */
public class SpaceMenus {

    // Registry for menus - using Registries.MENU_TYPE directly in NeoForge 1.21.5
    public static final DeferredRegister<MenuType<?>> MENUS = 
            DeferredRegister.create(Registries.MENU, SpaceModule.MODID);
    
    // Menu type for rocket assembly
    public static final DeferredHolder<MenuType<?>, MenuType<RocketAssemblyMenu>> ROCKET_ASSEMBLY_MENU =
            MENUS.register("rocket_assembly", 
                    () -> IMenuTypeExtension.create(
                        (windowId, inv, data) -> new RocketAssemblyMenu(windowId, inv, data)));
    
    /**
     * Register the menu registry with the event bus.
     */
    public static void register() {
        // Registration happens automatically via DeferredRegister
        // We don't need to do anything extra here
    }
}