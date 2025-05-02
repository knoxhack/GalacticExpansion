package com.astroframe.galactic.space.implementation.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix4f;

/**
 * Helper class for vertex rendering compatibility across Minecraft versions.
 * Simplified for NeoForge 1.21.5 compatibility with reduced functionality
 */
public class VertexHelper {
    
    /**
     * Adds a vertex to the vertex consumer with minimal required parameters
     * Simplified for NeoForge 1.21.5 compatibility
     *
     * @param consumer The vertex consumer
     * @param x The x coordinate
     * @param y The y coordinate 
     * @param z The z coordinate
     * @param red The red color component (0.0-1.0)
     * @param green The green color component (0.0-1.0)
     * @param blue The blue color component (0.0-1.0)
     * @param alpha The alpha component (0.0-1.0)
     */
    public static void addColoredVertex(VertexConsumer consumer,
                                      float x, float y, float z,
                                      float red, float green, float blue, float alpha) {
        // In NeoForge 1.21.5, we need to directly add each element of the vertex
        // Note that the actual order may need to be adjusted based on the exact buffer format expected
        consumer.vertex(x, y, z);                   // Position
        consumer.color(red, green, blue, alpha);    // Color
        consumer.normal(0.0F, 1.0F, 0.0F);          // Normal - using UP as default
        consumer.uv(0.0F, 0.0F);                    // Texture UV
        consumer.overlayCoords(OverlayTexture.NO_OVERLAY); // Default overlay
        consumer.uv2(calculateLightValue());        // Light UV (full brightness)
        consumer.endVertex();                       // Complete the vertex
    }
    
    /**
     * Adds a vertex to the vertex consumer with a transformation matrix
     * 
     * @param consumer The vertex consumer
     * @param matrix The transformation matrix
     * @param x The x coordinate
     * @param y The y coordinate 
     * @param z The z coordinate
     * @param red The red color component (0.0-1.0)
     * @param green The green color component (0.0-1.0)
     * @param blue The blue color component (0.0-1.0)
     * @param alpha The alpha component (0.0-1.0)
     */
    public static void addColoredVertex(VertexConsumer consumer, Matrix4f matrix,
                                      float x, float y, float z,
                                      float red, float green, float blue, float alpha) {
        // This version includes matrix transformation
        consumer.vertex(matrix, x, y, z);           // Position with matrix transform
        consumer.color(red, green, blue, alpha);    // Color
        consumer.normal(0.0F, 1.0F, 0.0F);          // Normal - using UP as default
        consumer.uv(0.0F, 0.0F);                    // Texture UV
        consumer.overlayCoords(OverlayTexture.NO_OVERLAY); // Default overlay
        consumer.uv2(calculateLightValue());        // Light UV (full brightness)
        consumer.endVertex();                       // Complete the vertex
    }
    
    /**
     * Creates a basic method to calculate light values
     * 
     * @return Full brightness light value
     */
    public static int calculateLightValue() {
        // Return full brightness light
        return 15 << 20 | 15 << 4;
    }
    
    /**
     * Utility method to get the overlay texture coordinates
     * 
     * @return The overlay coordinates
     */
    public static int getOverlayCoords() {
        return OverlayTexture.NO_OVERLAY;
    }
    
    /**
     * Draw a line between two points
     * 
     * @param consumer The vertex consumer
     * @param matrix The matrix transformation
     * @param x1 The first point X coordinate
     * @param y1 The first point Y coordinate
     * @param z1 The first point Z coordinate
     * @param x2 The second point X coordinate
     * @param y2 The second point Y coordinate
     * @param z2 The second point Z coordinate
     * @param red The red color component (0.0-1.0)
     * @param green The green color component (0.0-1.0)
     * @param blue The blue color component (0.0-1.0)
     * @param alpha The alpha color component (0.0-1.0)
     */
    public static void drawLine(VertexConsumer consumer, Matrix4f matrix,
                             float x1, float y1, float z1,
                             float x2, float y2, float z2,
                             float red, float green, float blue, float alpha) {
        // Add two vertices to form a line
        addColoredVertex(consumer, matrix, x1, y1, z1, red, green, blue, alpha);
        addColoredVertex(consumer, matrix, x2, y2, z2, red, green, blue, alpha);
    }
    
    /**
     * Draw a rectangle in 3D space
     */
    public static void drawRect(PoseStack poseStack, VertexConsumer consumer, 
                              float x, float y, float z, float width, float height,
                              float red, float green, float blue, float alpha) {
        // Get the matrix from the current pose
        Matrix4f matrix = poseStack.last().pose();
        
        // Draw the rectangle as two triangles (would need different consumer for triangles)
        // This is a stub that just creates a rectangle outline
        drawLine(consumer, matrix, x, y, z, x + width, y, z, red, green, blue, alpha);
        drawLine(consumer, matrix, x + width, y, z, x + width, y + height, z, red, green, blue, alpha);
        drawLine(consumer, matrix, x + width, y + height, z, x, y + height, z, red, green, blue, alpha);
        drawLine(consumer, matrix, x, y + height, z, x, y, z, red, green, blue, alpha);
    }
}