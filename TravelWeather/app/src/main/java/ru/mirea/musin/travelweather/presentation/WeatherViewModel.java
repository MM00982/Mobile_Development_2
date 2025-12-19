package ru.mirea.musin.travelweather.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.musin.domain.models.City;
import ru.mirea.musin.domain.models.WeatherNow;
import ru.mirea.musin.domain.repository.FavoritesRepository;
import ru.mirea.musin.domain.repository.WeatherRepository;
import ru.mirea.musin.domain.usecases.AddCityToFavorites;
import ru.mirea.musin.domain.usecases.RemoveCityFromFavorites;

public class WeatherViewModel extends ViewModel {

    private final WeatherRepository weatherRepo;
    private final FavoritesRepository favRepo;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    // Данные для UI
    private final MutableLiveData<WeatherNow> currentWeather = new MutableLiveData<>();
    private final MutableLiveData<List<WeatherNow>> forecast = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public WeatherViewModel(WeatherRepository weatherRepo, FavoritesRepository favRepo) {
        this.weatherRepo = weatherRepo;
        this.favRepo = favRepo;
    }

    public LiveData<WeatherNow> getCurrentWeather() { return currentWeather; }
    public LiveData<List<WeatherNow>> getForecast() { return forecast; }
    public LiveData<Boolean> getIsFavorite() { return isFavorite; }
    public LiveData<String> getError() { return error; }

    public void loadWeather(String cityName) {
        executor.execute(() -> {
            boolean isFav = favRepo.isFavorite(cityName);
            isFavorite.postValue(isFav);
        });

        executor.execute(() -> {
            WeatherNow now = weatherRepo.getForecastByCity(cityName);
            List<WeatherNow> daily = weatherRepo.get5DayForecast(cityName);

            if (now != null) {
                currentWeather.postValue(now);
                forecast.postValue(daily);
            } else {
                error.postValue("Не удалось загрузить данные для города: " + cityName);
            }
        });
    }

    public void toggleFavorite(String cityName) {
        executor.execute(() -> {
            City cityObj = new City(cityName.hashCode(), cityName, "RU");
            boolean currentStatus = favRepo.isFavorite(cityName);

            if (currentStatus) {
                new RemoveCityFromFavorites(favRepo).execute(cityObj);
                isFavorite.postValue(false);
            } else {
                new AddCityToFavorites(favRepo).execute(cityObj);
                isFavorite.postValue(true);
            }
        });
    }
}