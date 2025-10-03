package ru.mirea.musin.travelweather.data.repository;

import ru.mirea.musin.travelweather.domain.models.User;
import ru.mirea.musin.travelweather.domain.repository.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {
    @Override
    public User login(String email, String password) {
        return new User(1, "Demo User"); // мок
    }
}