package com.astroframe.galactic.energy.api;

/**
 * Represents the result of an energy transfer operation.
 * This provides detailed information about the success or failure of the operation.
 */
public class EnergyTransferResult {
    
    private final int energyTransferred;
    private final String message;
    private final Status status;
    
    /**
     * Create a new energy transfer result.
     * 
     * @param energyTransferred The amount of energy that was transferred
     * @param message A message describing the result
     * @param status The status of the operation
     */
    public EnergyTransferResult(int energyTransferred, String message, Status status) {
        this.energyTransferred = energyTransferred;
        this.message = message;
        this.status = status;
    }
    
    /**
     * Get the amount of energy that was transferred.
     * 
     * @return The energy transferred
     */
    public int getEnergyTransferred() {
        return energyTransferred;
    }
    
    /**
     * Get a message describing the result.
     * 
     * @return The message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Get the status of the operation.
     * 
     * @return The status
     */
    public Status getStatus() {
        return status;
    }
    
    /**
     * Check if the operation was successful.
     * 
     * @return true if the operation succeeded, false otherwise
     */
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }
    
    /**
     * Status codes for energy transfer operations.
     */
    public enum Status {
        /** The operation succeeded */
        SUCCESS,
        
        /** A warning occurred, but the operation completed */
        WARNING,
        
        /** The source node does not exist */
        INVALID_SOURCE,
        
        /** The destination node does not exist */
        INVALID_DESTINATION,
        
        /** The source node cannot extract energy */
        SOURCE_CANNOT_EXTRACT,
        
        /** The destination node cannot receive energy */
        DESTINATION_CANNOT_RECEIVE,
        
        /** The source has no energy to extract */
        SOURCE_EMPTY,
        
        /** The destination is full and cannot accept more energy */
        DESTINATION_FULL,
        
        /** The energy types are incompatible */
        INCOMPATIBLE_ENERGY_TYPES,
        
        /** The path between nodes is blocked or invalid */
        PATH_BLOCKED,
        
        /** The transfer exceeds the network's capacity */
        CAPACITY_EXCEEDED,
        
        /** An unknown error occurred */
        UNKNOWN_ERROR
    }
}