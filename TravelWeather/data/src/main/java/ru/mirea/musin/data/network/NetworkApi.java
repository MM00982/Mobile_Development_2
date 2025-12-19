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

        // ИСПРАВЛЕНИЕ: Передаем true (день) и "01d" (иконка солнца) как заглушки
        return new WeatherNow(1, 20.0, "Солнечно (Network Mock)", true, "01d");
    }
}