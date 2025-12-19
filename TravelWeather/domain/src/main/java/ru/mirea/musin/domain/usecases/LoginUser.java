package ru.mirea.musin.domain.usecases;

import ru.mirea.musin.domain.models.User;
import ru.mirea.musin.domain.repository.AuthRepository;

public class LoginUser {
    private final AuthRepository repo;
    public LoginUser(AuthRepository repo) { this.repo = repo; }
    public User execute(String email, String password) { return repo.login(email, password); }
}