package com.astroframe.galactic.space.implementation.hologram;

import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Renderer for the Holographic Projector block entity.
 * Displays the holographic projection of a rocket based on components.
 */
public class HolographicProjectorRenderer implements BlockEntityRenderer<HolographicProjectorBlockEntity> {
    
    // Hologram colors
    private static final float HOLOGRAM_RED = 0.0F;
    private static final float HOLOGRAM_GREEN = 0.8F;
    private static final float HOLOGRAM_BLUE = 1.0F;
    private static final float HOLOGRAM_ALPHA = 0.7F;
    
    // Texture for lines
    private static final ResourceLocation HOLOGRAM_TEXTURE = 
            ResourceLocation.parse("galactic:textures/hologram/hologram.png");
    
    /**
     * Constructor for the renderer.
     *
     * @param context The renderer provider context
     */
    public HolographicProjectorRenderer(BlockEntityRendererProvider.Context context) {
        // No initialization needed
    }
    
    /**
     * Renders the holographic projection.
     *
     * @param blockEntity The holographic projector block entity
     * @param partialTick The partial tick time
     * @param poseStack The pose stack
     * @param bufferSource The buffer source
     * @param packedLight The packed light
     * @param packedOverlay The packed overlay
     */
    /**
     * Implements the BlockEntityRenderer interface for rendering the holographic projector.
     */
    @Override
    public void render(HolographicProjectorBlockEntity blockEntity, float partialTick, PoseStack poseStack, 
                     MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        // Only render if the projector is active
        if (!blockEntity.isActive()) {
            return;
        }
        
        // Get the rocket data
        IRocket rocketData = blockEntity.getRocketData();
        
        // Get the rotation angle with smooth interpolation for animation
        float angle = blockEntity.getRotationAngle() + partialTick * 0.5F;
        
        // Setup the rendering pose
        poseStack.pushPose();
        
        // Position the hologram above the projector
        poseStack.translate(0.5D, 1.0D, 0.5D);
        
        // Apply the rotation for the spinning effect
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        
        // Use a direct rendering approach with BufferBuilder for compatibility
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        
        // Set up render state
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        
        // Start building lines - NeoForge 1.21.5 compatible
        builder.begin(com.mojang.blaze3d.vertex.VertexFormat.Mode.LINES, 
                      com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR_TEX_NORMAL_PADDING);
        
        // Render base platform with direct BufferBuilder
        renderHologramBase(poseStack, builder);
        
        // Render rocket data or placeholder
        if (rocketData != null) {
            renderRocketComponents(rocketData, poseStack, builder);
        } else {
            renderPlaceholderRocket(poseStack, builder);
        }
        
        // Add scan lines
        float time = (Minecraft.getInstance().level.getGameTime() % 80) / 80.0F;
        float scanHeight = Mth.sin(time * (float) Math.PI * 2) * 1.5F;
        renderScanLines(poseStack, builder, scanHeight);
        
        // Finish rendering
        tesselator.getBuilder().end(); // In NeoForge 1.21.5, we end the builder and draw it
        
        // Reset render state (NeoForge 1.21.5 doesn't use RenderSystem.disableBlend directly)
        // Instead we rely on the renderer to handle state
        
        poseStack.popPose();
    }
    
    /**
     * Renders the base platform of the hologram.
     *
     * @param poseStack The pose stack
     * @param builder The buffer builder
     */
    private void renderHologramBase(PoseStack poseStack, BufferBuilder builder) {
        poseStack.pushPose();
        
        // Move to base position
        poseStack.translate(0, -0.4, 0);
        poseStack.scale(1.0F, 0.02F, 1.0F);
        
        // Create a circular platform
        renderCircle(poseStack, builder, 0.5f, 16);
        
        poseStack.popPose();
    }
    
