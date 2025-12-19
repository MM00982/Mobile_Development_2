package ru.mirea.musin.travelweather.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import ru.mirea.musin.data.database.AppDatabase;
import ru.mirea.musin.data.repository.FavoritesRepositoryImpl;
import ru.mirea.musin.data.repository.VoiceRepositoryImpl;
import ru.mirea.musin.travelweather.R;
import ru.mirea.musin.domain.models.City;
import ru.mirea.musin.domain.usecases.GetFavorites;
import ru.mirea.musin.domain.usecases.RecognizeVoiceToCity;
import ru.mirea.musin.domain.usecases.RemoveCityFromFavorites;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView etSearch;
    private LinearLayout citiesContainer;
    private AppDatabase db;
    private FavoritesRepositoryImpl favRepo;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "weather-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        favRepo = new FavoritesRepositoryImpl(db);

        citiesContainer = findViewById(R.id.citiesContainer);
        etSearch = findViewById(R.id.etSearch);
        ImageButton btnMic = findViewById(R.id.btnMic);

        // --- НОВАЯ НИЖНЯЯ ПАНЕЛЬ ---
        // НИЖНЯЯ ПАНЕЛЬ
        View navHome = findViewById(R.id.navHome);
        View navProfile = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {
            // Мы уже тут, ничего не делаем
        });

        navProfile.setOnClickListener(v -> {
            Intent intent;
            if (auth.getCurrentUser() != null) {
                intent = new Intent(this, ProfileActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(0, 0); // <--- ВАЖНО
        });

        // --- ЛОГИКА КАРТОЧКИ ТЕКУЩЕГО ГОРОДА ---
        View cardCurrent = findViewById(R.id.cardCurrent);
        TextView tvCityName = findViewById(R.id.tvCityName);

        cardCurrent.setOnClickListener(v -> {
            String city = tvCityName.getText().toString();
            openWeather(city);
        });

        // --- ЛОГИКА ПОИСКА ---
        String[] cities = getResources().getStringArray(R.array.cities_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, cities);
        etSearch.setAdapter(adapter);

        etSearch.setOnItemClickListener((parent, view, position, id) -> {
            String city = (String) parent.getItemAtPosition(position);
            openWeather(city);
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                String query = etSearch.getText().toString().trim();
                if (!query.isEmpty()) openWeather(query);
                return true;
            }
            return false;
        });

        // --- ЛОГИКА МИКРОФОНА ---
        btnMic.setOnClickListener(v -> {
            if (auth.getCurrentUser() == null) {
                Toast.makeText(this, "Доступно только авторизованным пользователям", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Слушаю...", Toast.LENGTH_SHORT).show();
            new Thread(() -> {
                VoiceRepositoryImpl voiceRepo = new VoiceRepositoryImpl();
                RecognizeVoiceToCity useCase = new RecognizeVoiceToCity(voiceRepo);
                String recognizedCity = useCase.execute(new byte[0]);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Распознано: " + recognizedCity, Toast.LENGTH_SHORT).show();
                    openWeather(recognizedCity);
                });
            }).start();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLists();
    }

    private void refreshLists() {
        citiesContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        List<City> favorites = new GetFavorites(favRepo).execute();

        if (!favorites.isEmpty()) {
            addHeader(inflater, "СОХРАНЕННЫЕ");
            for (City city : favorites) {
                View itemView = inflater.inflate(R.layout.item_city_removable, citiesContainer, false);
                TextView tvName = itemView.findViewById(R.id.tvCityNameItem);
                TextView tvCountry = itemView.findViewById(R.id.tvCountryItem);
                View btnDelete = itemView.findViewById(R.id.btnDelete);
                View containerInfo = itemView.findViewById(R.id.containerInfo);

                tvName.setText(city.getName());
                tvCountry.setText("Сохранено");

                containerInfo.setOnClickListener(v -> openWeather(city.getName()));

                btnDelete.setOnClickListener(v -> {
                    new RemoveCityFromFavorites(favRepo).execute(city);
                    refreshLists();
                });

                citiesContainer.addView(itemView);
            }
        }

        addHeader(inflater, "ПОПУЛЯРНЫЕ ГОРОДА");
        String[] popularCities = {"Москва", "Лондон", "Токио", "Нью-Йорк", "Париж"};

        for (String popCity : popularCities) {
            boolean isAlreadyFav = false;
            for (City c : favorites) if (c.getName().equalsIgnoreCase(popCity)) isAlreadyFav = true;
            if (isAlreadyFav) continue;

            View itemView = inflater.inflate(R.layout.item_city_card, citiesContainer, false);
            TextView tvName = itemView.findViewById(R.id.tvCityNameItem);
            TextView tvCountry = itemView.findViewById(R.id.tvCountryItem);

            tvName.setText(popCity);
            tvCountry.setText("Популярное");

            itemView.setOnClickListener(v -> openWeather(popCity));
            citiesContainer.addView(itemView);
        }
    }

    private void addHeader(LayoutInflater inflater, String text) {
        TextView header = new TextView(this);
        header.setText(text);
        header.setTextSize(12);
        header.setTextColor(0xFF757575);
        header.setTypeface(null, android.graphics.Typeface.BOLD);
        header.setPadding(0, 30, 0, 15);
        citiesContainer.addView(header);
    }

    private void openWeather(String cityName) {
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("CITY_NAME", cityName);
        startActivity(intent);
        etSearch.setText("");
    }
}