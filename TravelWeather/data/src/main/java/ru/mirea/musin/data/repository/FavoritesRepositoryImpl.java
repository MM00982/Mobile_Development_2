package ru.mirea.musin.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.musin.data.database.AppDatabase;
import ru.mirea.musin.data.database.CityEntity;
import ru.mirea.musin.domain.models.City;
import ru.mirea.musin.domain.repository.FavoritesRepository;

public class FavoritesRepositoryImpl implements FavoritesRepository {

    private final AppDatabase db;
    private final FirebaseAuth auth;

    public FavoritesRepositoryImpl(AppDatabase db) {
        this.db = db;
        this.auth = FirebaseAuth.getInstance();
    }

    // Вспомогательный метод: получаем текущего ID (email) или "guest"
    private String getCurrentOwnerId() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null && user.getEmail() != null) {
            return user.getEmail();
        }
        return "guest";
    }

    @Override
    public boolean addCity(City city) {
        try {
            String owner = getCurrentOwnerId();
            // Проверяем, есть ли уже такой город у ЭТОГО пользователя
            if (isFavorite(city.getName())) {
                return false;
            }

            // Создаем сущность с ownerId
            CityEntity entity = new CityEntity(0, city.getName(), city.getCountry(), owner);
            db.cityDao().insert(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeCity(City city) {
        try {
            String owner = getCurrentOwnerId();
            // Ищем город именно у этого пользователя
            CityEntity existing = db.cityDao().getByNameAndUser(city.getName(), owner);

            if (existing != null) {
                db.cityDao().delete(existing);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<City> getFavorites() {
        List<City> result = new ArrayList<>();
        try {
            String owner = getCurrentOwnerId();
            // Загружаем только города текущего пользователя
            List<CityEntity> entities = db.cityDao().getAllForUser(owner);
            for (CityEntity entity : entities) {
                result.add(entity.toDomain());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean isFavorite(String cityName) {
        try {
            String owner = getCurrentOwnerId();
            return db.cityDao().getByNameAndUser(cityName, owner) != null;
        } catch (Exception e) {
            return false;
        }
    }
}