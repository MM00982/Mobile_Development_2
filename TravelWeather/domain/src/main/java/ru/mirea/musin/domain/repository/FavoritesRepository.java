package ru.mirea.musin.domain.repository;

import java.util.List;
import ru.mirea.musin.domain.models.City;

public interface FavoritesRepository {
    boolean isFavorite(String cityName);
    boolean addCity(City city);
    boolean removeCity(City city);
    List<City> getFavorites();
}