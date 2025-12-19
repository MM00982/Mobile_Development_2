package ru.mirea.musin.domain.usecases;

import java.util.List;
import ru.mirea.musin.domain.models.City;
import ru.mirea.musin.domain.repository.WeatherRepository;

public class GetCityList {
    private final WeatherRepository repo;
    public GetCityList(WeatherRepository repo) { this.repo = repo; }
    public List<City> execute() { return repo.getCityList(); }
}