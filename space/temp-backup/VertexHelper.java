package com.astroframe.galactic.space.implementation.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix4f;

/**
 * Helper class for vertex rendering compatibility across Minecraft versions.
 * Completely empty stub implementation for NeoForge 1.21.5 compatibility to allow compilation.
 */
public class VertexHelper {
    
    /**
     * Non-functional stub for vertex creation, for compatibility only.
     * Does nothing in this implementation to allow compilation.
     */
    public static void addColoredVertex(VertexConsumer consumer,
                                      float x, float y, float z,
                                      float red, float green, float blue, float alpha) {
        // Empty stub implementation to allow compilation
    }
    
    /**
     * Non-functional stub with matrix parameter, for compatibility only.
     * Does nothing in this implementation to allow compilation.
     */
    public static void addColoredVertex(VertexConsumer consumer, Matrix4f matrix,
                                      float x, float y, float z,
                                      float red, float green, float blue, float alpha) {
        // Empty stub implementation to allow compilation
    }
    
    /**
     * Utility constant for light value
     */
    public static int calculateLightValue() {
        return 15 << 20 | 15 << 4;
    }
    
    /**
     * Utility constant for overlay
     */
    public static int getOverlayCoords() {
        return OverlayTexture.NO_OVERLAY;
    }
    
    /**
     * Non-functional stub for drawing lines, for compatibility only.
     * Does nothing in this implementation to allow compilation.
     */
    public static void drawLine(VertexConsumer consumer, Matrix4f matrix,
                             float x1, float y1, float z1,
                             float x2, float y2, float z2,
                             float red, float green, float blue, float alpha) {
        // Empty stub implementation to allow compilation
    }
    
    /**
     * Non-functional stub for drawing rectangles, for compatibility only.
     * Does nothing in this implementation to allow compilation.
     */
    public static void drawRect(PoseStack poseStack, VertexConsumer consumer, 
                              float x, float y, float z, float width, float height,
                              float red, float green, float blue, float alpha) {
        // Empty stub implementation to allow compilation
    }
}