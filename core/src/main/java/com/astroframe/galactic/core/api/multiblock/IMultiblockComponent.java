package com.astroframe.galactic.core.api.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.UUID;

/**
 * Interface for blocks that can be part of a multiblock structure.
 */
public interface IMultiblockComponent {
    
    /**
     * Gets the position of this component.
     * 
     * @return The component position
     */
    BlockPos getComponentPosition();
    
    /**
     * Gets the structure this component is part of.
     * 
     * @return The structure, or null if not part of any structure
     */
    IMultiblockStructure getStructure();
    
    /**
     * Sets the structure this component is part of.
     * 
     * @param structure The structure to join
     */
    void setStructure(IMultiblockStructure structure);
    
    /**
     * Checks if this component is connected to a structure.
     * 
     * @return Whether this component is part of a structure
     */
    boolean isConnectedToStructure();
    
    /**
     * Gets the structure ID this component is part of.
     * 
     * @return The structure ID, or null if not part of any structure
     */
    UUID getStructureId();
    
    /**
     * Disconnects this component from its structure.
     */
    void disconnectFromStructure();
    
    /**
     * Checks if this component can connect to another component.
     * 
     * @param otherComponent The other component
     * @param direction The connection direction from this component
     * @param level The world level
     * @return Whether a connection is possible
     */
    boolean canConnectTo(IMultiblockComponent otherComponent, Direction direction, Level level);
    
    /**
     * Called when a structure is formed.
     * 
     * @param structure The formed structure
     * @param level The world level
     */
    void onStructureFormed(IMultiblockStructure structure, Level level);
    
    /**
     * Called when a structure is disassembled.
     * 
     * @param structure The disassembled structure
     * @param level The world level
     */
    void onStructureDisassembled(IMultiblockStructure structure, Level level);
    
    /**
     * Gets the role of this component in the structure.
     * 
     * @return The component role
     */
    ComponentRole getComponentRole();
    
    /**
     * Checks if this component is valid as part of its structure.
     * 
     * @param level The world level
     * @return Whether this component is valid
     */
    boolean isValidComponent(Level level);
    
    /**
     * Component roles that determine behavior in multiblock structures.
     */
    enum ComponentRole {
        /** The controller manages the entire structure */
        CONTROLLER,
        /** A core structural component that's required for stability */
        CORE,
        /** An interface component that allows interaction with the structure */
        INTERFACE,
        /** An input component that allows resources to enter the structure */
        INPUT,
        /** An output component that allows resources to exit the structure */
        OUTPUT,
        /** A storage component that holds resources */
        STORAGE,
        /** A processing component that performs operations */
        PROCESSING,
        /** An energy component that handles power */
        ENERGY,
        /** A structural component that forms the frame or walls */
        STRUCTURAL
    }
}