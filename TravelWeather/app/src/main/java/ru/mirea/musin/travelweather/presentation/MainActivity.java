package ru.mirea.musin.travelweather.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import ru.mirea.musin.travelweather.R;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView etSearch;
    private RecyclerView recyclerView;
    private CityAdapter cityAdapter;
    private FirebaseAuth auth;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        etSearch = findViewById(R.id.etSearch);
        ImageButton btnMic = findViewById(R.id.btnMic);
        recyclerView = findViewById(R.id.recyclerView);

        // --- НАСТРОЙКА СПИСКА ---
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cityAdapter = new CityAdapter(
                city -> openWeather(city.getName()),
                city -> viewModel.removeCity(city)
        );
        recyclerView.setAdapter(cityAdapter);

        // --- VIEW MODEL ---
        viewModel = new ViewModelProvider(this, new ViewModelFactory(this)).get(MainViewModel.class);

        // Подписка на общий контент (Сохраненные + Популярные)
        viewModel.getContent().observe(this, list -> {
            cityAdapter.setItems(list);

            // Если список совсем пуст, скрываем заголовок
            View tvSaved = findViewById(R.id.tvSavedLabel);
            if(list == null || list.isEmpty()) {
                tvSaved.setVisibility(View.GONE);
            } else {
                tvSaved.setVisibility(View.VISIBLE);
                // Можно менять текст заголовка, но "СОХРАНЕННЫЕ И ПОПУЛЯРНЫЕ" длинно
                // Оставим просто заголовок, или можно убрать его в XML
            }
        });

        viewModel.getVoiceResult().observe(this, recognizedCity -> {
            Toast.makeText(this, "Распознано: " + recognizedCity, Toast.LENGTH_SHORT).show();
            openWeather(recognizedCity);
        });

        // --- UI ---
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
            viewModel.recognizeSpeech();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.loadContent(); // Загружаем контент
    }

    private void openWeather(String cityName) {
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("CITY_NAME", cityName);
        startActivity(intent);
        etSearch.setText("");
    }
}