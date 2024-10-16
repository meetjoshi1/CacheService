package com.cache;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DatabaseServiceImpl implements DatabaseService {
    private Map<Long, Entity> database = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(CachingService.class);


    @Override
    public void saveToDatabase(Entity entity) {
        database.put(entity.getId(), entity);
        logger.info("Saved entity {} to database", entity.getId());
    }

    @Override
    public void deleteFromDatabase(Entity entity) {
        database.remove(entity.getId());
        logger.info("Deleted entity {} from database", entity.getId());
    }

    @Override
    public Entity fetchFromDatabase(long entityId) {
        logger.info("Fetched entity {} from database", entityId);
        return database.get(entityId);
    }

    @Override
    public void deleteAllFromDatabase() {
        database.clear();
        logger.info("Deleted all entities from database");
    }
}
