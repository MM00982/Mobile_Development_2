package ru.mirea.musin.navigationapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import ru.mirea.musin.navigationapp.databinding.ActivityFragmentManagerBinding;

public class FragmentManagerActivity extends AppCompatActivity {
    private ActivityFragmentManagerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFragmentManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // По умолчанию открываем Home
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        binding.btnHome.setOnClickListener(v -> replaceFragment(new HomeFragment()));
        binding.btnInfo.setOnClickListener(v -> replaceFragment(new InfoFragment()));
        binding.btnProfile.setOnClickListener(v -> replaceFragment(new ProfileFragment()));
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}