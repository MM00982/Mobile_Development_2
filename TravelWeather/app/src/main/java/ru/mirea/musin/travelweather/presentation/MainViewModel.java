package ru.mirea.musin.travelweather.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.musin.domain.models.City;
import ru.mirea.musin.domain.repository.FavoritesRepository;
import ru.mirea.musin.domain.repository.VoiceRepository;
import ru.mirea.musin.domain.usecases.GetFavorites;
import ru.mirea.musin.domain.usecases.RecognizeVoiceToCity;
import ru.mirea.musin.domain.usecases.RemoveCityFromFavorites;

public class MainViewModel extends ViewModel {

    private final FavoritesRepository favRepo;
    private final VoiceRepository voiceRepo;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<City>> favoritesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> voiceResultLiveData = new MutableLiveData<>();

    public MainViewModel(FavoritesRepository favRepo, VoiceRepository voiceRepo) {
        this.favRepo = favRepo;
        this.voiceRepo = voiceRepo;
    }

    public LiveData<List<City>> getFavorites() {
        return favoritesLiveData;
    }

    public LiveData<String> getVoiceResult() {
        return voiceResultLiveData;
    }

    public void loadFavorites() {
        executor.execute(() -> {
            List<City> cities = new GetFavorites(favRepo).execute();
            favoritesLiveData.postValue(cities);
        });
    }

    public void removeCity(City city) {
        executor.execute(() -> {
            new RemoveCityFromFavorites(favRepo).execute(city);
            loadFavorites();
        });
    }

    public void recognizeSpeech() {
        executor.execute(() -> {
            String city = new RecognizeVoiceToCity(voiceRepo).execute(new byte[0]);
            voiceResultLiveData.postValue(city);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}