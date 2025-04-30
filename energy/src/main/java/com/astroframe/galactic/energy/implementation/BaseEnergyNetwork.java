package com.astroframe.galactic.energy.implementation;

import com.astroframe.galactic.energy.api.EnergyNetwork;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyTransferResult;
import com.astroframe.galactic.energy.api.EnergyType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Basic implementation of the EnergyNetwork interface.
 * This provides a simple network for connecting energy storage devices.
 */
public class BaseEnergyNetwork implements EnergyNetwork {
    
    private final ResourceLocation networkId;
    private final EnergyType energyType;
    private final Level level;
    private final Map<BlockPos, EnergyStorage> nodeStorages = new HashMap<>();
    private final Set<EnergyStorage> storageDevices = new HashSet<>();
    
    /**
     * Create a new energy network.
     * 
     * @param networkId The unique identifier for this network
     * @param level The level (dimension) this network exists in
     * @param energyType The type of energy this network handles
     */
    public BaseEnergyNetwork(ResourceLocation networkId, Level level, EnergyType energyType) {
        this.networkId = networkId;
        this.level = level;
        this.energyType = energyType;
    }
    
    @Override
    public ResourceLocation getId() {
        return networkId;
    }
    
    @Override
    public EnergyType getEnergyType() {
        return energyType;
    }
    
    @Override
    public Level getLevel() {
        return level;
    }
    
    @Override
    public boolean addStorage(EnergyStorage storage) {
        if (storage.getEnergyType() != energyType) {
            return false;
        }
        
        return storageDevices.add(storage);
    }
    
    @Override
    public boolean removeStorage(EnergyStorage storage) {
        return storageDevices.remove(storage);
    }
    
    @Override
    public Collection<EnergyStorage> getStorageDevices() {
        return storageDevices;
    }
    
    @Override
    public int addEnergy(int amount, boolean simulate) {
        // Find storages that can receive energy
        Set<EnergyStorage> receivers = new HashSet<>();
        for (EnergyStorage storage : storageDevices) {
            if (storage.canReceive()) {
                receivers.add(storage);
            }
        }
        
        if (receivers.isEmpty()) {
            return 0;
        }
        
        // Calculate how much energy each storage should receive
        int energyPerStorage = amount / receivers.size();
        int remainder = amount % receivers.size();
        
        // Track how much energy was actually added
        int energyAdded = 0;
        
        // Distribute energy evenly
        for (EnergyStorage storage : receivers) {
            int energyToAdd = energyPerStorage;
            if (remainder > 0) {
                energyToAdd++;
                remainder--;
            }
            
            energyAdded += storage.receiveEnergy(energyToAdd, simulate);
        }
        
        return energyAdded;
    }
    
    @Override
    public int extractEnergy(int amount, boolean simulate) {
        // Find storages that can provide energy
        Set<EnergyStorage> providers = new HashSet<>();
        for (EnergyStorage storage : storageDevices) {
            if (storage.canExtract()) {
                providers.add(storage);
            }
        }
        
        if (providers.isEmpty()) {
            return 0;
        }
        
        // Calculate how much energy each storage should provide
        int energyPerStorage = amount / providers.size();
        int remainder = amount % providers.size();
        
        // Track how much energy was actually extracted
        int energyExtracted = 0;
        
        // Extract energy evenly
        for (EnergyStorage storage : providers) {
            int energyToExtract = energyPerStorage;
            if (remainder > 0) {
                energyToExtract++;
                remainder--;
            }
            
            energyExtracted += storage.extractEnergy(energyToExtract, simulate);
        }
        
        return energyExtracted;
    }
    
    @Override
    public int getEnergy() {
        int totalEnergy = 0;
        
        for (EnergyStorage storage : storageDevices) {
            totalEnergy += storage.getEnergy();
        }
        
        return totalEnergy;
    }
    
    @Override
    public int getMaxEnergy() {
        int totalCapacity = 0;
        
        for (EnergyStorage storage : storageDevices) {
            totalCapacity += storage.getMaxEnergy();
        }
        
        return totalCapacity;
    }
    
    @Override
    public boolean addNode(BlockPos pos, EnergyStorage storage) {
        if (storage.getEnergyType() != energyType) {
            return false;
        }
        
        if (nodeStorages.containsKey(pos)) {
            return false;
        }
        
        nodeStorages.put(pos, storage);
        return true;
    }
    
    @Override
    public boolean removeNode(BlockPos pos) {
        return nodeStorages.remove(pos) != null;
    }
    
    @Override
    public Collection<BlockPos> getNodes() {
        return nodeStorages.keySet();
    }
    
    @Override
    public EnergyStorage getNodeStorage(BlockPos pos) {
        return nodeStorages.get(pos);
    }
    
