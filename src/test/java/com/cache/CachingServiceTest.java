package com.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CachingServiceTest {
    private CachingService cachingService;
    private DatabaseServiceImpl databaseService;

    @BeforeEach
    public void setUp() {
        databaseService = new DatabaseServiceImpl();
        cachingService = new CachingService(databaseService); // Set max cache size to 2 for testing
    }

    @Test
    public void testAddAndGet() {
        Entity e1 = new Entity(1L, "Data1");
        Entity e2 = new Entity(2L, "Data2");

        cachingService.add(e1);
        cachingService.add(e2);

        assertEquals(e1, cachingService.get(e1));
        assertEquals(e2, cachingService.get(e2));
    }

    @Test
    void testEviction() {
        CachingService cachingService = new CachingService(databaseService);

        Entity entity1 = new Entity(1L, "test1");
        Entity entity2 = new Entity(2L,"test2");
        Entity entity3 = new Entity(3L,"test3");  // This should cause the eviction of entity1

        cachingService.add(entity1);
        cachingService.add(entity2);
        cachingService.add(entity3);  // At this point, entity1 should be evicted

        
        assertNotNull(cachingService.get(entity1));

        // Ensure entity2 and entity3 are still in the cache
        assertNotNull(cachingService.get(entity2));
        assertNotNull(cachingService.get(entity3));
    }


    @Test
    public void testRemoveAll() {
        cachingService.add(new Entity(1L, "Data1"));
        cachingService.add(new Entity(2L, "Data2"));

        cachingService.removeAll();

        assertNull(cachingService.get(new Entity(1L, "")));
        assertNull(cachingService.get(new Entity(2L, "")));
    }

    @Test
    public void testClearCache() {
        cachingService.add(new Entity(1L, "Data1"));
        cachingService.clear();
        assertNull(cachingService.get(new Entity(1L, "")));
    }
}
