package ru.mirea.musin.lesson9.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.musin.lesson9.domain.models.Movie;
import ru.mirea.musin.lesson9.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {

    private static final String PREFS = "movie_prefs";
    private static final String KEY_NAME = "movie_name";

    private final SharedPreferences prefs;

    public MovieRepositoryImpl(Context context) {
        this.prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public boolean saveMovie(Movie movie) {
        prefs.edit()
                .putString(KEY_NAME, movie.getName())
                .apply();
        return true;
    }

    @Override
    public Movie getMovie() {
        String name = prefs.getString(KEY_NAME, null);
        if (name == null || name.trim().isEmpty()) {
            return new Movie(0, "Doctor Strange");
        }
        return new Movie(1, name);
    }
}