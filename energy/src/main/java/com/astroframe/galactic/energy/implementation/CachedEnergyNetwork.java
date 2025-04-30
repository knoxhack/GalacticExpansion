package com.astroframe.galactic.energy.implementation;

import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.api.IEnergyHandler;
import com.astroframe.galactic.energy.api.energynetwork.WorldChunk;
import com.astroframe.galactic.energy.api.energynetwork.WorldPosition;
import net.minecraft.world.level.Level;

import java.util.*;

/**
 * An energy network implementation that caches paths between nodes.
 * This provides more efficient energy distribution by avoiding recalculating paths.
 */
public class CachedEnergyNetwork extends BaseEnergyNetwork {
    
    private final Map<CachedPathKey, List<WorldPosition>> pathCache = new HashMap<>();
    private final Map<CachedPathKey, Long> pathExpirations = new HashMap<>();
    
    // Path cache expiration time (in milliseconds)
    private static final long PATH_CACHE_EXPIRATION = 30_000; // 30 seconds
    
    // Reference to the level for chunk status checking
    private final Level level;
    
    /**
     * Creates a new CachedEnergyNetwork.
     * 
     * @param energyType The energy type
     * @param level The level (world)
     */
    public CachedEnergyNetwork(EnergyType energyType, Level level) {
        super(energyType);
        this.level = level;
    }
    
    @Override
    public int distributeEnergy() {
        int totalTransferred = 0;
        List<WorldPosition> sources = new ArrayList<>();
        List<WorldPosition> sinks = new ArrayList<>();
        
        // First, clean expired path cache entries
        cleanExpiredPathCache();
        
        // Identify sources and sinks
        for (Map.Entry<WorldPosition, IEnergyHandler> entry : nodes.entrySet()) {
            IEnergyHandler handler = entry.getValue();
            WorldPosition pos = entry.getKey();
            
            if (handler.canExtract() && handler.getEnergyStored() > 0) {
                sources.add(pos);
            }
            
            if (handler.canReceive() && handler.getEnergyStored() < handler.getMaxEnergyStored()) {
                sinks.add(pos);
            }
        }
        
        // For each source, distribute energy to sinks
        for (WorldPosition sourcePos : sources) {
            IEnergyHandler source = nodes.get(sourcePos);
            
            if (source == null || source.getEnergyStored() <= 0) {
                continue;
            }
            
            // Sort sinks by distance (closer first)
            Collections.sort(sinks, Comparator.comparingInt(pos -> {
                // Simple Manhattan distance
                return Math.abs(pos.getBlockPos().getX() - sourcePos.getBlockPos().getX()) +
                       Math.abs(pos.getBlockPos().getY() - sourcePos.getBlockPos().getY()) +
                       Math.abs(pos.getBlockPos().getZ() - sourcePos.getBlockPos().getZ());
            }));
            
            for (WorldPosition sinkPos : sinks) {
                IEnergyHandler sink = nodes.get(sinkPos);
                
                if (sink == null || sink.getEnergyStored() >= sink.getMaxEnergyStored()) {
                    continue;
                }
                
                // Find path between source and sink
                List<WorldPosition> path = findPath(sourcePos, sinkPos);
                
                if (path.isEmpty()) {
                    continue; // No path found
                }
                
                // Calculate transfer details
                float lossRate = getEnergyLoss(path);
                int transferRate = getTransferRate(path);
                int maxExtract = Math.min(source.getEnergyStored(), transferRate);
                int maxReceive = Math.min(sink.getMaxEnergyStored() - sink.getEnergyStored(), transferRate);
                int amount = Math.min(maxExtract, maxReceive);
                
                if (amount <= 0) {
                    continue;
                }
                
                // Apply energy loss
                int lossAmount = Math.round(amount * lossRate);
                int transferAmount = amount - lossAmount;
                
                // Extract from source
                int extracted = source.extractEnergy(amount, false);
                
                if (extracted <= 0) {
                    continue;
                }
                
                // Apply loss to extracted amount
                transferAmount = extracted - Math.round(extracted * lossRate);
                
                // Transfer to sink
                int received = sink.receiveEnergy(transferAmount, false);
                
                totalTransferred += received;
                
                // If source is depleted or sink is full, move to next
                if (source.getEnergyStored() <= 0 || sink.getEnergyStored() >= sink.getMaxEnergyStored()) {
                    break;
                }
            }
        }
        
        return totalTransferred;
    }
    
