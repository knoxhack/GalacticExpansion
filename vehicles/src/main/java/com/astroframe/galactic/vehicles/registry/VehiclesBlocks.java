package com.astroframe.galactic.vehicles.registry;

import com.astroframe.galactic.vehicles.GalacticVehicles;
import com.astroframe.galactic.vehicles.blocks.RoverBlock;
import com.astroframe.galactic.vehicles.blocks.SimpleVehicleBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry class for all blocks in the Vehicles module.
 */
public class VehiclesBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            BuiltInRegistries.BLOCK, GalacticVehicles.MOD_ID);

    // Register the simple vehicle block
    public static final Supplier<SimpleVehicleBlock> SIMPLE_VEHICLE_BLOCK = BLOCKS.register(
            "simple_vehicle_block", SimpleVehicleBlock::new);
    
    // Register the rover block
    public static final Supplier<RoverBlock> ROVER = BLOCKS.register(
            "rover", RoverBlock::new);
    
    /**
     * Register all blocks with the mod event bus.
     *
     * @param eventBus The mod event bus
     */
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}