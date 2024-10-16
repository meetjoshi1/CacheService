package com.cache;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CachingService {
    private static final Logger logger = LoggerFactory.getLogger(CachingService.class);

    private final int maxCacheSize=100;
    private final Map<Long, Entity> cache;  // Using Entity.getId() as key
    private final Map<Long, Integer> usageFrequency;  // To track how often each entity is accessed
    
    @Autowired
    private final DatabaseService databaseService;
    
  
    public CachingService(DatabaseService databaseService) {
        this.cache = new ConcurrentHashMap<>();
        this.usageFrequency = new ConcurrentHashMap<>();
        this.databaseService = databaseService;
        logger.info("Initialized caching service with maxCacheSize: {}", maxCacheSize);
    }

    public synchronized void add(Entity entity) {
        long id = entity.getId();
        if (cache.size() >= maxCacheSize) {
            evictLeastUsed();
        }
        cache.put(id, entity);
        usageFrequency.put(id, 1);  // Set initial usage count
        logger.info("Added entity {} to cache", id);
    }

    public synchronized void remove(Entity entity) {
        long id = entity.getId();
        cache.remove(id);
        usageFrequency.remove(id);
        databaseService.deleteFromDatabase(entity);
        logger.info("Removed entity {} from cache and database", id);
    }

    public synchronized void removeAll() {
        cache.clear();
        usageFrequency.clear();
        databaseService.deleteAllFromDatabase();
        logger.info("Removed all entities from cache and database");
    }

    public synchronized Entity get(Entity entity) {
        long id = entity.getId();
        if (cache.containsKey(id)) {
            // Update usage frequency
            usageFrequency.put(id, usageFrequency.get(id) + 1);
            logger.info("Entity {} found in cache", id);
            return cache.get(id);
        } else {
            // Retrieve from database if not in cache
            Entity dbEntity = databaseService.fetchFromDatabase(id);
            if (dbEntity != null) {
                add(dbEntity);
                logger.info("Entity {} retrieved from database and added to cache", id);
                return dbEntity;
            }
        }
        logger.warn("Entity {} not found", id);
        return null;  // Entity not found in cache or database
    }

    public synchronized void clear() {
        cache.clear();
        usageFrequency.clear();
        logger.info("Cleared cache without affecting database entries");
    }

    private void evictLeastUsed() {
        long leastUsedId = Collections.min(usageFrequency.entrySet(), Map.Entry.comparingByValue()).getKey();
        Entity leastUsedEntity = cache.remove(leastUsedId);  // Remove the least-used entity
        usageFrequency.remove(leastUsedId);  // Ensure it's removed from the frequency map too
        databaseService.saveToDatabase(leastUsedEntity);  // Optional: Saving it back to the database
        logger.info("Evicted least used entity {} to database", leastUsedId);
    }

}
