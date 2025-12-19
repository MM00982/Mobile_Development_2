package ru.mirea.musin.lesson9.domain.repository;

import ru.mirea.musin.lesson9.domain.models.Movie;

public interface MovieRepository {
    boolean saveMovie(Movie movie);
    Movie getMovie();
}