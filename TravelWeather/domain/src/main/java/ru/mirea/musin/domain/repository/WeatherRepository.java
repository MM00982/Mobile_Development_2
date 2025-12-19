package ru.mirea.musin.domain.repository;

import java.util.List;
import ru.mirea.musin.domain.models.City;
import ru.mirea.musin.domain.models.WeatherNow;

public interface WeatherRepository {
    List<City> getCityList();
    List<City> searchCity(String query);
    WeatherNow getForecastByCity(String cityName);
    boolean syncWeatherFromApi();

    // --- ДОБАВЬ ВОТ ЭТУ СТРОКУ ---
    List<WeatherNow> get5DayForecast(String cityName);
}