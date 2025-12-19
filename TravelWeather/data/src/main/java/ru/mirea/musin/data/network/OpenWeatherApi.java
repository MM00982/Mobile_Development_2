package ru.mirea.musin.data.network;

import ru.mirea.musin.data.network.model.ForecastResponse;
import ru.mirea.musin.data.network.model.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApi {
    @GET("data/2.5/weather")
    Call<WeatherResponse> getCurrentWeather(
            @Query("q") String cityName,
            @Query("appid") String apiKey,
            @Query("units") String units, // metric
            @Query("lang") String lang    // ru
    );

    @GET("data/2.5/forecast")
    Call<ForecastResponse> getForecast(
            @Query("q") String cityName,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String lang
    );
}