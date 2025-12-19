package ru.mirea.musin.data.database;

import androidx.room.ColumnInfo; // Добавь этот импорт
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import ru.mirea.musin.domain.models.City;

@Entity(tableName = "cities")
public class CityEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String country;

    // НОВОЕ ПОЛЕ: Кто сохранил город (email пользователя или "guest")
    @ColumnInfo(name = "owner_id")
    public String ownerId;

    public CityEntity() {}

    public CityEntity(int id, String name, String country, String ownerId) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.ownerId = ownerId;
    }

    // Здесь мы пока поставим заглушку ownerId, реальный ID подставит репозиторий
    public static CityEntity fromDomain(City city) {
        return new CityEntity(city.getId(), city.getName(), city.getCountry(), "guest");
    }

    public City toDomain() {
        return new City(this.id, this.name, this.country);
    }
}