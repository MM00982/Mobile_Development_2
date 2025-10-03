package ru.mirea.musin.travelweather.data.repository;

import ru.mirea.musin.travelweather.domain.repository.VoiceRepository;

public class VoiceRepositoryImpl implements VoiceRepository {
    @Override
    public String recognizeCity(byte[] audioBytes) {
        return "Москва"; // мок
    }
}