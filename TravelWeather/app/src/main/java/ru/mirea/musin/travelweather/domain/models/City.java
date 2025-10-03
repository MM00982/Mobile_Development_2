package ru.mirea.musin.travelweather.domain.models;

public class City {
    private final int id;
    private final String name;
    private final String country;

    public City(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCountry() { return country; }
}