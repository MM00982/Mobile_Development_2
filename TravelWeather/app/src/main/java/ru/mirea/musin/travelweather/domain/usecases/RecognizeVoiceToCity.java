package ru.mirea.musin.travelweather.domain.usecases;

import ru.mirea.musin.travelweather.domain.repository.VoiceRepository;

public class RecognizeVoiceToCity {
    private final VoiceRepository repo;
    public RecognizeVoiceToCity(VoiceRepository repo) { this.repo = repo; }
    public String execute(byte[] audioBytes) { return repo.recognizeCity(audioBytes); }
}