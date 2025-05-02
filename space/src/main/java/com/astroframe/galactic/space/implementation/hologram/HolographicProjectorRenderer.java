package com.astroframe.galactic.space.implementation.hologram;

import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;

import java.util.List;
import org.joml.Matrix4f;

/**
 * Renderer for the Holographic Projector block entity.
 * Displays the holographic projection of a rocket based on components.
 * Uses a simplified approach with LevelRenderer.renderLineBox for rendering.
 */
public class HolographicProjectorRenderer implements BlockEntityRenderer<HolographicProjectorBlockEntity> {
    
    // Hologram colors
    private static final float HOLOGRAM_RED = 0.0F;
    private static final float HOLOGRAM_GREEN = 0.8F;
    private static final float HOLOGRAM_BLUE = 1.0F;
    private static final float HOLOGRAM_ALPHA = 0.7F;
    
    /**
     * Constructor for the renderer.
     *
     * @param context The renderer provider context
     */
    public HolographicProjectorRenderer(BlockEntityRendererProvider.Context context) {
        // Renderer setup
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
        
        // Set up rendering state
        RenderSystem.setShaderColor(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        // Render base platform
        renderHologramBase(poseStack);
        
        // If we have rocket data, render the rocket components
        if (rocketData != null) {
            renderRocketComponents(rocketData, poseStack);
        } else {
            // Render placeholder if no rocket data available
            renderPlaceholderRocket(poseStack);
        }
        
        // Add scan lines
        float time = (Minecraft.getInstance().level.getGameTime() % 80) / 80.0F;
        float scanHeight = Mth.sin(time * (float) Math.PI * 2) * 1.5F;
        renderScanLines(poseStack, scanHeight);
        
        // Reset rendering state
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        
        poseStack.popPose();
    }
    
    /**
     * Renders the base platform of the hologram.
     *
     * @param poseStack The pose stack
     */
    private void renderHologramBase(PoseStack poseStack) {
        poseStack.pushPose();
        
        // Move to base position
        poseStack.translate(0, -0.4, 0);
        poseStack.scale(1.2F, 0.02F, 1.2F);
        
        // Render a circular platform
        AABB basePlatform = new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5);
        LevelRenderer.renderLineBox(poseStack, basePlatform, HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        
        poseStack.popPose();
    }
    
    /**
     * Renders the rocket components from the rocket data.
     *
     * @param rocket The rocket data
     * @param poseStack The pose stack
     */
    private void renderRocketComponents(IRocket rocket, PoseStack poseStack) {
        if (rocket == null) return;
        
        poseStack.pushPose();
        
        // Adjust position for rocket components
        poseStack.translate(0, 0.5, 0);
        
        // Get all components from the rocket
        List<IRocketComponent> components = rocket.getAllComponents();
        
        // Render each component
        for (IRocketComponent component : components) {
            renderRocketComponent(component, poseStack);
        }
        
        poseStack.popPose();
    }
    
    /**
     * Renders a single rocket component.
     *
     * @param component The rocket component
     * @param poseStack The pose stack
     */
    private void renderRocketComponent(IRocketComponent component, PoseStack poseStack) {
        if (component == null) return;
        
        poseStack.pushPose();
        
        // Get component position and size from the component
        Vec3 position = component.getPosition();
        Vec3 size = component.getSize();
        
        // Translate to component position
        poseStack.translate(position.x, position.y, position.z);
        
        // Scale based on component size
        poseStack.scale((float)size.x, (float)size.y, (float)size.z);
        
        // Render a holographic box for the component
        AABB componentBox = new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5);
        LevelRenderer.renderLineBox(poseStack, componentBox, HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        
        poseStack.popPose();
    }
    
    /**
     * Renders a placeholder rocket when no rocket data is available.
     *
     * @param poseStack The pose stack
     */
    private void renderPlaceholderRocket(PoseStack poseStack) {
        poseStack.pushPose();
        
        // Adjust position for placeholder
        poseStack.translate(0, 0.5, 0);
        
        // Body
        poseStack.pushPose();
        poseStack.scale(0.3F, 1.0F, 0.3F);
        AABB rocketBody = new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5);
        LevelRenderer.renderLineBox(poseStack, rocketBody, HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        poseStack.popPose();
        
        // Nose cone (simplified as a small box)
        poseStack.pushPose();
        poseStack.translate(0, 0.75, 0);
        poseStack.scale(0.2F, 0.3F, 0.2F);
        AABB noseCone = new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5);
        LevelRenderer.renderLineBox(poseStack, noseCone, HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
        poseStack.popPose();
        
        // Fins
        for (int i = 0; i < 4; i++) {
            poseStack.pushPose();
            poseStack.translate(0, -0.4, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(90 * i));
            poseStack.translate(0.31, 0, 0);
            poseStack.scale(0.1F, 0.3F, 0.3F);
            
            AABB fin = new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5);
            LevelRenderer.renderLineBox(poseStack, fin, HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA);
            
            poseStack.popPose();
        }
        
        poseStack.popPose();
    }
    
    /**
     * Renders holographic scan lines for added effect.
     *
     * @param poseStack The pose stack
     * @param scanHeight The height of the scan effect
     */
    private void renderScanLines(PoseStack poseStack, float scanHeight) {
        poseStack.pushPose();
        
        // Position the scan plane
        poseStack.translate(0, scanHeight, 0);
        
        // Render a horizontal scan plane
        float size = 1.0F;
        
        // Render a grid
        AABB scanPlane = new AABB(-size, 0, -size, size, 0, size);
        LevelRenderer.renderLineBox(poseStack, scanPlane, HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, 0.3F * HOLOGRAM_ALPHA);
        
        poseStack.popPose();
    }
}