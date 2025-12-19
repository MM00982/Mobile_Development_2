package ru.mirea.musin.domain.usecases;

import java.util.List;
import ru.mirea.musin.domain.models.City;
import ru.mirea.musin.domain.repository.WeatherRepository;

public class SearchCity {
    private final WeatherRepository repo;
    public SearchCity(WeatherRepository repo) { this.repo = repo; }
    public List<City> execute(String query) { return repo.searchCity(query); }
}
