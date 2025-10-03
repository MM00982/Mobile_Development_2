package ru.mirea.musin.travelweather.domain.repository;

import ru.mirea.musin.travelweather.domain.models.User;

public interface AuthRepository {
    User login(String email, String password);
}