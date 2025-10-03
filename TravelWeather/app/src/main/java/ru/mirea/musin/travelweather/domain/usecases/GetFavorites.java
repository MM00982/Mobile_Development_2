package ru.mirea.musin.travelweather.domain.usecases;

import java.util.List;
import ru.mirea.musin.travelweather.domain.models.City;
import ru.mirea.musin.travelweather.domain.repository.FavoritesRepository;

public class GetFavorites {
    private final FavoritesRepository repo;
    public GetFavorites(FavoritesRepository repo) { this.repo = repo; }
    public List<City> execute() { return repo.getFavorites(); }
}