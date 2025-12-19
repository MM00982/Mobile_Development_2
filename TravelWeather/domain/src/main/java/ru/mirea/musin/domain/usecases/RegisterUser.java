package ru.mirea.musin.domain.usecases;

import ru.mirea.musin.domain.models.User;
import ru.mirea.musin.domain.repository.AuthRepository;

public class RegisterUser {
    private final AuthRepository repo;
    public RegisterUser(AuthRepository repo) { this.repo = repo; }

    public User execute(String email, String password) {
        return repo.register(email, password);
    }
}