    @Override
    public EnergyTransferResult transferEnergy(BlockPos source, BlockPos destination, int amount, boolean simulate) {
        // Check if both nodes exist
        EnergyStorage sourceStorage = getNodeStorage(source);
        EnergyStorage destStorage = getNodeStorage(destination);
        
        if (sourceStorage == null) {
            return new EnergyTransferResult(0, "Source node does not exist", EnergyTransferResult.Status.INVALID_SOURCE);
        }
        
        if (destStorage == null) {
            return new EnergyTransferResult(0, "Destination node does not exist", EnergyTransferResult.Status.INVALID_DESTINATION);
        }
        
        // Check if source can extract
        if (!sourceStorage.canExtract()) {
            return new EnergyTransferResult(0, "Source cannot extract energy", EnergyTransferResult.Status.SOURCE_CANNOT_EXTRACT);
        }
        
        // Check if destination can receive
        if (!destStorage.canReceive()) {
            return new EnergyTransferResult(0, "Destination cannot receive energy", EnergyTransferResult.Status.DESTINATION_CANNOT_RECEIVE);
        }
        
        // Simulate extraction from source
        int extractable = sourceStorage.extractEnergy(amount, true);
        if (extractable <= 0) {
            return new EnergyTransferResult(0, "Source has no energy to extract", EnergyTransferResult.Status.SOURCE_EMPTY);
        }
        
        // Simulate reception by destination
        int receivable = destStorage.receiveEnergy(extractable, true);
        if (receivable <= 0) {
            return new EnergyTransferResult(0, "Destination cannot accept any energy", EnergyTransferResult.Status.DESTINATION_FULL);
        }
        
        if (simulate) {
            return new EnergyTransferResult(receivable, "Simulated transfer", EnergyTransferResult.Status.SUCCESS);
        }
        
        // Actually transfer the energy
        int extracted = sourceStorage.extractEnergy(receivable, false);
        int received = destStorage.receiveEnergy(extracted, false);
        
        if (extracted != received) {
            // This should never happen under normal circumstances
            return new EnergyTransferResult(received, "Transfer mismatch detected", EnergyTransferResult.Status.WARNING);
        }
        
        return new EnergyTransferResult(received, "Energy transferred successfully", EnergyTransferResult.Status.SUCCESS);
    }
    
    @Override
    public void onChunkStatusChange(ChunkPos chunkPos, boolean loaded) {
        // In a more advanced implementation, we might disable nodes in unloaded chunks
        // or invalidate paths that go through unloaded chunks
    }
    
    @Override
    public void tick() {
        // In a real implementation, this would handle energy transfer between
        // sources, consumers, and storage based on demands and priorities
        // For this example, we'll just balance energy between storage devices
        balanceEnergy();
    }
    
    /**
     * Balance energy between network nodes.
     * This attempts to distribute energy evenly across all node storage devices.
     */
    private void balanceEnergy() {
        Collection<BlockPos> nodes = getNodes();
        if (nodes.size() <= 1) {
            return;
        }
        
        // Calculate the target energy level (as a percentage of capacity)
        float totalEnergy = getEnergy();
        float totalCapacity = getMaxEnergy();
        float targetFillLevel = totalCapacity > 0 ? totalEnergy / totalCapacity : 0;
        
        // Balance energy between node storage devices
        for (BlockPos pos : nodes) {
            EnergyStorage storage = getNodeStorage(pos);
            if (storage == null) {
                continue;
            }
            
            float currentFillLevel = storage.getFillLevel();
            
            if (Math.abs(currentFillLevel - targetFillLevel) < 0.01f) {
                // Already close enough to the target
                continue;
            }
            
            int targetEnergy = Math.round(storage.getMaxEnergy() * targetFillLevel);
            int currentEnergy = storage.getEnergy();
            int delta = targetEnergy - currentEnergy;
            
            if (delta > 0 && storage.canReceive()) {
                // Need to add energy
                storage.receiveEnergy(delta, false);
            } else if (delta < 0 && storage.canExtract()) {
                // Need to remove energy
                storage.extractEnergy(-delta, false);
            }
        }
        
        // For backward compatibility, also balance legacy storage devices
        if (storageDevices.size() > 1) {
            for (EnergyStorage storage : storageDevices) {
                float currentFillLevel = storage.getFillLevel();
                
                if (Math.abs(currentFillLevel - targetFillLevel) < 0.01f) {
                    // Already close enough to the target
                    continue;
                }
                
                int targetEnergy = Math.round(storage.getMaxEnergy() * targetFillLevel);
                int currentEnergy = storage.getEnergy();
                int delta = targetEnergy - currentEnergy;
                
                if (delta > 0 && storage.canReceive()) {
                    // Need to add energy
                    storage.receiveEnergy(delta, false);
                } else if (delta < 0 && storage.canExtract()) {
                    // Need to remove energy
                    storage.extractEnergy(-delta, false);
                }
            }
        }
    }
}