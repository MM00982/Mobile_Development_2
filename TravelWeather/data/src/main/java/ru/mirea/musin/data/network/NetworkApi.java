package ru.mirea.musin.data.network;

import ru.mirea.musin.domain.models.WeatherNow;

public class NetworkApi {
    // Имитация запроса в интернет
    public WeatherNow getWeatherFromApi(String city) {
        try {
            Thread.sleep(1000); // Задержка 1 секунда
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ИСПРАВЛЕНИЕ: Добавили "1" (cityId) первым параметром
        return new WeatherNow(1, 20.0, "Солнечно (Network Mock)");
    }
}