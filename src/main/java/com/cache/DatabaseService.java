package com.cache;

public interface DatabaseService {
    void saveToDatabase(Entity entity);
    void deleteFromDatabase(Entity entity);
    Entity fetchFromDatabase(long entityId);
    void deleteAllFromDatabase();
}
