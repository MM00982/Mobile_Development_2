package ru.mirea.musin.travelweather.domain.usecases;

import java.util.List;
import ru.mirea.musin.travelweather.domain.models.City;
import ru.mirea.musin.travelweather.domain.repository.WeatherRepository;

public class SearchCity {
    private final WeatherRepository repo;
    public SearchCity(WeatherRepository repo) { this.repo = repo; }
    public List<City> execute(String query) { return repo.searchCity(query); }
}
