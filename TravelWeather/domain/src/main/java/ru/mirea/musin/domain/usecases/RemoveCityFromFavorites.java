package ru.mirea.musin.domain.usecases;

import ru.mirea.musin.domain.models.City;
import ru.mirea.musin.domain.repository.FavoritesRepository;

public class RemoveCityFromFavorites {
    private final FavoritesRepository repo;
    public RemoveCityFromFavorites(FavoritesRepository repo) { this.repo = repo; }
    public boolean execute(City city) { return repo.removeCity(city); }
}