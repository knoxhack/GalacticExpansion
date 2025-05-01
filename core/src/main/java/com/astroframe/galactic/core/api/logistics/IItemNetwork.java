package com.astroframe.galactic.core.api.logistics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Set;

/**
 * Interface for an item network that can transport items between connected nodes.
 * This is the central system for the logistics module.
 */
public interface IItemNetwork {
    
    /**
     * Gets the unique identifier for this network.
     * 
     * @return The network ID
     */
    String getNetworkId();
    
    /**
     * Gets all nodes connected to this network.
     * 
     * @return A set of all connected node positions
     */
    Set<BlockPos> getConnectedNodes();
    
    /**
     * Checks if a node is connected to this network.
     * 
     * @param pos The position to check
     * @return Whether the position is a connected node
     */
    boolean isNodeConnected(BlockPos pos);
    
    /**
     * Connects a node to this network.
     * 
     * @param pos The position to connect
     * @param level The world level
     * @return Whether the connection was successful
     */
    boolean connectNode(BlockPos pos, Level level);
    
    /**
     * Disconnects a node from this network.
     * 
     * @param pos The position to disconnect
     * @return Whether the disconnection was successful
     */
    boolean disconnectNode(BlockPos pos);
    
    /**
     * Attempts to insert an item into the network.
     * 
     * @param stack The item stack to insert
     * @param simulate If true, no actual insertion will occur
     * @return The remaining item stack that could not be inserted
     */
    ItemStack insertItem(ItemStack stack, boolean simulate);
    
    /**
     * Attempts to extract an item matching the given filter from the network.
     * 
     * @param filter The item to extract (used as a filter)
     * @param amount The maximum amount to extract
     * @param simulate If true, no actual extraction will occur
     * @return The extracted item stack
     */
    ItemStack extractItem(ItemStack filter, int amount, boolean simulate);
    
    /**
     * Gets a list of all items stored within the network.
     * 
     * @return A list of all stored items
     */
    List<ItemStack> getNetworkContents();
    
    /**
     * Gets the total count of an item in the network.
     * 
     * @param filter The item to count
     * @return The total count
     */
    int getItemCount(ItemStack filter);
    
    /**
     * Updates the state of the network.
     * This should be called periodically to ensure proper functioning.
     * 
     * @param level The world level
     */
    void update(Level level);
}