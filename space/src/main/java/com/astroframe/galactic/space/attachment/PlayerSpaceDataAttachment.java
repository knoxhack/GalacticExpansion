package com.astroframe.galactic.space.attachment;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.util.TagHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Attachment data for tracking a player's space-related data.
 * Uses NeoForge 1.21.5's attachment system instead of capabilities.
 */
public class PlayerSpaceDataAttachment {
    private final List<ResourceLocation> discoveredBodies;
    private ResourceLocation lastVisitedBody;
    private int spaceExplorationExperience;
    private boolean inSpace;
    private UUID currentRocketId;
    private ResourceLocation currentDimension;
    private boolean changingDimension;
    
    public PlayerSpaceDataAttachment() {
        this.discoveredBodies = new ArrayList<>();
        this.lastVisitedBody = null;
        this.spaceExplorationExperience = 0;
        this.inSpace = false;
        this.currentRocketId = null;
        this.currentDimension = null;
        this.changingDimension = false;
    }
    
    /**
     * Add a discovered celestial body.
     * @param body The celestial body
     * @return true if the body was newly discovered
     */
    public boolean discoverCelestialBody(ICelestialBody body) {
        if (!hasDiscovered(body)) {
            discoveredBodies.add(body.getId());
            return true;
        }
        return false;
    }
    
    /**
     * Checks if a celestial body has been discovered.
     * @param body The celestial body
     * @return true if discovered
     */
    public boolean hasDiscovered(ICelestialBody body) {
        return discoveredBodies.contains(body.getId());
    }
    
    /**
     * Gets all discovered celestial body IDs.
     * @return A list of celestial body IDs
     */
    public List<ResourceLocation> getDiscoveredBodies() {
        return new ArrayList<>(discoveredBodies);
    }
    
    /**
     * Sets the last visited celestial body.
     * @param body The celestial body
     */
    public void setLastVisitedBody(ICelestialBody body) {
        this.lastVisitedBody = body.getId();
    }
    
    /**
     * Gets the last visited celestial body ID.
     * @return The celestial body ID, or null if none
     */
    public ResourceLocation getLastVisitedBodyId() {
        return lastVisitedBody;
    }
    
    /**
     * Adds space exploration experience.
     * @param amount The amount to add
     */
    public void addExperience(int amount) {
        this.spaceExplorationExperience += amount;
    }
    
    /**
     * Gets the space exploration experience.
     * @return The experience amount
     */
    public int getExperience() {
        return spaceExplorationExperience;
    }
    
    /**
     * Check if player is currently in space.
     * @return true if in space
     */
    public boolean isInSpace() {
        return inSpace;
    }
    
    /**
     * Set whether the player is in space.
     * @param inSpace true if in space
     */
    public void setInSpace(boolean inSpace) {
        this.inSpace = inSpace;
    }
    
    /**
     * Get the ID of the rocket the player is currently in.
     * @return the rocket UUID, or null if not in a rocket
     */
    public UUID getCurrentRocketId() {
        return currentRocketId;
    }
    
    /**
     * Set the ID of the rocket the player is currently in.
     * @param rocketId the rocket UUID, or null if not in a rocket
     */
    public void setCurrentRocketId(UUID rocketId) {
        this.currentRocketId = rocketId;
    }
    
    /**
     * Get the current dimension the player is in.
     * @return the dimension resource location
     */
    public ResourceLocation getCurrentDimension() {
        return currentDimension;
    }
    
    /**
     * Set the current dimension the player is in.
     * @param dimension the dimension resource location
     */
    public void setCurrentDimension(ResourceLocation dimension) {
        this.currentDimension = dimension;
    }
    
    /**
     * Check if the player is currently changing dimensions.
     * @return true if changing dimensions
     */
    public boolean isChangingDimension() {
        return changingDimension;
    }
    
    /**
     * Set whether the player is currently changing dimensions.
     * @param changing true if changing dimensions
     */
    public void setChangingDimension(boolean changing) {
        this.changingDimension = changing;
    }
    