    /**
     * Renders a circle in the XZ plane.
     *
     * @param poseStack The pose stack
     * @param builder The buffer builder
     * @param radius The radius of the circle
     * @param segments The number of segments to use
     */
    private void renderCircle(PoseStack poseStack, BufferBuilder builder, float radius, int segments) {
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
            drawLineBuffered(pose, builder, x1, 0.0f, z1, x2, 0.0f, z2);
            
            // Draw line from center
            float red = HOLOGRAM_RED;
            float green = HOLOGRAM_GREEN;
            float blue = HOLOGRAM_BLUE;
            float alpha = HOLOGRAM_ALPHA;
            float fadedAlpha = 0.3f * HOLOGRAM_ALPHA;
            
            // Line from center to edge - NeoForge 1.21.5 compatible method
            Vector4f pos1 = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
            pos1.mul(pose);
            builder.vertex(pos1.x, pos1.y, pos1.z)
                   .color(red, green, blue, alpha)
                   .uv(0.0f, 1.0f)
                   .normal(0, 0, 1)
                   .endVertex();
                    
            Vector4f pos2 = new Vector4f(x1, 0.0f, z1, 1.0f);
            pos2.mul(pose);
            builder.vertex(pos2.x, pos2.y, pos2.z)
                   .color(red, green, blue, fadedAlpha)
                   .uv(0.0f, 1.0f)
                   .normal(0, 0, 1)
                   .endVertex();
        }
    }
    
    /**
     * Draws a line between two points using BufferBuilder.
     *
     * @param pose The matrix pose
     * @param builder The buffer builder
     * @param x1 Start X
     * @param y1 Start Y
     * @param z1 Start Z
     * @param x2 End X
     * @param y2 End Y
     * @param z2 End Z
     */
    private void drawLineBuffered(Matrix4f pose, BufferBuilder builder, float x1, float y1, float z1, 
                                  float x2, float y2, float z2) {
        // Use direct color components
        float red = HOLOGRAM_RED;
        float green = HOLOGRAM_GREEN;
        float blue = HOLOGRAM_BLUE;
        float alpha = HOLOGRAM_ALPHA;
        
        // Draw the line using Vector4f transformation for NeoForge 1.21.5
        Vector4f pos1 = new Vector4f(x1, y1, z1, 1.0f);
        pos1.mul(pose);
        builder.vertex(pos1.x, pos1.y, pos1.z)
               .color(red, green, blue, alpha)
               .uv(0.0f, 1.0f)
               .normal(0, 0, 1)
               .endVertex();
                
        Vector4f pos2 = new Vector4f(x2, y2, z2, 1.0f);
        pos2.mul(pose);
        builder.vertex(pos2.x, pos2.y, pos2.z)
               .color(red, green, blue, alpha)
               .uv(0.0f, 1.0f)
               .normal(0, 0, 1)
               .endVertex();
    }
    
    /**
     * Renders the rocket components from the rocket data.
     *
     * @param rocket The rocket data
     * @param poseStack The pose stack
     * @param builder The buffer builder
     */
    private void renderRocketComponents(IRocket rocket, PoseStack poseStack, BufferBuilder builder) {
        if (rocket == null) return;
        
        poseStack.pushPose();
        
        // Adjust position for rocket components
        poseStack.translate(0, 0.5, 0);
        
        // Get all components from the rocket
        List<IRocketComponent> components = rocket.getAllComponents();
        
        // Render each component
        for (IRocketComponent component : components) {
            renderRocketComponent(component, poseStack, builder);
        }
        
        poseStack.popPose();
    }
    
    /**
     * Renders a single rocket component.
     *
     * @param component The rocket component
     * @param poseStack The pose stack
     * @param builder The buffer builder
     */
    private void renderRocketComponent(IRocketComponent component, PoseStack poseStack, BufferBuilder builder) {
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
        renderBox(poseStack, builder);
        
        poseStack.popPose();
    }
    
    /**
     * Renders a box centered at the origin with size 1x1x1.
     *
     * @param poseStack The pose stack
     * @param builder The buffer builder
     */
    private void renderBox(PoseStack poseStack, BufferBuilder builder) {
        Matrix4f pose = poseStack.last().pose();
        
        // Draw 12 edges of a 1x1x1 cube centered at the origin
        // Bottom face
        drawLineBuffered(pose, builder, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f);
        drawLineBuffered(pose, builder, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f);
        drawLineBuffered(pose, builder, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f);
        drawLineBuffered(pose, builder, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f);
        
        // Top face
        drawLineBuffered(pose, builder, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f);
        drawLineBuffered(pose, builder, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f);
        drawLineBuffered(pose, builder, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f);
        drawLineBuffered(pose, builder, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f);
        
        // Connecting edges
        drawLineBuffered(pose, builder, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f);
        drawLineBuffered(pose, builder, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f);
        drawLineBuffered(pose, builder, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f);
        drawLineBuffered(pose, builder, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f);
    }
    
    /**
     * Draws a scan line with custom alpha.
     *
     * @param pose The matrix pose
     * @param builder The buffer builder
     * @param x1 Start X
     * @param y1 Start Y
     * @param z1 Start Z
     * @param x2 End X
     * @param y2 End Y
     * @param z2 End Z
     * @param alphaValue The alpha value to use
     */
    private void drawScanLineBuffered(Matrix4f pose, BufferBuilder builder, float x1, float y1, float z1, 
                           float x2, float y2, float z2, float alphaValue) {
        // Use direct color components
        float red = HOLOGRAM_RED;
        float green = HOLOGRAM_GREEN;
        float blue = HOLOGRAM_BLUE;
        
        // Draw the line using Vector4f transformation for NeoForge 1.21.5
        Vector4f pos1 = new Vector4f(x1, y1, z1, 1.0f);
        pos1.mul(pose);
        builder.vertex(pos1.x, pos1.y, pos1.z)
               .color(red, green, blue, alphaValue)
               .uv(0.0f, 1.0f)
               .normal(0, 0, 1)
               .endVertex();
                
        Vector4f pos2 = new Vector4f(x2, y2, z2, 1.0f);
        pos2.mul(pose);
        builder.vertex(pos2.x, pos2.y, pos2.z)
               .color(red, green, blue, alphaValue)
               .uv(0.0f, 1.0f)
               .normal(0, 0, 1)
               .endVertex();
    }
    
    /**
     * Renders a placeholder rocket when no rocket data is available.
     *
     * @param poseStack The pose stack
     * @param builder The buffer builder
     */
    private void renderPlaceholderRocket(PoseStack poseStack, BufferBuilder builder) {
        poseStack.pushPose();
        
        // Adjust position for placeholder
        poseStack.translate(0, 0.5, 0);
        
        // Body
        poseStack.pushPose();
        poseStack.scale(0.3F, 1.0F, 0.3F);
        renderBox(poseStack, builder);
        poseStack.popPose();
        
        // Nose cone
        poseStack.pushPose();
        poseStack.translate(0, 0.75, 0);
        poseStack.scale(0.2F, 0.3F, 0.2F);
        renderBox(poseStack, builder);
        poseStack.popPose();
        
        // Fins
        for (int i = 0; i < 4; i++) {
            poseStack.pushPose();
            poseStack.translate(0, -0.4, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(90 * i));
            poseStack.translate(0.2, 0, 0);
            poseStack.scale(0.1F, 0.3F, 0.2F);
            renderBox(poseStack, builder);
            poseStack.popPose();
        }
        
        poseStack.popPose();
    }
    
    /**
     * Renders holographic scan lines for added effect.
     *
     * @param poseStack The pose stack
     * @param builder The buffer builder
     * @param scanHeight The height of the scan effect
     */
    private void renderScanLines(PoseStack poseStack, BufferBuilder builder, float scanHeight) {
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
            drawScanLineBuffered(pose, builder, -size, 0.0f, z, size, 0.0f, z, scanAlpha);
        }
        
        // Vertical lines
        for (int i = 0; i <= gridSize; i++) {
            float x = -size + i * step;
            drawScanLineBuffered(pose, builder, x, 0.0f, -size, x, 0.0f, size, scanAlpha);
        }
        
        poseStack.popPose();
    }
}