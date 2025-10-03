package ru.mirea.musin.travelweather.domain.usecases;

import ru.mirea.musin.travelweather.domain.repository.WeatherRepository;

public class SyncWeatherFromApi {
    private final WeatherRepository repo;
    public SyncWeatherFromApi(WeatherRepository repo) { this.repo = repo; }
    public boolean execute() { return repo.syncWeatherFromApi(); }
}