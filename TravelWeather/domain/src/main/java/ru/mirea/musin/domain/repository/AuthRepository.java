package ru.mirea.musin.domain.repository;
import ru.mirea.musin.domain.models.User;

public interface AuthRepository {
    User login(String email, String password);
    User register(String email, String password); // <--- НОВЫЙ МЕТОД
    void logout();
}