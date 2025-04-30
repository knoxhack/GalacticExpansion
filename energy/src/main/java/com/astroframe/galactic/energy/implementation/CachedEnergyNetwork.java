package com.astroframe.galactic.energy.implementation;

import com.astroframe.galactic.energy.api.EnergyNetwork;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyTransferResult;
import com.astroframe.galactic.energy.api.EnergyType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * An optimized implementation of EnergyNetwork that caches network paths and
 * manages energy transfers efficiently. This implementation improves performance
 * by reducing repeated calculations for frequently accessed paths in the network.
 * It also provides chunk-based management to handle unloaded chunks properly.
 */
public class CachedEnergyNetwork implements EnergyNetwork {

    private final ResourceLocation id;
    private final EnergyType energyType;
    private final Level level;
    private final Map<BlockPos, EnergyStorage> nodes = new HashMap<>();
    
    // Cache for network paths (source → destination)
    private final Map<CachedPathKey, List<BlockPos>> pathCache = new ConcurrentHashMap<>();
    
    // Expiration time for cached paths (in milliseconds)
    private final Map<CachedPathKey, Long> pathExpirations = new ConcurrentHashMap<>();
    private final long pathCacheExpiry = TimeUnit.MINUTES.toMillis(5);
    
    // Chunk tracking for optimization
    private final Map<ChunkPos, Set<BlockPos>> chunkToNodesMap = new HashMap<>();
    
    // Transfer rate limiting for stability
    private final Map<BlockPos, Long> lastTransferTimes = new HashMap<>();
    private final Map<BlockPos, Double> transferRateLimits = new HashMap<>();
    private final Map<BlockPos, Double> currentPeriodTransferred = new HashMap<>();
    private final long rateLimitWindow = TimeUnit.SECONDS.toMillis(1);
    
    /**
     * Constructor for CachedEnergyNetwork
     * 
     * @param id The network identifier
     * @param energyType The energy type this network handles
     * @param level The level/dimension this network exists in
     */
    public CachedEnergyNetwork(ResourceLocation id, EnergyType energyType, Level level) {
        this.id = id;
        this.energyType = energyType;
        this.level = level;
    }

