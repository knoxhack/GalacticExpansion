package com.astroframe.galactic.core.api.logistics;

import com.astroframe.galactic.core.api.common.BlockPos;
import com.astroframe.galactic.core.api.common.Direction;
import com.astroframe.galactic.core.api.common.ItemStack;
import com.astroframe.galactic.core.api.common.Level;

import java.util.Set;

/**
 * Interface for blocks that can be connected to an item network.
 */
public interface INetworkNode {
    
    /**
     * Gets the position of this node.
     * 
     * @return The node position
     */
    BlockPos getNodePosition();
    
    /**
     * Gets the network this node is connected to.
     * 
     * @return The connected network, or null if not connected
     */
    IItemNetwork getNetwork();
    
    /**
     * Sets the network this node is connected to.
     * 
     * @param network The network to connect to
     */
    void setNetwork(IItemNetwork network);
    
    /**
     * Checks if this node can connect to networks through the given face.
     * 
     * @param direction The direction to check
     * @return Whether a connection is possible through this face
     */
    boolean canConnectToNetwork(Direction direction);
    
    /**
     * Gets the available connection sides for this network node.
     * 
     * @return A set of directions where network connections are possible
     */
    Set<Direction> getNetworkConnectionSides();
    
    /**
     * Called when a network scan is performed to verify connections.
     * 
     * @param level The world level
     * @return Whether this node is still valid and should remain connected
     */
    boolean validateConnection(Level level);
    
    /**
     * Attempts to insert an item into this node.
     * 
     * @param stack The item stack to insert
     * @param simulate If true, no actual insertion will occur
     * @return The remaining item stack that could not be inserted
     */
    ItemStack insertItem(ItemStack stack, boolean simulate);
    
    /**
     * Attempts to extract an item matching the given filter from this node.
     * 
     * @param filter The item to extract (used as a filter)
     * @param amount The maximum amount to extract
     * @param simulate If true, no actual extraction will occur
     * @return The extracted item stack
     */
    ItemStack extractItem(ItemStack filter, int amount, boolean simulate);
    
    /**
     * Gets the priority of this node in the network.
     * Higher priority nodes are preferred for insertion/extraction operations.
     * 
     * @return The node priority
     */
    int getNodePriority();
    
    /**
     * Sets the priority of this node in the network.
     * 
     * @param priority The new priority
     */
    void setNodePriority(int priority);
    
    /**
     * Gets the node type, which determines its behavior in the network.
     * 
     * @return The node type
     */
    NodeType getNodeType();
    
    /**
     * Node types that determine behavior in the network.
     */
    enum NodeType {
        /** A storage node that can store items */
        STORAGE,
        /** An input node that can only insert items into the network */
        INPUT,
        /** An output node that can only extract items from the network */
        OUTPUT,
        /** A connector node that only passes items through */
        CONNECTOR,
        /** A controller node that manages the network */
        CONTROLLER,
        /** A crafting node that can request item crafting */
        CRAFTING,
        /** A buffer node that temporarily holds items during transit */
        BUFFER,
        /** A monitor node that displays network contents */
        MONITOR
    }
}