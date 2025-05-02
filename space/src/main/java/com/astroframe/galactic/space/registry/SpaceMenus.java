package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.core.registry.RegistryEntry;
import com.astroframe.galactic.core.registry.RegistryManager;
import com.astroframe.galactic.space.implementation.assembly.RocketAssemblyTableBlockEntity;
import com.astroframe.galactic.space.implementation.assembly.menu.RocketAssemblyMenu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * Registry for menu types in the Space module.
 */
public class SpaceMenus {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = RegistryManager.createDeferredRegister(
            ForgeRegistries.MENU_TYPES, "galactic");
    
    // Menu types
    public static final RegistryEntry<MenuType<RocketAssemblyMenu>> ROCKET_ASSEMBLY_MENU = 
            register("rocket_assembly", () -> createMenuType(
                    (id, inventory, data) -> new RocketAssemblyMenu(id, inventory, data)));
    
    /**
     * Registers this registry with the Registry Manager.
     */
    public static void register() {
        RegistryManager.register(MENU_TYPES);
    }
    
    /**
     * Registers a menu type.
     *
     * @param name The name of the menu type
     * @param supplier The supplier for the menu type
     * @param <T> The menu type
     * @return The registry entry
     */
    private static <T extends AbstractContainerMenu> RegistryEntry<MenuType<T>> register(
            String name, Supplier<MenuType<T>> supplier) {
        return new RegistryEntry<>(MENU_TYPES.register(name, supplier));
    }
    
    /**
     * Creates a menu type with the specified factory.
     *
     * @param factory The container factory
     * @param <T> The menu type
     * @return The menu type
     */
    private static <T extends AbstractContainerMenu> MenuType<T> createMenuType(IContainerFactory<T> factory) {
        return IMenuTypeExtension.create(factory);
    }
}