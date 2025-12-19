package ru.mirea.musin.travelweather.presentation;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import ru.mirea.musin.data.database.AppDatabase;
import ru.mirea.musin.data.repository.FavoritesRepositoryImpl;
import ru.mirea.musin.data.repository.VoiceRepositoryImpl;
import ru.mirea.musin.data.repository.WeatherRepositoryImpl;
import ru.mirea.musin.domain.repository.FavoritesRepository;
import ru.mirea.musin.domain.repository.VoiceRepository;
import ru.mirea.musin.domain.repository.WeatherRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public ViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "weather-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        FavoritesRepository favRepo = new FavoritesRepositoryImpl(db);
        WeatherRepository weatherRepo = new WeatherRepositoryImpl();
        VoiceRepository voiceRepo = new VoiceRepositoryImpl();

        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(favRepo, voiceRepo);
        }
        else if (modelClass.isAssignableFrom(WeatherViewModel.class)) {
            return (T) new WeatherViewModel(weatherRepo, favRepo);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}