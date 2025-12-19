package ru.mirea.musin.data.repository;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.musin.data.network.OpenWeatherApi;
import ru.mirea.musin.data.network.model.ForecastResponse;
import ru.mirea.musin.data.network.model.WeatherResponse;
import ru.mirea.musin.domain.models.City;
import ru.mirea.musin.domain.models.WeatherNow;
import ru.mirea.musin.domain.repository.WeatherRepository;

public class WeatherRepositoryImpl implements WeatherRepository {
    private final OpenWeatherApi api;
    private final String API_KEY = "c46f8646fcbbab02b1eccd639673be3a";

    public WeatherRepositoryImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(OpenWeatherApi.class);
    }

    @Override
    public WeatherNow getForecastByCity(String cityName) {
        try {
            Response<WeatherResponse> response = api.getCurrentWeather(cityName, API_KEY, "metric", "ru").execute();

            if (response.isSuccessful() && response.body() != null) {
                WeatherResponse w = response.body();
                String condition = w.weather.get(0).description;
                String iconCode = w.weather.get(0).icon; // Получаем код иконки (нап. "10d")

                boolean isDay = true;
                if (w.sys != null) {
                    long now = System.currentTimeMillis() / 1000;
                    isDay = (now > w.sys.sunrise && now < w.sys.sunset);
                }

                // Передаем iconCode
                return new WeatherNow(1, w.main.temp, condition, isDay, iconCode);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<WeatherNow> get5DayForecast(String cityName) {
        List<WeatherNow> result = new ArrayList<>();
        try {
            Response<ForecastResponse> response = api.getForecast(cityName, API_KEY, "metric", "ru").execute();
            if (response.isSuccessful() && response.body() != null) {
                List<WeatherResponse> list = response.body().list;

                for (WeatherResponse item : list) {
                    boolean isDay = "d".equals(item.sys.pod);
                    String iconCode = item.weather.get(0).icon;

                    result.add(new WeatherNow((int)item.dt, item.main.temp, item.weather.get(0).description, isDay, iconCode));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override public List<City> getCityList() { return new ArrayList<>(); }
    @Override public List<City> searchCity(String query) { return new ArrayList<>(); }
    @Override public boolean syncWeatherFromApi() { return true; }
}