package ru.mirea.musin.lesson9.data.repository;

import java.time.LocalDate;

import ru.mirea.musin.lesson9.data.storage.MovieStorage;
import ru.mirea.musin.lesson9.data.storage.models.MovieData;
import ru.mirea.musin.lesson9.domain.models.Movie;
import ru.mirea.musin.lesson9.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {

    private final MovieStorage storage;

    public MovieRepositoryImpl(MovieStorage storage) {
        this.storage = storage;
    }

    @Override
    public boolean saveMovie(Movie movie) {
        storage.save(mapToStorage(movie));
        return true;
    }

    @Override
    public Movie getMovie() {
        MovieData data = storage.get();
        return mapToDomain(data);
    }

    // ----- Маппинг (преобразование моделей) -----

    private MovieData mapToStorage(Movie movie) {
        return new MovieData(
                1,
                movie.getName(),
                LocalDate.now().toString()
        );
    }

    private Movie mapToDomain(MovieData data) {
        return new Movie(data.getId(), data.getName());
    }
}
