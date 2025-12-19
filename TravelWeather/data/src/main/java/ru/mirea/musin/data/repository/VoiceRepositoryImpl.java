package ru.mirea.musin.data.repository;

import java.util.Random;
import ru.mirea.musin.domain.repository.VoiceRepository;

public class VoiceRepositoryImpl implements VoiceRepository {

    // Список городов для рандомного выбора
    private final String[] MOCK_CITIES = {
            "Москва",
            "Санкт-Петербург",
            "Лондон",
            "Париж",
            "Нью-Йорк",
            "Токио",
            "Дубай",
            "Берлин",
            "Рим",
            "Стамбул",
            "Казань",
            "Сочи",
            "Екатеринбург",
            "Новосибирск",
            "Калининград"
    };

    private final Random random = new Random();

    @Override
    public String recognizeCity(byte[] audioData) {
        // Имитация задержки обработки (как будто отправляем голос на сервер)
        try {
            Thread.sleep(1500); // 1.5 секунды "думаем"
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Выбираем случайный город из списка
        int index = random.nextInt(MOCK_CITIES.length);
        return MOCK_CITIES[index];
    }
}