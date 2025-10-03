package ru.mirea.musin.travelweather.domain.models;

public class User {
    private final int id;
    private final String name;

    public User(int id, String name) {
        this.id = id; this.name = name;
    }
    public int getId() { return id; }
    public String getName() { return name; }
}
