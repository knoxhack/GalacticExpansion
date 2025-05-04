package com.astroframe.galactic.robotics.registry;

import com.astroframe.galactic.robotics.GalacticRobotics;
import com.astroframe.galactic.robotics.blocks.DroneStationBlock;
import com.astroframe.galactic.robotics.blocks.RobotStationBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry class for all blocks in the Robotics module.
 */
public class RoboticsBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            BuiltInRegistries.BLOCK, GalacticRobotics.MOD_ID);

    // Register the robot station block
    public static final Supplier<RobotStationBlock> ROBOT_STATION = BLOCKS.register(
            "robot_station", RobotStationBlock::new);
    
    // Register the drone station block
    public static final Supplier<DroneStationBlock> DRONE_STATION = BLOCKS.register(
            "drone_station", DroneStationBlock::new);
    
    /**
     * Register all blocks with the mod event bus.
     *
     * @param eventBus The mod event bus
     */
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}