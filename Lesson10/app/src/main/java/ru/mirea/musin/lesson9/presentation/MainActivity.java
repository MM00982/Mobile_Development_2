package ru.mirea.musin.lesson9.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.musin.lesson9.R;
import ru.mirea.musin.lesson9.data.repository.MovieRepositoryImpl;
import ru.mirea.musin.lesson9.domain.models.Movie;
import ru.mirea.musin.lesson9.domain.repository.MovieRepository;
import ru.mirea.musin.lesson9.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.musin.lesson9.domain.usecases.SaveMovieToFavoriteUseCase;
import ru.mirea.musin.lesson9.data.storage.MovieStorage;
import ru.mirea.musin.lesson9.data.storage.SharedPrefMovieStorage;

public class MainActivity extends AppCompatActivity {

    private MovieRepository movieRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieStorage storage = new SharedPrefMovieStorage(this);
        MovieRepository movieRepository = new MovieRepositoryImpl(storage);

        TextView textView = findViewById(R.id.textViewMovie);
        EditText editText = findViewById(R.id.editTextMovie);
        Button btnGet = findViewById(R.id.buttonGetMovie);
        Button btnSave = findViewById(R.id.buttonSaveMovie);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String name = editText.getText().toString();
                boolean result = new SaveMovieToFavoriteUseCase(movieRepository)
                        .execute(new Movie(2, name));
                textView.setText(String.format("Save result %s", result));
            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
                textView.setText(movie.getName());
            }
        });
    }
}