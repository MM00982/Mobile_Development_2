package ru.mirea.musin.fragmentsapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Важно: мы добавляем "implements FragmentListener"
public class Task4Activity extends AppCompatActivity implements FragmentListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Мы используем разметку от 3-го задания, чтобы не плодить одинаковые файлы
        setContentView(R.layout.activity_task3);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ClickFragment())
                    .commit();
        }
    }

    @Override
    public void sendResult(String message) {
        Toast.makeText(this, "Activity получила: " + message, Toast.LENGTH_SHORT).show();
    }
}