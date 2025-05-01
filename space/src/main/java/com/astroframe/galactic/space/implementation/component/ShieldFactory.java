package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IShield;
import com.astroframe.galactic.core.api.space.component.enums.ShieldType;
import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Factory for creating standard shield components.
 */
public class ShieldFactory {
    
    /**
     * Creates a quantum shield component.
     * @return A new quantum shield
     */
    public static IShield createQuantumShield() {
        return new ShieldImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":shield_quantum"),
                Component.translatable("component.galactic-space.shield_quantum"),
                3,
                350,
                500.0f,
                1.2f,
                1.4f,
                1.3f,
                40,
                true,
                0.99f,
                ShieldType.QUANTUM
        );
    }
}