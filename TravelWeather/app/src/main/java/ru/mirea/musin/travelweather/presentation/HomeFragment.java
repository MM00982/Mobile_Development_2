package ru.mirea.musin.travelweather.presentation;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import ru.mirea.musin.travelweather.R;

public class HomeFragment extends Fragment {

    private MainViewModel viewModel;
    private CityAdapter cityAdapter;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Подключаем наш XML файл
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();

        // Инициализация ViewModel (context берем через requireContext())
        viewModel = new ViewModelProvider(this, new ViewModelFactory(requireContext())).get(MainViewModel.class);

        setupRecyclerView(view);
        setupSearch(view);
        setupBottomNav(view); // Нижняя панель

        // Подписка на данные
        viewModel.getContent().observe(getViewLifecycleOwner(), list -> {
            cityAdapter.setItems(list);
            // Показать/скрыть заголовок, если список пуст
            View tvSaved = view.findViewById(R.id.tvSavedLabel);
            if (tvSaved != null) {
                tvSaved.setVisibility((list == null || list.isEmpty()) ? View.GONE : View.VISIBLE);
            }
        });

        viewModel.getVoiceResult().observe(getViewLifecycleOwner(), city -> {
            Toast.makeText(getContext(), "Распознано: " + city, Toast.LENGTH_SHORT).show();
            openWeather(city);
        });

        view.findViewById(R.id.btnMic).setOnClickListener(v -> {
            if (auth.getCurrentUser() == null) {
                Toast.makeText(getContext(), "Только для авторизованных", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "Слушаю...", Toast.LENGTH_SHORT).show();
            viewModel.recognizeSpeech();
        });

        view.findViewById(R.id.cardCurrent).setOnClickListener(v -> openWeather("Москва"));
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadContent();
    }

    private void setupRecyclerView(View view) {
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        cityAdapter = new CityAdapter(
                city -> openWeather(city.getName()),
                city -> viewModel.removeCity(city)
        );
        rv.setAdapter(cityAdapter);
    }

    private void setupSearch(View view) {
        AutoCompleteTextView etSearch = view.findViewById(R.id.etSearch);
        String[] cities = getResources().getStringArray(R.array.cities_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, cities);
        etSearch.setAdapter(adapter);

        etSearch.setOnItemClickListener((parent, v, position, id) -> {
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
    }

    private void setupBottomNav(View view) {
        // Кнопка "Главная" ничего не делает, мы уже тут

        // Кнопка "Профиль"
        view.findViewById(R.id.navProfile).setOnClickListener(v -> {
            Fragment nextFragment;
            if (auth.getCurrentUser() != null) {
                nextFragment = new ProfileFragment();
            } else {
                nextFragment = new LoginFragment();
            }

            // Навигация
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, nextFragment)
                    .addToBackStack(null) // Чтобы можно было вернуться назад
                    .commit();
        });
    }

    private void openWeather(String cityName) {
        // Переход на WeatherFragment с передачей аргумента
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString("CITY_NAME", cityName);
        fragment.setArguments(args);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // Добавляем в стек
                .commit();
    }
}