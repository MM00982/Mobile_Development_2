package ru.mirea.musin.domain.usecases;

import ru.mirea.musin.domain.repository.VoiceRepository;

public class RecognizeVoiceToCity {
    private final VoiceRepository repo;
    public RecognizeVoiceToCity(VoiceRepository repo) { this.repo = repo; }
    public String execute(byte[] audioBytes) { return repo.recognizeCity(audioBytes); }
}