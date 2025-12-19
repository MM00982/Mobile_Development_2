package ru.mirea.musin.travelweather.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import ru.mirea.musin.travelweather.R;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPass = findViewById(R.id.etPass);
        Button btnReg = findViewById(R.id.btnRegister);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        btnReg.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String pass = etPass.getText().toString();

            if (email.isEmpty() || pass.isEmpty()) return;

            // Используем Firebase напрямую для простоты (или через UseCase)
            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Успешно! Теперь войдите.", Toast.LENGTH_LONG).show();
                            finish(); // Закрываем экран регистрации
                        } else {
                            String error = task.getException() != null ? task.getException().getMessage() : "Ошибка";
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}