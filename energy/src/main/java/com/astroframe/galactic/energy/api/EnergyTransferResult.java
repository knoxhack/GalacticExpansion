package com.astroframe.galactic.energy.api;

/**
 * Represents the result of an energy transfer operation.
 * Provides detailed information about the success, amount transferred, and any error messages.
 */
public class EnergyTransferResult {

    private final boolean success;
    private final int amount;
    private final String message;

    /**
     * Creates a new energy transfer result.
     *
     * @param success Whether the transfer was successful
     * @param amount The amount of energy that was transferred
     * @param message A descriptive message about the transfer (especially useful for errors)
     */
    public EnergyTransferResult(boolean success, int amount, String message) {
        this.success = success;
        this.amount = amount;
        this.message = message;
    }

    /**
     * Gets whether the transfer was successful.
     *
     * @return true if the transfer was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Gets the amount of energy that was transferred.
     * This will be 0 if the transfer was not successful.
     *
     * @return The amount of energy transferred
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Gets a descriptive message about the transfer.
     * This is especially useful for error messages.
     *
     * @return The message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Creates a successful transfer result with the specified amount.
     *
     * @param amount The amount of energy transferred
     * @return A new EnergyTransferResult instance
     */
    public static EnergyTransferResult success(int amount) {
        return new EnergyTransferResult(true, amount, "Transfer successful");
    }
    
    /**
     * Creates a failed transfer result with the specified error message.
     *
     * @param errorMessage The error message
     * @return A new EnergyTransferResult instance
     */
    public static EnergyTransferResult failure(String errorMessage) {
        return new EnergyTransferResult(false, 0, errorMessage);
    }
    
    /**
     * Creates a partial transfer result.
     *
     * @param amount The partial amount transferred
     * @param reason The reason for partial transfer
     * @return A new EnergyTransferResult instance
     */
    public static EnergyTransferResult partial(int amount, String reason) {
        return new EnergyTransferResult(true, amount, "Partial transfer: " + reason);
    }
}