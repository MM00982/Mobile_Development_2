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
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import ru.mirea.musin.travelweather.R;
import ru.mirea.musin.domain.models.City;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView etSearch;
    private LinearLayout citiesContainer;
    private FirebaseAuth auth;
    private MainViewModel viewModel; // Ссылка на ViewModel

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        citiesContainer = findViewById(R.id.citiesContainer);
        etSearch = findViewById(R.id.etSearch);
        ImageButton btnMic = findViewById(R.id.btnMic);

        // 1. Инициализация ViewModel через Фабрику
        viewModel = new ViewModelProvider(this, new ViewModelFactory(this)).get(MainViewModel.class);

        // 2. Подписка на данные (LiveData)
        viewModel.getFavorites().observe(this, favoritesList -> {
            // Как только данные изменятся, этот код выполнится сам
            refreshUI(favoritesList);
        });

        viewModel.getVoiceResult().observe(this, recognizedCity -> {
            Toast.makeText(this, "Распознано: " + recognizedCity, Toast.LENGTH_SHORT).show();
            openWeather(recognizedCity);
        });

        // --- UI Логика ---
        View navHome = findViewById(R.id.navHome);
        View navProfile = findViewById(R.id.navProfile);

        navHome.setOnClickListener(v -> {});

        navProfile.setOnClickListener(v -> {
            Intent intent;
            if (auth.getCurrentUser() != null) {
                intent = new Intent(this, ProfileActivity.class);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        View cardCurrent = findViewById(R.id.cardCurrent);
        TextView tvCityName = findViewById(R.id.tvCityName);

        cardCurrent.setOnClickListener(v -> {
            String city = tvCityName.getText().toString();
            openWeather(city);
        });

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

        btnMic.setOnClickListener(v -> {
            if (auth.getCurrentUser() == null) {
                Toast.makeText(this, "Доступно только авторизованным пользователям", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Слушаю...", Toast.LENGTH_SHORT).show();
            // Вызов метода ViewModel
            viewModel.recognizeSpeech();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadFavorites();
    }

    private void refreshUI(List<City> favorites) {
        citiesContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (favorites != null && !favorites.isEmpty()) {
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

                // Удаление через ViewModel
                btnDelete.setOnClickListener(v -> {
                    viewModel.removeCity(city);
                });

                citiesContainer.addView(itemView);
            }
        }

        addHeader(inflater, "ПОПУЛЯРНЫЕ ГОРОДА");
        String[] popularCities = {"Москва", "Лондон", "Токио", "Нью-Йорк", "Париж"};

        for (String popCity : popularCities) {
            boolean isAlreadyFav = false;
            if (favorites != null) {
                for (City c : favorites) if (c.getName().equalsIgnoreCase(popCity)) isAlreadyFav = true;
            }
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