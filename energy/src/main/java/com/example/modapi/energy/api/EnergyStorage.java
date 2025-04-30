package com.example.modapi.energy.api;

import net.minecraft.nbt.CompoundTag;

/**
 * Base implementation of an energy storage.
 * Stores energy with a maximum capacity, input and output rates.
 */
public class EnergyStorage implements IEnergyHandler {
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;
    protected EnergyUnit unit;

    /**
     * Constructor for EnergyStorage.
     * 
     * @param capacity The maximum energy capacity
     * @param maxReceive The maximum input rate
     * @param maxExtract The maximum output rate
     * @param energy The initial energy amount
     * @param unit The energy unit
     */
    public EnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, EnergyUnit unit) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0, Math.min(capacity, energy));
        this.unit = unit;
    }

    /**
     * Creates a new energy storage with default values.
     * 
     * @param capacity The maximum capacity
     * @return A new energy storage
     */
    public static EnergyStorage create(int capacity) {
        return new EnergyStorage(capacity, capacity, capacity, 0, EnergyUnit.FORGE_ENERGY);
    }

    /**
     * Creates a new energy storage with custom receive and extract rates.
     * 
     * @param capacity The maximum capacity
     * @param maxReceive The maximum input rate
     * @param maxExtract The maximum output rate
     * @return A new energy storage
     */
    public static EnergyStorage create(int capacity, int maxReceive, int maxExtract) {
        return new EnergyStorage(capacity, maxReceive, maxExtract, 0, EnergyUnit.FORGE_ENERGY);
    }

    /**
     * Creates a new energy storage with custom receive and extract rates and initial energy.
     * 
     * @param capacity The maximum capacity
     * @param maxReceive The maximum input rate
     * @param maxExtract The maximum output rate
     * @param energy The initial energy amount
     * @return A new energy storage
     */
    public static EnergyStorage create(int capacity, int maxReceive, int maxExtract, int energy) {
        return new EnergyStorage(capacity, maxReceive, maxExtract, energy, EnergyUnit.FORGE_ENERGY);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    @Override
    public EnergyUnit getEnergyUnit() {
        return unit;
    }

    /**
     * Serializes the energy storage to NBT.
     * 
     * @param nbt The NBT compound to write to
     * @return The NBT compound
     */
    public CompoundTag serializeNBT(CompoundTag nbt) {
        nbt.putInt("Energy", energy);
        nbt.putInt("Capacity", capacity);
        nbt.putInt("MaxReceive", maxReceive);
        nbt.putInt("MaxExtract", maxExtract);
        nbt.putString("Unit", unit.name());
        return nbt;
    }

    /**
     * Deserializes the energy storage from NBT.
     * 
     * @param nbt The NBT compound to read from
     */
    public void deserializeNBT(CompoundTag nbt) {
        energy = nbt.getInt("Energy");
        capacity = nbt.getInt("Capacity");
        maxReceive = nbt.getInt("MaxReceive");
        maxExtract = nbt.getInt("MaxExtract");
        try {
            unit = EnergyUnit.valueOf(nbt.getString("Unit"));
        } catch (IllegalArgumentException e) {
            unit = EnergyUnit.FORGE_ENERGY;
        }
    }
}
