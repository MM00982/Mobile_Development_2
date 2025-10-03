package ru.mirea.musin.travelweather.data.repository;

import java.util.*;
import ru.mirea.musin.travelweather.domain.models.City;
import ru.mirea.musin.travelweather.domain.models.WeatherNow;
import ru.mirea.musin.travelweather.domain.repository.WeatherRepository;

public class WeatherRepositoryImpl implements WeatherRepository {

    private final List<City> cities = Arrays.asList(
            new City(1, "Москва", "RU"),
            new City(2, "Санкт-Петербург", "RU"),
            new City(3, "Амстердам", "NL"),
            new City(4, "Париж", "FR")
    );

    @Override public List<City> getCityList() { return cities; }

    @Override public List<City> searchCity(String query) {
        String q = query.toLowerCase(Locale.ROOT);
        List<City> res = new ArrayList<>();
        for (City c : cities) if (c.getName().toLowerCase(Locale.ROOT).contains(q)) res.add(c);
        return res;
    }

    @Override public WeatherNow getForecastByCity(String cityName) {
        int id = 1;
        for (City c : cities) if (c.getName().equalsIgnoreCase(cityName)) { id = c.getId(); break; }
        return new WeatherNow(id, 21.5, "Ясно"); // мок
    }

    @Override public boolean syncWeatherFromApi() { return true; } // мок
}