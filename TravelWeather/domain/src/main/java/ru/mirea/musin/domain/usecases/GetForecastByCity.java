package ru.mirea.musin.domain.usecases;

import ru.mirea.musin.domain.models.WeatherNow;
import ru.mirea.musin.domain.repository.WeatherRepository;

public class GetForecastByCity {
    private final WeatherRepository repo;
    public GetForecastByCity(WeatherRepository repo) { this.repo = repo; }
    public WeatherNow execute(String cityName) { return repo.getForecastByCity(cityName); }
}
