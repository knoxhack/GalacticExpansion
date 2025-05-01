package com.astroframe.galactic.space.dimension;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

/**
 * Registry and configuration for the Space Station dimension.
 */
public class SpaceStationDimension {
    
    /**
     * The dimension location for the space station.
     */
    public static final ResourceLocation SPACE_STATION_LOCATION = 
        ResourceLocation.tryParse(GalacticSpace.MOD_ID + ":space_station");
    
    /**
     * The dimension type location for the space station.
     */
    public static final ResourceLocation SPACE_STATION_TYPE_LOCATION = 
        ResourceLocation.tryParse(GalacticSpace.MOD_ID + ":space_station_type");
    
    /**
     * The resource key for the space station dimension.
     */
    public static final ResourceKey<Level> SPACE_STATION_LEVEL_KEY = ResourceKey.create(
            Registries.DIMENSION,
            SPACE_STATION_LOCATION
    );
    
    /**
     * The resource key for the space station dimension type.
     */
    public static final ResourceKey<DimensionType> SPACE_STATION_TYPE_KEY = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            SPACE_STATION_TYPE_LOCATION
    );
    
    /**
     * Gets the resource location for the dimension.
     * 
     * @return The space station dimension resource location
     */
    public static ResourceLocation getDimensionLocation() {
        return SPACE_STATION_LEVEL_KEY.location();
    }
    
    /**
     * Checks if a level is the space station dimension.
     * 
     * @param level The level to check
     * @return true if the level is the space station dimension
     */
    public static boolean isSpaceStation(Level level) {
        return level.dimension().equals(SPACE_STATION_LEVEL_KEY);
    }
}