    @Override
    public List<WorldPosition> findPath(WorldPosition from, WorldPosition to) {
        // Check the cache first
        CachedPathKey key = new CachedPathKey(from, to);
        if (pathCache.containsKey(key)) {
            // Update the expiration time
            pathExpirations.put(key, System.currentTimeMillis() + PATH_CACHE_EXPIRATION);
            return pathCache.get(key);
        }
        
        // Verify both positions have nodes
        if (!nodes.containsKey(from) || !nodes.containsKey(to)) {
            return Collections.emptyList();
        }
        
        // Calculate the path
        List<WorldPosition> path = calculatePath(from, to);
        
        // Cache the result
        pathCache.put(key, path);
        pathExpirations.put(key, System.currentTimeMillis() + PATH_CACHE_EXPIRATION);
        
        return path;
    }
    
    /**
     * Calculates the path between two positions using A* algorithm.
     * 
     * @param source The source position
     * @param destination The destination position
     * @return The path, or an empty list if no path exists
     */
    private List<WorldPosition> calculatePath(WorldPosition source, WorldPosition destination) {
        // A* algorithm implementation
        Set<WorldPosition> closedSet = new HashSet<>();
        Set<WorldPosition> openSet = new HashSet<>();
        openSet.add(source);
        
        Map<WorldPosition, WorldPosition> cameFrom = new HashMap<>();
        
        Map<WorldPosition, Integer> gScore = new HashMap<>();
        gScore.put(source, 0);
        
        Map<WorldPosition, Integer> fScore = new HashMap<>();
        fScore.put(source, heuristic(source, destination));
        
        while (!openSet.isEmpty()) {
            // Find the node in openSet with the lowest fScore
            WorldPosition current = null;
            int lowestFScore = Integer.MAX_VALUE;
            
            for (WorldPosition pos : openSet) {
                int score = fScore.getOrDefault(pos, Integer.MAX_VALUE);
                if (score < lowestFScore) {
                    lowestFScore = score;
                    current = pos;
                }
            }
            
            if (current.equals(destination)) {
                // Path found, reconstruct and return
                return reconstructPath(cameFrom, source, destination);
            }
            
            openSet.remove(current);
            closedSet.add(current);
            
            for (WorldPosition neighbor : getAdjacentPositions(current)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }
                
                // Check if this is a valid node
                if (!nodes.containsKey(neighbor)) {
                    // If not a node, check if it's a valid connection point
                    // For simplicity, we'll just skip it for now
                    continue;
                }
                
                // Check if the chunk is loaded
                WorldChunk chunk = new WorldChunk(neighbor);
                if (!chunk.isLoaded()) {
                    continue;
                }
                
                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + 1;
                
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                } else if (tentativeGScore >= gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    continue;
                }
                
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, gScore.get(neighbor) + heuristic(neighbor, destination));
            }
        }
        
        // No path found
        return List.of();
    }
    
    /**
     * Reconstructs the path from the cameFrom map
     */
    private List<WorldPosition> reconstructPath(Map<WorldPosition, WorldPosition> cameFrom, WorldPosition source, WorldPosition destination) {
        List<WorldPosition> path = new ArrayList<>();
        WorldPosition current = destination;
        
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
    private List<WorldPosition> getAdjacentPositions(WorldPosition pos) {
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
     * Heuristic function for A* algorithm (Manhattan distance)
     */
    private int heuristic(WorldPosition a, WorldPosition b) {
        return Math.abs(a.getBlockPos().getX() - b.getBlockPos().getX()) +
               Math.abs(a.getBlockPos().getY() - b.getBlockPos().getY()) +
               Math.abs(a.getBlockPos().getZ() - b.getBlockPos().getZ());
    }
    
    @Override
    public float getEnergyLoss(List<WorldPosition> path) {
        // Simple implementation: 1% loss per block in the path
        float lossRate = 0.01f * (path.size() - 1); // -1 because we don't count the source
        return Math.min(lossRate, 0.5f); // Cap at 50% loss
    }
    
    @Override
    public int getTransferRate(List<WorldPosition> path) {
        // Simple implementation: constant rate
        return 1000; // 1000 energy units per tick
    }
    
    @Override
    public void onChunkStatusChange(WorldChunk chunk, boolean loaded) {
        // When a chunk is unloaded, we can temporarily ignore nodes in that chunk
        // When it's loaded again, we need to recalculate paths that might involve it
        if (!loaded || !chunkToNodesMap.containsKey(chunk)) {
            return;
        }
        
        // When a chunk is loaded, invalidate paths that might now be possible
        invalidateCachedPaths();
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
     * Key class for the path cache
     */
    private static class CachedPathKey {
        private final WorldPosition source;
        private final WorldPosition destination;
        
        public CachedPathKey(WorldPosition source, WorldPosition destination) {
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