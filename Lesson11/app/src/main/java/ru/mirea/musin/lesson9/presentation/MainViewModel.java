package ru.mirea.musin.lesson9.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.musin.lesson9.domain.models.Movie;
import ru.mirea.musin.lesson9.domain.repository.MovieRepository;
import ru.mirea.musin.lesson9.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.musin.lesson9.domain.usecases.SaveMovieToFavoriteUseCase;

public class MainViewModel extends ViewModel {

    private final MovieRepository movieRepository;
    private final MutableLiveData<String> favoriteMovie = new MutableLiveData<>();

    public MainViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public LiveData<String> getFavoriteMovie() {
        return favoriteMovie;
    }

    public void saveMovie(String text) {
        boolean result = new SaveMovieToFavoriteUseCase(movieRepository).execute(new Movie(2, text));
        favoriteMovie.setValue(String.format("Save result: %s", result));
    }

    public void getMovie() {
        Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
        favoriteMovie.setValue(String.format("My favorite movie is %s", movie.getName()));
    }
}