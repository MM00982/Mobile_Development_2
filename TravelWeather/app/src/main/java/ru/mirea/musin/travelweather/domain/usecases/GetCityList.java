package ru.mirea.musin.travelweather.domain.usecases;

import java.util.List;
import ru.mirea.musin.travelweather.domain.models.City;
import ru.mirea.musin.travelweather.domain.repository.WeatherRepository;

public class GetCityList {
    private final WeatherRepository repo;
    public GetCityList(WeatherRepository repo) { this.repo = repo; }
    public List<City> execute() { return repo.getCityList(); }
}