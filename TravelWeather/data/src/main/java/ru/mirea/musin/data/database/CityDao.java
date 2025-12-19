package ru.mirea.musin.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CityDao {
    // Получаем города только конкретного владельца (userId)
    @Query("SELECT * FROM cities WHERE owner_id = :ownerId")
    List<CityEntity> getAllForUser(String ownerId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CityEntity city);

    @Delete
    void delete(CityEntity city);

    // Ищем город по имени И по владельцу
    @Query("SELECT * FROM cities WHERE name = :cityName AND owner_id = :ownerId LIMIT 1")
    CityEntity getByNameAndUser(String cityName, String ownerId);
}