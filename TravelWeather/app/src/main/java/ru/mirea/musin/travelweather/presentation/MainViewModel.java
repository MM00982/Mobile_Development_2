package ru.mirea.musin.travelweather.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
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

    private final MutableLiveData<List<City>> contentLiveData = new MutableLiveData<>(); // Список для экрана
    private final MutableLiveData<String> voiceResultLiveData = new MutableLiveData<>();

    public MainViewModel(FavoritesRepository favRepo, VoiceRepository voiceRepo) {
        this.favRepo = favRepo;
        this.voiceRepo = voiceRepo;
    }

    public LiveData<List<City>> getContent() {
        return contentLiveData;
    }

    public LiveData<String> getVoiceResult() {
        return voiceResultLiveData;
    }

    // Загрузка данных: Избранное + Популярное
    public void loadContent() {
        executor.execute(() -> {
            // 1. Получаем избранное из БД
            List<City> favorites = new GetFavorites(favRepo).execute();
            // Обновляем поле country на "Сохранено", чтобы Адаптер знал, что писать
            List<City> displayList = new ArrayList<>();
            for (City c : favorites) {
                displayList.add(new City(c.getId(), c.getName(), "Сохранено"));
            }

            // 2. Добавляем популярные города
            String[] popularNames = {"Москва", "Лондон", "Токио", "Нью-Йорк", "Париж", "Дубай"};

            for (String popName : popularNames) {
                // Проверяем, нет ли уже такого города в избранном
                boolean alreadyFav = false;
                for (City fav : favorites) {
                    if (fav.getName().equalsIgnoreCase(popName)) {
                        alreadyFav = true;
                        break;
                    }
                }

                // Если не в избранном - добавляем как "Популярное"
                if (!alreadyFav) {
                    displayList.add(new City(0, popName, "Популярное"));
                }
            }

            contentLiveData.postValue(displayList);
        });
    }

    public void removeCity(City city) {
        executor.execute(() -> {
            // Удаляем только если это "Сохранено" (ID != 0 или проверка через репо)
            // Но метод репозитория ищет по имени, так что сработает
            new RemoveCityFromFavorites(favRepo).execute(city);
            loadContent(); // Перезагружаем список
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