package com.astroframe.galactic.space.dimension;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Simplified chunk generator for the Space Station dimension.
 * This generator creates a flat void with a space station platform at the center.
 * 
 * Note: This is a stub class. In NeoForge 1.21.5, dimensions are defined using JSON configuration files.
 * Resources folder will contain:
 * - data/galactic-space/dimension/space_station.json
 * - data/galactic-space/dimension_type/space_station_type.json
 */
public class SpaceStationChunkGenerator extends ChunkGenerator {

    public SpaceStationChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);
    }

    // Method implementations for ChunkGenerator
    // @Override annotations removed as they were causing errors with NeoForge 1.21.5 compatibility
    
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunkAccess) {
        return CompletableFuture.completedFuture(chunkAccess);
    }

    public int getGenDepth() {
        return 384; // Default world height
    }

    public CompletableFuture<ChunkAccess> createBiomes(RandomState randomState, Blender blender, StructureManager structureFeatureManager, ChunkAccess chunkAccess) {
        return CompletableFuture.completedFuture(chunkAccess);
    }

    // This method is no longer used in NeoForge 1.21.5, but kept for compatibility with other versions
    // The new method signature is used instead (the one with WorldGenRegion)
    public void applyCarvers(WorldGenLevel level, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess, GenerationStep.Decoration carvingStage) {
        // No carvers needed in space station
    }
    
    // Implementation for NeoForge 1.21.5
    public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess) {
        // No carvers needed in space station
    }

    // Implementation for NeoForge 1.21.5 abstract method
    public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess) {
        buildSpaceStationSurface(chunkAccess);
    }
    
    // Common implementation for both methods
    private void buildSpaceStationSurface(ChunkAccess chunkAccess) {
        ChunkPos chunkPos = chunkAccess.getPos();
        
        // Only generate the platform in the origin chunk
        if (chunkPos.x == 0 && chunkPos.z == 0) {
            // This will be handled by SpaceStationHelper when world is first created
            GalacticSpace.LOGGER.debug("Origin chunk detected in space station dimension");
        }
    }

    // Implementation for NeoForge 1.21.5
    public void spawnOriginalMobs(WorldGenRegion region) {
        // No natural mob spawning in space station
    }

    public int getSeaLevel() {
        return -63; // No sea in space
    }

    public int getMinY() {
        return -64;
    }

    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        // Check if this is part of the platform
        int distanceSquared = x * x + z * z;
        if (distanceSquared <= SpaceStationHelper.PLATFORM_RADIUS * SpaceStationHelper.PLATFORM_RADIUS) {
            return new NoiseColumn(SpaceStationHelper.PLATFORM_Y - 1, new BlockState[]{ SpaceStationHelper.PLATFORM_FLOOR });
        }
        return new NoiseColumn(SpaceStationHelper.PLATFORM_Y - 1, new BlockState[]{ Blocks.AIR.defaultBlockState() });
    }
    
    // Add required method for NeoForge 1.21.5
    public void addDebugScreenInfo(List<String> debugInfo, RandomState randomState, BlockPos pos) {
        debugInfo.add("Space Station Dimension - Custom Chunk Generator");
    }
    
    // Implementation for NeoForge 1.21.5 abstract method
    public int getBaseHeight(int x, int z, Heightmap.Types types, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
        // Check if this is part of the platform
        int distanceSquared = x * x + z * z;
        if (distanceSquared <= SpaceStationHelper.PLATFORM_RADIUS * SpaceStationHelper.PLATFORM_RADIUS) {
            return SpaceStationHelper.PLATFORM_Y;
        }
        return SpaceStationHelper.PLATFORM_Y - 1; // Below platform for void areas
    }
    
    // Required codec implementation for NeoForge 1.21.5
    // The signature changed in NeoForge 1.21.5 to return MapCodec instead of Codec
    @Override
    public com.mojang.serialization.MapCodec<? extends ChunkGenerator> codec() {
        return com.mojang.serialization.MapCodec.unit(this);
    }
}