package ru.mirea.musin.lesson9.domain.usecases;

import ru.mirea.musin.lesson9.domain.models.Movie;
import ru.mirea.musin.lesson9.domain.repository.MovieRepository;

public class SaveMovieToFavoriteUseCase {
    private final MovieRepository movieRepository;

    public SaveMovieToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public boolean execute(Movie movie) {
        if (movie == null || movie.getName() == null || movie.getName().trim().isEmpty()) {
            return false;
        }
        return movieRepository.saveMovie(movie);
    }
}