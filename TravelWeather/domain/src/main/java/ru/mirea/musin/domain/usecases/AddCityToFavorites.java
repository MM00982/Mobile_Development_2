package ru.mirea.musin.domain.usecases;

import ru.mirea.musin.domain.models.City;
import ru.mirea.musin.domain.repository.FavoritesRepository;

public class AddCityToFavorites {
    private final FavoritesRepository repo;
    public AddCityToFavorites(FavoritesRepository repo) { this.repo = repo; }
    public boolean execute(City city) { return repo.addCity(city); }
}