package ru.mirea.musin.domain.repository;

public interface VoiceRepository {
    // Принимает байты аудио (заглушка), возвращает название города
    String recognizeCity(byte[] audioData);
}