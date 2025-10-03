package ru.mirea.musin.travelweather.domain.repository;

public interface VoiceRepository {
    String recognizeCity(byte[] audioBytes);
}