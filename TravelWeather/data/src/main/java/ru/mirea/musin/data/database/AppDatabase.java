package ru.mirea.musin.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CityEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CityDao cityDao();
}