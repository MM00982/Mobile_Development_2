package ru.mirea.musin.fragmentsapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Task3Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task3); // Layout с контейнером (как в Task1)

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new InfoFragment())
                    .commit();
        }
    }
}