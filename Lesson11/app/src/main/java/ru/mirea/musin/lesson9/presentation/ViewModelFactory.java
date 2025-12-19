package ru.mirea.musin.lesson9.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.musin.lesson9.data.repository.MovieRepositoryImpl;
import ru.mirea.musin.lesson9.data.storage.MovieStorage;
import ru.mirea.musin.lesson9.data.storage.SharedPrefMovieStorage;
import ru.mirea.musin.lesson9.domain.repository.MovieRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public ViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        MovieStorage storage = new SharedPrefMovieStorage(context);
        MovieRepository movieRepository = new MovieRepositoryImpl(storage);

        return (T) new MainViewModel(movieRepository);
    }
}