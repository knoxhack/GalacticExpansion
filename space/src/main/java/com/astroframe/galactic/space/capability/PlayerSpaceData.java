package com.astroframe.galactic.space.capability;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Capability data for tracking a player's space-related data.
 */
public class PlayerSpaceData {
    private final List<ResourceLocation> discoveredBodies;
    private ResourceLocation lastVisitedBody;
    private int spaceExplorationExperience;
    
    public PlayerSpaceData() {
        this.discoveredBodies = new ArrayList<>();
        this.lastVisitedBody = null;
        this.spaceExplorationExperience = 0;
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
     * Saves this data to a CompoundTag.
     * @return The saved data
     */
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        
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
        
        return tag;
    }
    
    /**
     * Loads data from a CompoundTag.
     * @param tag The tag to load from
     */
    public void load(CompoundTag tag) {
        // Clear existing data
        discoveredBodies.clear();
        
        // Load discovered bodies
        if (tag.contains("DiscoveredBodies")) {
            // Get tag without using ifPresent
            ListTag bodiesTag = new ListTag();
            Tag tagElement = tag.get("DiscoveredBodies");
            if (tagElement != null && tagElement instanceof ListTag listTag) {
                bodiesTag.addAll(listTag);
            }
            
            for (int i = 0; i < bodiesTag.size(); i++) {
                // Use getString method with orElse for Optional
                String idString = "";
                if (bodiesTag.getString(i) != null) {
                    // Since getString returns Optional<String> in 1.21.5
                    try {
                        idString = bodiesTag.getString(i).orElse("");
                    } catch (Exception e) {
                        // Fallback if the method signature changed or errored
                        GalacticSpace.LOGGER.warn("Error getting string from tag: " + e.getMessage());
                    }
                }
                
                if (!idString.isEmpty()) {
                    discoveredBodies.add(ResourceLocation.parse(idString));
                }
            }
        }
        
        // Load last visited body
        if (tag.contains("LastVisitedBody")) {
            String bodyId = tag.getString("LastVisitedBody").orElse("");
            lastVisitedBody = bodyId.isEmpty() ? null : ResourceLocation.parse(bodyId);
        } else {
            lastVisitedBody = null;
        }
        
        // Load experience
        spaceExplorationExperience = tag.getInt("Experience").orElse(0);
    }
}