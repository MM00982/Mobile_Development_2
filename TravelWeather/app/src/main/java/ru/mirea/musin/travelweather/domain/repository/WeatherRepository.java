package ru.mirea.musin.travelweather.domain.repository;

import java.util.List;
import ru.mirea.musin.travelweather.domain.models.City;
import ru.mirea.musin.travelweather.domain.models.WeatherNow;

public interface WeatherRepository {
    List<City> getCityList();
    List<City> searchCity(String query);
    WeatherNow getForecastByCity(String cityName);
    boolean syncWeatherFromApi();
}