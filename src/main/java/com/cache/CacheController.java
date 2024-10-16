package com.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/cache")
public class CacheController {
	
	@Autowired
    private final CachingService cachingService;

    public CacheController(CachingService cachingService) {
        this.cachingService = cachingService;
    }

    @PostMapping("/add")
    @ApiOperation(value = "Add entity to cache")
    public void addEntity(@RequestBody Entity entity) {
        cachingService.add(entity);
    }

    @DeleteMapping("/remove")
    @ApiOperation(value = "Remove entity to cache")
    public void removeEntity(@RequestBody Entity entity) {
        cachingService.remove(entity);
    }

    @GetMapping("/get")
    @ApiOperation(value = "Get entity from cache")
    public Entity getEntity(@RequestParam long id) {
        return cachingService.get(new Entity(id, ""));
    }

    @DeleteMapping("/removeAll")
    @ApiOperation(value = "Remove all from cache")
    public void removeAll() {
        cachingService.removeAll();
    }

    @PostMapping("/clear")
    @ApiOperation(value = "clear all from cache")
    public void clearCache() {
        cachingService.clear();
    }
}
