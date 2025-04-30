package com.example.modapi.energy.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.*;

/**
 * Network system for managing energy transfer between connected energy handlers.
 * Tracks connected machines and distributes energy efficiently.
 */
public class EnergyNetwork {
    private final Set<BlockPos> connectedBlocks = new HashSet<>();
    private final Map<BlockPos, IEnergyHandler> energyHandlers = new HashMap<>();
    private final UUID networkId;
    private int transferRate = 1000;
    
    /**
     * Constructor for EnergyNetwork.
     */
    public EnergyNetwork() {
        this.networkId = UUID.randomUUID();
    }
    
    /**
     * Constructor for EnergyNetwork with custom ID.
     * 
     * @param networkId The network ID
     */
    public EnergyNetwork(UUID networkId) {
        this.networkId = networkId;
    }
    
    /**
     * Gets the unique ID of this network.
     * 
     * @return The network ID
     */
    public UUID getNetworkId() {
        return networkId;
    }
    
    /**
     * Sets the energy transfer rate for this network.
     * 
     * @param transferRate The maximum transfer rate per tick
     * @return This network for chaining
     */
    public EnergyNetwork setTransferRate(int transferRate) {
        this.transferRate = transferRate;
        return this;
    }
    
    /**
     * Gets the energy transfer rate for this network.
     * 
     * @return The transfer rate
     */
    public int getTransferRate() {
        return transferRate;
    }
    
    /**
     * Add a block to the network.
     * 
     * @param level The world
     * @param pos The block position
     * @return Whether the block was added successfully
     */
    public boolean addBlock(Level level, BlockPos pos) {
        if (connectedBlocks.contains(pos)) {
            return false; // Already in the network
        }
        
        // Check if the block has an energy handler
        if (level.getBlockEntity(pos) instanceof IEnergyHandler energyHandler) {
            connectedBlocks.add(pos);
            energyHandlers.put(pos, energyHandler);
            return true;
        }
        
        return false;
    }
    
    /**
     * Remove a block from the network.
     * 
     * @param pos The block position
     * @return Whether the block was removed
     */
    public boolean removeBlock(BlockPos pos) {
        if (connectedBlocks.remove(pos)) {
            energyHandlers.remove(pos);
            return true;
        }
        return false;
    }
    
    /**
     * Checks if a block is part of this network.
     * 
     * @param pos The block position
     * @return Whether the block is in the network
     */
    public boolean containsBlock(BlockPos pos) {
        return connectedBlocks.contains(pos);
    }
    
    /**
     * Gets all blocks in this network.
     * 
     * @return An unmodifiable set of block positions
     */
    public Set<BlockPos> getConnectedBlocks() {
        return Collections.unmodifiableSet(connectedBlocks);
    }
    
    /**
     * Gets all energy handlers in this network.
     * 
     * @return An unmodifiable map of positions to energy handlers
     */
    public Map<BlockPos, IEnergyHandler> getEnergyHandlers() {
        return Collections.unmodifiableMap(energyHandlers);
    }
    
