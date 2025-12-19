package ru.mirea.musin.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import ru.mirea.musin.domain.models.User;
import ru.mirea.musin.domain.repository.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public User login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password);
        FirebaseUser fbUser = auth.getCurrentUser();
        if (fbUser != null) return new User(1, fbUser.getEmail());
        return new User(0, "Guest");
    }

    // РЕАЛИЗАЦИЯ РЕГИСТРАЦИИ
    @Override
    public User register(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password);
        FirebaseUser fbUser = auth.getCurrentUser();
        if (fbUser != null) return new User(1, fbUser.getEmail());
        return new User(0, "Error");
    }

    @Override
    public void logout() {
        auth.signOut();
    }
}