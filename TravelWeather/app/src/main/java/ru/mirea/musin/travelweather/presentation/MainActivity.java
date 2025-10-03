package ru.mirea.musin.travelweather.presentation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ru.mirea.musin.travelweather.R;
import ru.mirea.musin.travelweather.data.repository.AuthRepositoryImpl;
import ru.mirea.musin.travelweather.data.repository.FavoritesRepositoryImpl;
import ru.mirea.musin.travelweather.data.repository.WeatherRepositoryImpl;
import ru.mirea.musin.travelweather.domain.models.City;
import ru.mirea.musin.travelweather.domain.models.WeatherNow;
import ru.mirea.musin.travelweather.domain.repository.AuthRepository;
import ru.mirea.musin.travelweather.domain.repository.FavoritesRepository;
import ru.mirea.musin.travelweather.domain.repository.WeatherRepository;
import ru.mirea.musin.travelweather.domain.usecases.GetCityList;
import ru.mirea.musin.travelweather.domain.usecases.GetForecastByCity;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;
import ru.mirea.musin.travelweather.R;
import ru.mirea.musin.travelweather.data.repository.*;
import ru.mirea.musin.travelweather.domain.models.*;
import ru.mirea.musin.travelweather.domain.repository.*;
import ru.mirea.musin.travelweather.domain.usecases.*;

public class MainActivity extends AppCompatActivity {

    private WeatherRepository weatherRepo;
    private FavoritesRepository favoritesRepo;
    private AuthRepository authRepo;
    private VoiceRepository voiceRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherRepo   = new WeatherRepositoryImpl();
        favoritesRepo = new FavoritesRepositoryImpl();
        authRepo      = new AuthRepositoryImpl();
        voiceRepo     = new VoiceRepositoryImpl();

        TextView tv = findViewById(R.id.tvResult);
        EditText etCity = findViewById(R.id.etCity);
        EditText etFavCity = findViewById(R.id.etFavCity);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            User u = new LoginUser(authRepo).execute("demo@ex", "123");
            tv.setText("Привет, " + u.getName());
        });

        findViewById(R.id.btnCities).setOnClickListener(v -> {
            List<City> list = new GetCityList(weatherRepo).execute();
            StringBuilder sb = new StringBuilder("Города:\n");
            for (City c : list) sb.append("• ").append(c.getName()).append(" (").append(c.getCountry()).append(")\n");
            tv.setText(sb.toString());
        });

        findViewById(R.id.btnSearch).setOnClickListener(v -> {
            String q = etCity.getText().toString();
            List<City> res = new SearchCity(weatherRepo).execute(q);
            StringBuilder sb = new StringBuilder("Результаты поиска:\n");
            for (City c : res) sb.append("• ").append(c.getName()).append("\n");
            tv.setText(sb.toString());
        });

        findViewById(R.id.btnForecast).setOnClickListener(v -> {
            String city = etCity.getText().toString().trim();
            if (city.isEmpty()) city = "Москва";
            WeatherNow w = new GetForecastByCity(weatherRepo).execute(city);
            tv.setText(city + ": " + w.getTempC() + "°C, " + w.getCondition());
        });

        findViewById(R.id.btnRecognize).setOnClickListener(v -> {
            String recognized = new RecognizeVoiceToCity(voiceRepo).execute(new byte[0]);
            tv.setText("Распознано: " + recognized);
            etCity.setText(recognized);
        });

        findViewById(R.id.btnSync).setOnClickListener(v -> {
            boolean ok = new SyncWeatherFromApi(weatherRepo).execute();
            tv.setText("Синхронизация: " + (ok ? "OK" : "Ошибка"));
        });

        findViewById(R.id.btnAddFav).setOnClickListener(v -> {
            String name = etFavCity.getText().toString().trim();
            if (name.isEmpty()) { tv.setText("Введите город для добавления"); return; }
            int id = Math.abs(name.hashCode());
            boolean ok = new AddCityToFavorites(favoritesRepo).execute(new City(id, name, "RU"));
            tv.setText(ok ? "Добавлено" : "Уже было в списке");
        });

        findViewById(R.id.btnRemoveFav).setOnClickListener(v -> {
            String name = etFavCity.getText().toString().trim();
            if (name.isEmpty()) { tv.setText("Введите город для удаления"); return; }
            List<City> favs = new GetFavorites(favoritesRepo).execute();
            City target = null;
            for (City c : favs) if (c.getName().equalsIgnoreCase(name)) { target = c; break; }
            if (target == null) { tv.setText("Город не найден в избранном"); return; }
            boolean ok = new RemoveCityFromFavorites(favoritesRepo).execute(target);
            tv.setText(ok ? "Удалено" : "Не удалось удалить");
        });

        findViewById(R.id.btnGetFav).setOnClickListener(v -> {
            List<City> favs = new GetFavorites(favoritesRepo).execute();
            StringBuilder sb = new StringBuilder("Избранное:\n");
            for (City c : favs) sb.append("• ").append(c.getName()).append("\n");
            tv.setText(sb.toString());
        });
    }
}