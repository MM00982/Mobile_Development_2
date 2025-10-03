package ru.mirea.musin.travelweather.data.repository;

import java.util.*;
import ru.mirea.musin.travelweather.domain.models.City;
import ru.mirea.musin.travelweather.domain.repository.FavoritesRepository;

public class FavoritesRepositoryImpl implements FavoritesRepository {

    private final List<City> favorites = new ArrayList<>(Collections.singletonList(
            new City(1, "Москва", "RU")
    ));

    @Override public boolean addCity(City city) {
        for (City c : favorites) if (c.getId() == city.getId()) return false;
        favorites.add(city);
        return true;
    }

    @Override public boolean removeCity(City city) {
        return favorites.removeIf(c -> c.getId() == city.getId());
    }

    @Override public List<City> getFavorites() { return new ArrayList<>(favorites); }
}
