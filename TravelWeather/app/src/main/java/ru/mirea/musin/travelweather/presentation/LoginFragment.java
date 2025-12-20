package ru.mirea.musin.travelweather.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.firebase.auth.FirebaseAuth;
import ru.mirea.musin.travelweather.R;

public class LoginFragment extends Fragment {

    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();

        // Авто-вход
        if (auth.getCurrentUser() != null) {
            Navigation.findNavController(view).navigate(R.id.action_login_to_home);
            return;
        }

        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPass = view.findViewById(R.id.etPass);
        Button btnLogin = view.findViewById(R.id.btnLogin);
        TextView tvRegister = view.findViewById(R.id.tvRegisterLabel);

        // Кнопка Гость (если есть в разметке, иначе удали этот блок)
        View btnGuest = view.findViewById(R.id.btnGuest);
        if (btnGuest != null) {
            btnGuest.setOnClickListener(v ->
                    Navigation.findNavController(view).navigate(R.id.action_login_to_home)
            );
        }

        tvRegister.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_login_to_register);
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPass.getText().toString().trim();

            if(email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(getContext(), "Заполните поля", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            Navigation.findNavController(view).navigate(R.id.action_login_to_home);
                        } else {
                            String msg = task.getException() != null ? task.getException().getMessage() : "Ошибка";
                            Toast.makeText(getContext(), "Ошибка: " + msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}