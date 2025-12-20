package ru.mirea.musin.navigationapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import ru.mirea.musin.navigationapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnFragmentManager.setOnClickListener(v ->
                startActivity(new Intent(this, FragmentManagerActivity.class)));

        binding.btnBottomNav.setOnClickListener(v ->
                startActivity(new Intent(this, BottomNavActivity.class)));

        binding.btnDrawer.setOnClickListener(v ->
                startActivity(new Intent(this, DrawerActivity.class)));
    }
}