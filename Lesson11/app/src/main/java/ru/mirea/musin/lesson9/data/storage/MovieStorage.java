package ru.mirea.musin.lesson9.data.storage;

import ru.mirea.musin.lesson9.data.storage.models.MovieData;

public interface MovieStorage {
    MovieData get();
    boolean save(MovieData movie);
}
