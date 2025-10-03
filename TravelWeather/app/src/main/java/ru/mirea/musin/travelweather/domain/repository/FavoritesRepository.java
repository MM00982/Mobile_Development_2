package ru.mirea.musin.travelweather.domain.repository;

import java.util.List;
import ru.mirea.musin.travelweather.domain.models.City;

public interface FavoritesRepository {
    boolean addCity(City city);
    boolean removeCity(City city);
    List<City> getFavorites();
}