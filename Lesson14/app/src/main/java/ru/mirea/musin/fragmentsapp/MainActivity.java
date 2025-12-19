package ru.mirea.musin.fragmentsapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnTask1).setOnClickListener(v -> startActivity(new Intent(this, Task1Activity.class)));
        findViewById(R.id.btnTask2).setOnClickListener(v -> startActivity(new Intent(this, Task2Activity.class)));
        findViewById(R.id.btnTask3).setOnClickListener(v -> startActivity(new Intent(this, Task3Activity.class)));
        findViewById(R.id.btnTask4).setOnClickListener(v -> startActivity(new Intent(this, Task4Activity.class)));
    }
}