    /**
     * Serialization method for the attachment.
     * @param tag The tag to write to
     */
    public void write(CompoundTag tag) {
        // Save discovered bodies
        ListTag bodiesTag = new ListTag();
        for (ResourceLocation id : discoveredBodies) {
            bodiesTag.add(StringTag.valueOf(id.toString()));
        }
        tag.put("DiscoveredBodies", bodiesTag);
        
        // Save last visited body
        if (lastVisitedBody != null) {
            tag.putString("LastVisitedBody", lastVisitedBody.toString());
        }
        
        // Save experience
        tag.putInt("Experience", spaceExplorationExperience);
        
        // Save space state
        tag.putBoolean("InSpace", inSpace);
        
        // Save current rocket
        if (currentRocketId != null) {
            tag.putString("CurrentRocket", currentRocketId.toString());
        }
        
        // Save current dimension
        if (currentDimension != null) {
            tag.putString("CurrentDimension", currentDimension.toString());
        }
        
        // Save dimension change state
        tag.putBoolean("ChangingDimension", changingDimension);
    }
    
    /**
     * Deserialization method for the attachment.
     * @param tag The tag to read from
     * @return The created attachment
     */
    public static PlayerSpaceDataAttachment read(CompoundTag tag) {
        PlayerSpaceDataAttachment data = new PlayerSpaceDataAttachment();
        
        // Load discovered bodies
        if (tag.contains("DiscoveredBodies")) {
            // Get tag without using ifPresent
            ListTag bodiesTag = new ListTag();
            Tag tagElement = tag.get("DiscoveredBodies");
            if (tagElement != null && tagElement instanceof ListTag listTag) {
                bodiesTag.addAll(listTag);
            }
            
            for (int i = 0; i < bodiesTag.size(); i++) {
                // Direct access to string content in NeoForge 1.21.5
                if (bodiesTag.get(i) instanceof StringTag) {
                    String idString = TagHelper.getStringValue(bodiesTag.get(i));
                    if (!idString.isEmpty()) {
                        data.discoveredBodies.add(ResourceLocationHelper.parse(idString));
                    }
                } else {
                    // Fallback if tag type is unexpected
                    GalacticSpace.LOGGER.warn("Unexpected tag type in bodiesTag at index " + i);
                }
            }
        }
        
        // Load last visited body
        if (tag.contains("LastVisitedBody")) {
            Tag lastVisitedTag = tag.get("LastVisitedBody");
            if (lastVisitedTag instanceof StringTag) {
                String bodyId = TagHelper.getStringValue(lastVisitedTag);
                data.lastVisitedBody = bodyId.isEmpty() ? null : ResourceLocationHelper.parse(bodyId);
            } else {
                data.lastVisitedBody = null;
                GalacticSpace.LOGGER.warn("LastVisitedBody tag is not a StringTag");
            }
        }
        
        // Load experience
        if (tag.contains("Experience")) {
            int expValue = 0;
            try {
                // Direct integer conversion for NeoForge 1.21.5
                expValue = TagHelper.getIntValue(tag, "Experience");
            } catch (Exception e) {
                GalacticSpace.LOGGER.warn("Failed to get Experience value: " + e.getMessage());
            }
            data.spaceExplorationExperience = expValue;
        }
        
        // Load space state
        data.inSpace = TagHelper.getBooleanValue(tag, "InSpace");
        
        // Load current rocket
        if (tag.contains("CurrentRocket")) {
            try {
                String rocketIdStr = TagHelper.getStringValue(tag, "CurrentRocket");
                if (!rocketIdStr.isEmpty()) {
                    data.currentRocketId = UUID.fromString(rocketIdStr);
                }
            } catch (IllegalArgumentException e) {
                GalacticSpace.LOGGER.warn("Invalid UUID format for CurrentRocket: " + e.getMessage());
            }
        }
        
        // Load current dimension
        if (tag.contains("CurrentDimension")) {
            String dimString = TagHelper.getStringValue(tag, "CurrentDimension");
            if (!dimString.isEmpty()) {
                data.currentDimension = ResourceLocationHelper.parse(dimString);
            }
        }
        
        // Load dimension change state
        data.changingDimension = TagHelper.getBooleanValue(tag, "ChangingDimension");
        
        return data;
    }
}