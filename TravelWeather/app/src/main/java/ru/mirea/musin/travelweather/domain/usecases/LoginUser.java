package ru.mirea.musin.travelweather.domain.usecases;

import ru.mirea.musin.travelweather.domain.models.User;
import ru.mirea.musin.travelweather.domain.repository.AuthRepository;

public class LoginUser {
    private final AuthRepository repo;
    public LoginUser(AuthRepository repo) { this.repo = repo; }
    public User execute(String email, String password) { return repo.login(email, password); }
}