    @Override
    public ResourceLocation getId() {
        return id;
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
    public synchronized boolean addNode(BlockPos pos, EnergyStorage storage) {
        if (nodes.containsKey(pos)) {
            return false;
        }
        
        nodes.put(pos, storage);
        
        // Add to chunk tracking
        ChunkPos chunkPos = new ChunkPos(pos);
        chunkToNodesMap.computeIfAbsent(chunkPos, k -> new HashSet<>()).add(pos);
        
        // Invalidate any cached paths that might be affected
        invalidateCachedPaths();
        
        return true;
    }

    @Override
    public synchronized boolean removeNode(BlockPos pos) {
        if (!nodes.containsKey(pos)) {
            return false;
        }
        
        nodes.remove(pos);
        
        // Remove from chunk tracking
        ChunkPos chunkPos = new ChunkPos(pos);
        Set<BlockPos> chunkNodes = chunkToNodesMap.get(chunkPos);
        if (chunkNodes != null) {
            chunkNodes.remove(pos);
            if (chunkNodes.isEmpty()) {
                chunkToNodesMap.remove(chunkPos);
            }
        }
        
        // Clean up rate limit data
        lastTransferTimes.remove(pos);
        transferRateLimits.remove(pos);
        currentPeriodTransferred.remove(pos);
        
        // Invalidate any cached paths that might be affected
        invalidateCachedPaths();
        
        return true;
    }

    @Override
    public Collection<BlockPos> getNodes() {
        return new ArrayList<>(nodes.keySet());
    }

    @Override
    public EnergyStorage getNodeStorage(BlockPos pos) {
        return nodes.get(pos);
    }

    @Override
    public synchronized EnergyTransferResult transferEnergy(BlockPos source, BlockPos destination, int amount, boolean simulate) {
        // Check if the nodes exist
        EnergyStorage sourceStorage = nodes.get(source);
        EnergyStorage destStorage = nodes.get(destination);
        
        if (sourceStorage == null || destStorage == null) {
            return new EnergyTransferResult(false, 0, "One or both nodes do not exist in the network");
        }
        
        // Check if the chunks are loaded
        ChunkPos sourceChunk = new ChunkPos(source);
        ChunkPos destChunk = new ChunkPos(destination);
        
        if (!isChunkLoaded(sourceChunk) || !isChunkLoaded(destChunk)) {
            return new EnergyTransferResult(false, 0, "One or both nodes are in unloaded chunks");
        }
        
        // Apply rate limiting if not simulating
        if (!simulate) {
            long currentTime = System.currentTimeMillis();
            double rateLimit = transferRateLimits.getOrDefault(source, Double.MAX_VALUE);
            
            // Reset rate limit tracking for new periods
            Long lastTransfer = lastTransferTimes.get(source);
            if (lastTransfer == null || currentTime - lastTransfer > rateLimitWindow) {
                currentPeriodTransferred.put(source, 0.0);
            }
            
            // Check if transfer would exceed rate limit
            double currentTransferred = currentPeriodTransferred.getOrDefault(source, 0.0);
            if (currentTransferred + amount > rateLimit) {
                int allowed = (int)(rateLimit - currentTransferred);
                if (allowed <= 0) {
                    return new EnergyTransferResult(false, 0, "Rate limit exceeded for source node");
                }
                amount = allowed;
            }
        }
        
        // Find a path between the nodes
        List<BlockPos> path = findPath(source, destination);
        if (path.isEmpty()) {
            return new EnergyTransferResult(false, 0, "No valid path between nodes");
        }
        
        // Calculate the maximum transferable amount
        int maxExtract = sourceStorage.extractEnergy(amount, true);
        if (maxExtract <= 0) {
            return new EnergyTransferResult(false, 0, "Source cannot extract energy");
        }
        
        int maxReceive = destStorage.receiveEnergy(maxExtract, true);
        if (maxReceive <= 0) {
            return new EnergyTransferResult(false, 0, "Destination cannot receive energy");
        }
        
        int transferAmount = Math.min(maxExtract, maxReceive);
        
        // If we're just simulating, return the calculated amount
        if (simulate) {
            return new EnergyTransferResult(true, transferAmount, "Simulated transfer successful");
        }
        
        // Perform the actual transfer
        int extracted = sourceStorage.extractEnergy(transferAmount, false);
        int received = destStorage.receiveEnergy(extracted, false);
        
        // Update rate limit tracking
        long currentTime = System.currentTimeMillis();
        lastTransferTimes.put(source, currentTime);
        double currentTransferred = currentPeriodTransferred.getOrDefault(source, 0.0);
        currentPeriodTransferred.put(source, currentTransferred + received);
        
        if (extracted != received) {
            // This should not happen with proper implementations, but handle it anyway
            if (received < extracted) {
                // Put back the energy that wasn't received
                sourceStorage.receiveEnergy(extracted - received, false);
            }
            return new EnergyTransferResult(true, received, "Partial transfer: extracted " + extracted + ", received " + received);
        }
        
        return new EnergyTransferResult(true, received, "Transfer successful");
    }

    /**
     * Sets a transfer rate limit for a specific node
     * 
     * @param pos The node position
     * @param ratePerSecond Maximum energy units that can be transferred per second
     */
    public void setTransferRateLimit(BlockPos pos, double ratePerSecond) {
        if (nodes.containsKey(pos)) {
            transferRateLimits.put(pos, ratePerSecond);
        }
    }
    
    /**
     * Finds a path between two nodes, using the cache if available
     * 
     * @param source The source node
     * @param destination The destination node
     * @return A list of BlockPos representing the path, or empty list if no path exists
     */
    private List<BlockPos> findPath(BlockPos source, BlockPos destination) {
        // Check cache first
        CachedPathKey key = new CachedPathKey(source, destination);
        
        // Clean expired cache entries periodically
        cleanExpiredPathCache();
        
        // Return from cache if valid
        Long expiration = pathExpirations.get(key);
        if (expiration != null && System.currentTimeMillis() < expiration) {
            List<BlockPos> cachedPath = pathCache.get(key);
            if (cachedPath != null) {
                return new ArrayList<>(cachedPath);
            }
        }
        
        // If not in cache or expired, calculate the path
        List<BlockPos> path = calculatePath(source, destination);
        
        // Cache the result if we found a path
        if (!path.isEmpty()) {
            pathCache.put(key, new ArrayList<>(path));
            pathExpirations.put(key, System.currentTimeMillis() + pathCacheExpiry);
        }
        
        return path;
    }
    
    /**
     * Calculate the shortest path between two nodes
     * This uses a simple breadth-first search for now
     * 
     * @param source The source node
     * @param destination The destination node
     * @return A list of BlockPos representing the path, or empty list if no path
     */
    private List<BlockPos> calculatePath(BlockPos source, BlockPos destination) {
        if (source.equals(destination)) {
            return List.of(source);
        }
        
        Queue<BlockPos> queue = new LinkedList<>();
        Map<BlockPos, BlockPos> cameFrom = new HashMap<>();
        Set<BlockPos> visited = new HashSet<>();
        
        queue.add(source);
        visited.add(source);
        
        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            
            if (current.equals(destination)) {
                // Found the destination, reconstruct the path
                return reconstructPath(cameFrom, source, destination);
            }
            
            // For simplicity, just check 6 adjacent blocks
            // In a real implementation, you'd want to check for valid connections
            for (BlockPos neighbor : getAdjacentPositions(current)) {
                if (nodes.containsKey(neighbor) && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    cameFrom.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        
        // No path found
        return List.of();
    }
    
    /**
     * Reconstructs the path from the cameFrom map
     */
    private List<BlockPos> reconstructPath(Map<BlockPos, BlockPos> cameFrom, BlockPos source, BlockPos destination) {
        List<BlockPos> path = new ArrayList<>();
        BlockPos current = destination;
        
        while (!current.equals(source)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        
        path.add(source);
        Collections.reverse(path);
        return path;
    }
    
    /**
     * Gets all adjacent positions (six directions)
     */
    private List<BlockPos> getAdjacentPositions(BlockPos pos) {
        return List.of(
            pos.above(),
            pos.below(),
            pos.north(),
            pos.south(),
            pos.east(),
            pos.west()
        );
    }
    
    /**
     * Checks if a chunk is loaded
     */
    private boolean isChunkLoaded(ChunkPos pos) {
        return level.hasChunk(pos.x, pos.z);
    }
    
    /**
     * Removes all expired entries from the path cache
     */
    private void cleanExpiredPathCache() {
        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<CachedPathKey, Long>> iterator = pathExpirations.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<CachedPathKey, Long> entry = iterator.next();
            if (currentTime > entry.getValue()) {
                pathCache.remove(entry.getKey());
                iterator.remove();
            }
        }
    }
    
    /**
     * Invalidates all cached paths
     */
    private void invalidateCachedPaths() {
        pathCache.clear();
        pathExpirations.clear();
    }
    
    /**
     * Updates the network when chunks are loaded or unloaded
     * 
     * @param chunkPos The chunk position
     * @param loaded Whether the chunk is being loaded (true) or unloaded (false)
     */
    public void onChunkStatusChange(ChunkPos chunkPos, boolean loaded) {
        // When a chunk is unloaded, we can temporarily ignore nodes in that chunk
        // When it's loaded again, we need to recalculate paths that might involve it
        if (!loaded || !chunkToNodesMap.containsKey(chunkPos)) {
            return;
        }
        
        // When a chunk is loaded, invalidate paths that might now be possible
        invalidateCachedPaths();
    }
    
    /**
     * Key class for the path cache
     */
    private static class CachedPathKey {
        private final BlockPos source;
        private final BlockPos destination;
        
        public CachedPathKey(BlockPos source, BlockPos destination) {
            this.source = source;
            this.destination = destination;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CachedPathKey that = (CachedPathKey) o;
            return source.equals(that.source) && destination.equals(that.destination);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(source, destination);
        }
    }
}