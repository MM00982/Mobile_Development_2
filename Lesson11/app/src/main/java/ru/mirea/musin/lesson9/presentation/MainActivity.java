package ru.mirea.musin.lesson9.presentation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.musin.lesson9.R;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textViewMovie);
        EditText editText = findViewById(R.id.editTextMovie);
        Button btnGet = findViewById(R.id.buttonGetMovie);
        Button btnSave = findViewById(R.id.buttonSaveMovie);

        mainViewModel = new ViewModelProvider(this, new ViewModelFactory(this))
                .get(MainViewModel.class);

        mainViewModel.getFavoriteMovie().observe(this, text -> {

            textView.setText(text);
        });

        btnSave.setOnClickListener(v -> {
            mainViewModel.saveMovie(editText.getText().toString());
        });

        btnGet.setOnClickListener(v -> {
            mainViewModel.getMovie();
        });
    }
}