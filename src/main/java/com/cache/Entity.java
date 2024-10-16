package com.cache;

public class Entity {
    private long id;
    private String data;

    public Entity(long id, String data) {
        this.id = id;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public String getData() {
        return data;
    }

	public Entity() {
		super();
	}
}
