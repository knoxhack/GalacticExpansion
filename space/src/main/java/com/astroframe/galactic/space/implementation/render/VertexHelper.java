package com.astroframe.galactic.space.implementation.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

/**
 * Helper class for vertex rendering compatibility across Minecraft versions.
 */
public class VertexHelper {
    
    /**
     * Adds a vertex to the vertex consumer with the given position and color.
     * This handles API differences between Minecraft versions.
     * 
     * @param consumer The vertex consumer
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @param red The red component (0.0-1.0)
     * @param green The green component (0.0-1.0)
     * @param blue The blue component (0.0-1.0)
     * @param alpha The alpha component (0.0-1.0)
     */
    public static void addColoredVertex(VertexConsumer consumer, float x, float y, float z, 
                                      float red, float green, float blue, float alpha) {
        // In NeoForge 1.21.5, use the BufferBuilder directly
        try {
            if (consumer instanceof BufferBuilder) {
                BufferBuilder buffer = (BufferBuilder) consumer;
                
                // Add vertex information
                buffer.vertex(x, y, z)
                      .color(red, green, blue, alpha)
                      .uv(0, 0)
                      .uv2(0)
                      .normal(0, 1, 0)
                      .endVertex();
            } else {
                // Fallback for other VertexConsumer implementations
                PoseStack poseStack = new PoseStack();
                poseStack.pushPose();
                consumer.vertex(poseStack.last().pose(), x, y, z)
                        .color(red, green, blue, alpha)
                        .uv(0, 0)
                        .overlayCoords(0, 0)
                        .uv2(0)
                        .normal(poseStack.last().normal(), 0, 1, 0)
                        .endVertex();
                poseStack.popPose();
            }
        } catch (Exception e) {
            // Log fallback for error handling
            System.err.println("Error in vertex rendering: " + e.getMessage());
        }
    }
}