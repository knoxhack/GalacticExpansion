package com.astroframe.galactic.weaponry.registry;

import com.astroframe.galactic.weaponry.GalacticWeaponry;
import com.astroframe.galactic.weaponry.blocks.ArmoryBlock;
import com.astroframe.galactic.weaponry.blocks.WeaponStationBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry class for all blocks in the Weaponry module.
 */
public class WeaponryBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            BuiltInRegistries.BLOCK, GalacticWeaponry.MOD_ID);

    // Register the weapon station block
    public static final Supplier<WeaponStationBlock> WEAPON_STATION = BLOCKS.register(
            "weapon_station", WeaponStationBlock::new);
    
    // Register the armory block
    public static final Supplier<ArmoryBlock> ARMORY = BLOCKS.register(
            "armory", ArmoryBlock::new);
    
    /**
     * Register all blocks with the mod event bus.
     *
     * @param eventBus The mod event bus
     */
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}