    /**
     * Distributes energy from providers to consumers.
     * Called every tick to balance energy in the network.
     */
    public void distributeEnergy() {
        // Find energy providers and consumers
        List<Map.Entry<BlockPos, IEnergyHandler>> providers = new ArrayList<>();
        List<Map.Entry<BlockPos, IEnergyHandler>> consumers = new ArrayList<>();
        
        for (Map.Entry<BlockPos, IEnergyHandler> entry : energyHandlers.entrySet()) {
            IEnergyHandler handler = entry.getValue();
            
            if (handler.canExtract() && handler.getEnergyStored() > 0) {
                providers.add(entry);
            }
            
            if (handler.canReceive() && handler.getEnergyStored() < handler.getMaxEnergyStored()) {
                consumers.add(entry);
            }
        }
        
        // Nothing to do if no providers or consumers
        if (providers.isEmpty() || consumers.isEmpty()) {
            return;
        }
        
        // Distribute energy from each provider
        for (Map.Entry<BlockPos, IEnergyHandler> providerEntry : providers) {
            IEnergyHandler provider = providerEntry.getValue();
            
            // Calculate available energy to distribute
            int availableEnergy = Math.min(provider.getEnergyStored(), transferRate);
            if (availableEnergy <= 0) continue;
            
            // Calculate energy per consumer
            int consumersCount = consumers.size();
            int energyPerConsumer = Math.max(1, availableEnergy / consumersCount);
            
            // Distribute to each consumer
            int totalExtracted = 0;
            
            for (Map.Entry<BlockPos, IEnergyHandler> consumerEntry : consumers) {
                IEnergyHandler consumer = consumerEntry.getValue();
                
                // Skip if the consumer is full
                if (consumer.getEnergyStored() >= consumer.getMaxEnergyStored()) {
                    continue;
                }
                
                // Calculate how much energy this consumer can accept
                int toTransfer = Math.min(energyPerConsumer, availableEnergy - totalExtracted);
                
                // Convert energy if units differ
                if (provider.getEnergyUnit() != consumer.getEnergyUnit()) {
                    toTransfer = provider.getEnergyUnit().convertTo(toTransfer, consumer.getEnergyUnit());
                }
                
                // Extract from provider (simulate first)
                int extracted = provider.extractEnergy(toTransfer, true);
                if (extracted <= 0) continue;
                
                // Try to send to the consumer
                int accepted = consumer.receiveEnergy(extracted, true);
                if (accepted <= 0) continue;
                
                // Actually perform the transfer
                provider.extractEnergy(accepted, false);
                consumer.receiveEnergy(accepted, false);
                
                totalExtracted += accepted;
                
                // Stop if we've distributed all available energy
                if (totalExtracted >= availableEnergy) {
                    break;
                }
            }
        }
    }
    
    /**
     * Merges another network into this one.
     * 
     * @param other The other network to merge
     * @return This network with the merged blocks
     */
    public EnergyNetwork merge(EnergyNetwork other) {
        if (other == this) return this;
        
        // Add all blocks from the other network
        connectedBlocks.addAll(other.connectedBlocks);
        energyHandlers.putAll(other.energyHandlers);
        
        // Use the higher transfer rate
        transferRate = Math.max(transferRate, other.transferRate);
        
        return this;
    }
    
    /**
     * Splits this network into multiple networks based on connectivity.
     * 
     * @param level The world
     * @return A list of new networks
     */
    public List<EnergyNetwork> split(Level level) {
        List<EnergyNetwork> networks = new ArrayList<>();
        Set<BlockPos> remainingBlocks = new HashSet<>(connectedBlocks);
        
        while (!remainingBlocks.isEmpty()) {
            // Start a new network with the first remaining block
            BlockPos startPos = remainingBlocks.iterator().next();
            EnergyNetwork newNetwork = new EnergyNetwork();
            newNetwork.setTransferRate(this.transferRate);
            
            // Find all connected blocks using breadth-first search
            Set<BlockPos> connectedToStart = findConnectedBlocks(level, startPos);
            
            // Add all connected blocks to the new network
            for (BlockPos pos : connectedToStart) {
                if (energyHandlers.containsKey(pos)) {
                    newNetwork.addBlock(level, pos);
                }
            }
            
            networks.add(newNetwork);
            remainingBlocks.removeAll(connectedToStart);
        }
        
        return networks;
    }
    
    /**
     * Find all blocks connected to a starting position.
     * 
     * @param level The world
     * @param startPos The starting position
     * @return A set of connected block positions
     */
    private Set<BlockPos> findConnectedBlocks(Level level, BlockPos startPos) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        
        queue.add(startPos);
        visited.add(startPos);
        
        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            
            // Check all adjacent positions
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        // Skip diagonals
                        if (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) != 1) continue;
                        
                        BlockPos neighbor = current.offset(dx, dy, dz);
                        
                        // If this neighbor is in our network and not visited yet
                        if (connectedBlocks.contains(neighbor) && !visited.contains(neighbor)) {
                            queue.add(neighbor);
                            visited.add(neighbor);
                        }
                    }
                }
            }
        }
        
        return visited;
    }
}
