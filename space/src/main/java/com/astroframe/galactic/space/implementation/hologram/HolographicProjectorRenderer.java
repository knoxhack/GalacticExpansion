package com.astroframe.galactic.space.implementation.hologram;

import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Renderer for the Holographic Projector block entity.
 * Uses NeoForge 1.21.5 compatible rendering methods.
 */
public class HolographicProjectorRenderer implements BlockEntityRenderer<HolographicProjectorBlockEntity> {

    // Hologram color constants
    public static final float HOLOGRAM_RED = 0.0f;
    public static final float HOLOGRAM_GREEN = 0.7f;
    public static final float HOLOGRAM_BLUE = 1.0f;
    public static final float HOLOGRAM_ALPHA = 0.7f;
    
    private final BlockEntityRendererProvider.Context context;
    
    /**
     * Creates a new holographic projector renderer.
     *
     * @param context The renderer context
     */
    public HolographicProjectorRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }
    
    @Override
    public void render(HolographicProjectorBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
                      MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, Vec3 camPos) {
        // Spinning animation
        float angle = (Minecraft.getInstance().level.getGameTime() + partialTicks) * 1.5F % 360F;
        
        // Get rocket data from the block entity
        IRocket rocketData = blockEntity.getRocketData();
        
        // Setup the rendering pose
        poseStack.pushPose();
        
        // Position the hologram above the projector
        poseStack.translate(0.5D, 1.0D, 0.5D);
        
        // Apply the rotation for the spinning effect
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        
        // Get a specific buffer for lines from the buffer source
        VertexConsumer lineBuffer = bufferSource.getBuffer(RenderType.lines());
        
        // Render base platform with VertexConsumer
        renderHologramBase(poseStack, lineBuffer);
        
        // Render rocket data or placeholder
        if (rocketData != null) {
            renderRocketComponents(rocketData, poseStack, lineBuffer);
        } else {
            renderPlaceholderRocket(poseStack, lineBuffer);
        }
        
        // Add scan lines
        float time = (Minecraft.getInstance().level.getGameTime() % 80) / 80.0F;
        float scanHeight = Mth.sin(time * (float) Math.PI * 2) * 1.5F;
        renderScanLines(poseStack, lineBuffer, scanHeight);
        
        poseStack.popPose();
    }
    
    /**
     * Renders the base platform of the hologram.
     *
     * @param poseStack The pose stack
     * @param consumer The vertex consumer
     */
    private void renderHologramBase(PoseStack poseStack, VertexConsumer consumer) {
        poseStack.pushPose();
        
        // Move to base position
        poseStack.translate(0, -0.4, 0);
        poseStack.scale(1.0F, 0.02F, 1.0F);
        
        // Create a circular platform
        renderCircle(poseStack, consumer, 0.5f, 16);
        
        poseStack.popPose();
    }
    
    /**
     * Renders a circle in the XZ plane.
     *
     * @param poseStack The pose stack
     * @param consumer The vertex consumer
     * @param radius The radius of the circle
     * @param segments The number of segments to use
     */
    private void renderCircle(PoseStack poseStack, VertexConsumer consumer, float radius, int segments) {
        Matrix4f pose = poseStack.last().pose();
        
        // Draw a circle with lines
        for (int i = 0; i < segments; i++) {
            float angle1 = (float) (2 * Math.PI * i / segments);
            float angle2 = (float) (2 * Math.PI * ((i + 1) % segments) / segments);
            
            float x1 = (float) (radius * Math.cos(angle1));
            float z1 = (float) (radius * Math.sin(angle1));
            
            float x2 = (float) (radius * Math.cos(angle2));
            float z2 = (float) (radius * Math.sin(angle2));
            
            // Draw line for the circle
            drawLine(consumer, pose, x1, 0.0f, z1, x2, 0.0f, z2, 
                    HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
            
            // Draw line from center
            float alpha = HOLOGRAM_ALPHA;
            float fadedAlpha = 0.3f * HOLOGRAM_ALPHA;
            
            // Line from center to edge
            drawLine(consumer, pose, 0.0f, 0.0f, 0.0f, x1, 0.0f, z1,
                    HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, fadedAlpha);
        }
    }
    
    /**
     * Draws a line between two points.
     */
    private void drawLine(VertexConsumer consumer, Matrix4f pose, 
                         float x1, float y1, float z1, float x2, float y2, float z2,
                         float red, float green, float blue, float alpha) {
        // Calculate transformed positions using the matrix multiplication for NeoForge 1.21.5
        // This way we apply the matrix transformation ourselves before passing to the vertex consumer
        float[] pos1 = applyMatrix(pose, x1, y1, z1);
        float[] pos2 = applyMatrix(pose, x2, y2, z2);
        
        // Use the correct vertex method format for Minecraft 1.21.5
        consumer.vertex(pos1[0], pos1[1], pos1[2], red, green, blue, alpha, 0, 0, 0, 0, 1, 0, 0);
                
        consumer.vertex(pos2[0], pos2[1], pos2[2], red, green, blue, alpha, 0, 0, 0, 0, 1, 0, 0);
    }
    
    /**
     * Applies a matrix transformation to a point
     * 
     * @param matrix The transformation matrix
     * @param x The x coordinate
     * @param y The y coordinate
     * @param z The z coordinate
     * @return The transformed coordinates as a float array [x, y, z]
     */
    private float[] applyMatrix(Matrix4f matrix, float x, float y, float z) {
        Vector4f vec = new Vector4f(x, y, z, 1.0f);
        vec.mul(matrix);
        return new float[] { vec.x(), vec.y(), vec.z() };
    }
    
    /**
     * Renders the rocket components from the rocket data.
     *
     * @param rocket The rocket data
     * @param poseStack The pose stack
     * @param consumer The vertex consumer
     */
    private void renderRocketComponents(IRocket rocket, PoseStack poseStack, VertexConsumer consumer) {
        if (rocket == null) return;
        
        poseStack.pushPose();
        
        // Adjust position for rocket components
        poseStack.translate(0, 0.5, 0);
        
        // Get all components from the rocket
        List<IRocketComponent> components = rocket.getAllComponents();
        
        // Render each component
        for (IRocketComponent component : components) {
            renderRocketComponent(component, poseStack, consumer);
        }
        
        poseStack.popPose();
    }
    
    /**
     * Renders a single rocket component.
     *
     * @param component The rocket component
     * @param poseStack The pose stack
     * @param consumer The vertex consumer
     */
    private void renderRocketComponent(IRocketComponent component, PoseStack poseStack, VertexConsumer consumer) {
        if (component == null) return;
        
        poseStack.pushPose();
        
        // Get component position and size
        Vec3 position = component.getPosition();
        Vec3 size = component.getSize();
        
        // Translate to component position
        poseStack.translate(position.x, position.y, position.z);
        
        // Scale based on component size
        poseStack.scale((float)size.x, (float)size.y, (float)size.z);
        
        // Render a box for the component
        renderBox(poseStack, consumer);
        
        poseStack.popPose();
    }
    
    /**
     * Renders a box centered at the origin with size 1x1x1.
     *
     * @param poseStack The pose stack
     * @param consumer The vertex consumer
     */
    private void renderBox(PoseStack poseStack, VertexConsumer consumer) {
        Matrix4f pose = poseStack.last().pose();
        
        // Draw 12 edges of a 1x1x1 cube centered at the origin
        // Bottom face
        drawLine(consumer, pose, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 
                HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        drawLine(consumer, pose, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 
                HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        drawLine(consumer, pose, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 
                HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        drawLine(consumer, pose, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 
                HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        
        // Top face
        drawLine(consumer, pose, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 
                HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        drawLine(consumer, pose, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 
                HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        drawLine(consumer, pose, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 
                HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        drawLine(consumer, pose, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 
                HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        
        // Connecting edges
        drawLine(consumer, pose, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 
                HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        drawLine(consumer, pose, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 
                HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        drawLine(consumer, pose, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 
                HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        drawLine(consumer, pose, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 
                HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
    }
    
    /**
     * Renders a placeholder rocket when no rocket data is available.
     *
     * @param poseStack The pose stack
     * @param consumer The vertex consumer
     */
    private void renderPlaceholderRocket(PoseStack poseStack, VertexConsumer consumer) {
        poseStack.pushPose();
        
        // Adjust position for placeholder
        poseStack.translate(0, 0.5, 0);
        
        // Body
        poseStack.pushPose();
        poseStack.scale(0.3F, 1.0F, 0.3F);
        renderBox(poseStack, consumer);
        poseStack.popPose();
        
        // Nose cone
        poseStack.pushPose();
        poseStack.translate(0, 0.75, 0);
        poseStack.scale(0.2F, 0.3F, 0.2F);
        renderBox(poseStack, consumer);
        poseStack.popPose();
        
        // Fins
        for (int i = 0; i < 4; i++) {
            poseStack.pushPose();
            poseStack.translate(0, -0.4, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(90 * i));
            poseStack.translate(0.2, 0, 0);
            poseStack.scale(0.1F, 0.3F, 0.2F);
            renderBox(poseStack, consumer);
            poseStack.popPose();
        }
        
        poseStack.popPose();
    }
    
    /**
     * Renders holographic scan lines for added effect.
     *
     * @param poseStack The pose stack
     * @param consumer The vertex consumer
     * @param scanHeight The height of the scan effect
     */
    private void renderScanLines(PoseStack poseStack, VertexConsumer consumer, float scanHeight) {
        poseStack.pushPose();
        
        // Position the scan plane
        poseStack.translate(0, scanHeight, 0);
        
        // Render a grid of lines
        Matrix4f pose = poseStack.last().pose();
        float size = 1.0F;
        int gridSize = 4;
        float step = size * 2 / gridSize;
        
        // Render the grid with lower alpha
        float scanAlpha = 0.3F * HOLOGRAM_ALPHA;
        
        // Horizontal lines
        for (int i = 0; i <= gridSize; i++) {
            float z = -size + i * step;
            drawLine(consumer, pose, -size, 0.0f, z, size, 0.0f, z,
                    HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, scanAlpha);
        }
        
        // Vertical lines
        for (int i = 0; i <= gridSize; i++) {
            float x = -size + i * step;
            drawLine(consumer, pose, x, 0.0f, -size, x, 0.0f, size,
                    HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, scanAlpha);
        }
        
        poseStack.popPose();
    }
}