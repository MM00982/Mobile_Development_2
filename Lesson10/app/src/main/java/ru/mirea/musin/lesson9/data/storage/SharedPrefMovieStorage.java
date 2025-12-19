package ru.mirea.musin.lesson9.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDate;

import ru.mirea.musin.lesson9.data.storage.models.MovieData;

public class SharedPrefMovieStorage implements MovieStorage {

    private static final String PREFS = "movie_prefs";
    private static final String KEY_NAME = "movie_name";
    private static final String KEY_DATE = "movie_date";
    private static final String KEY_ID = "movie_id";

    private final SharedPreferences prefs;

    public SharedPrefMovieStorage(Context context) {
        this.prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public MovieData get() {
        String name = prefs.getString(KEY_NAME, "unknown");
        String date = prefs.getString(KEY_DATE, LocalDate.now().toString());
        int id = prefs.getInt(KEY_ID, -1);

        return new MovieData(id, name, date);
    }

    @Override
    public boolean save(MovieData movie) {
        prefs.edit()
                .putString(KEY_NAME, movie.getName())
                .putString(KEY_DATE, LocalDate.now().toString())
                .putInt(KEY_ID, movie.getId())
                .apply();
        return true;
    }
}
