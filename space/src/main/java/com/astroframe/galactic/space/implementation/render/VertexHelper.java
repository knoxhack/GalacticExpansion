package com.astroframe.galactic.space.implementation.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import net.minecraft.core.Direction;

/**
 * Helper class for vertex rendering compatibility across Minecraft versions.
 * Updated for NeoForge 1.21.5
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
        // Implementation for NeoForge 1.21.5
        // Use PoseStack for creating a transformation matrix
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        Matrix4f matrix = poseStack.last().pose();
        
        // Use the VertexConsumer correctly with matrix transformation
        addTransformedVertex(consumer, matrix, x, y, z, red, green, blue, alpha);
        
        poseStack.popPose();
    }
    
    /**
     * Adds a transformed vertex to the vertex consumer.
     * 
     * @param consumer The vertex consumer
     * @param matrix The transformation matrix
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @param red The red component (0.0-1.0)
     * @param green The green component (0.0-1.0)
     * @param blue The blue component (0.0-1.0)
     * @param alpha The alpha component (0.0-1.0)
     */
    private static void addTransformedVertex(VertexConsumer consumer, Matrix4f matrix, 
                                          float x, float y, float z, 
                                          float red, float green, float blue, float alpha) {
        // Use the NeoForge 1.21.5 compatible method for adding a vertex with all required data
        // Note: Method chain may vary based on the specific VertexConsumer implementation
        consumer.vertex(matrix, x, y, z)
                .color(red, green, blue, alpha)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();
    }
    
    /**
     * Creates a vertex with position, color, and using a transform matrix.
     * 
     * @param consumer The vertex consumer
     * @param matrix The transform matrix
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @param red The red component (0.0-1.0)
     * @param green The green component (0.0-1.0)
     * @param blue The blue component (0.0-1.0)
     * @param alpha The alpha component (0.0-1.0)
     */
    public static void addColoredVertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z, 
                                      float red, float green, float blue, float alpha) {
        // Use the helper method to add the vertex with all required data
        addTransformedVertex(consumer, matrix, x, y, z, red, green, blue, alpha);
    }
}