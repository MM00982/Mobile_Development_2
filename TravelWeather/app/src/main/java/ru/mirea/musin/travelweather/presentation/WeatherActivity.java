package ru.mirea.musin.travelweather.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ru.mirea.musin.data.database.AppDatabase;
import ru.mirea.musin.data.repository.FavoritesRepositoryImpl;
import ru.mirea.musin.data.repository.WeatherRepositoryImpl;
import ru.mirea.musin.travelweather.R;
import ru.mirea.musin.domain.models.City;
import ru.mirea.musin.domain.models.WeatherNow;
import ru.mirea.musin.domain.usecases.AddCityToFavorites;
import ru.mirea.musin.domain.usecases.RemoveCityFromFavorites;

public class WeatherActivity extends AppCompatActivity {

    private boolean isFavorite = false;
    private FavoritesRepositoryImpl favRepo;
    private Button btnAction;
    private String finalCityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        String cityName = getIntent().getStringExtra("CITY_NAME");
        if (cityName == null) cityName = "Москва";
        finalCityName = cityName;

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "weather-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        favRepo = new FavoritesRepositoryImpl(db);

        // UI Элементы
        TextView tvTitle = findViewById(R.id.tvCityTitle);
        TextView tvTemp = findViewById(R.id.tvTempBig);
        TextView tvCondition = findViewById(R.id.tvCondition);
        TextView tvDate = findViewById(R.id.tvDate);
        ImageView ivBigSun = findViewById(R.id.ivBigSun); // Главная иконка
        btnAction = findViewById(R.id.btnMap);

        LinearLayout forecastContainer = findViewById(R.id.forecastContainer);
        LinearLayout hourlyContainer = findViewById(R.id.hourlyContainer); // Новый контейнер

        // Детали (TextView с ID)
        TextView tvHumidity = findViewById(R.id.tvHum);
        TextView tvRain = findViewById(R.id.tvRain);
        TextView tvWind = findViewById(R.id.tvWind);

        tvTitle.setText(finalCityName);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM • HH:mm", new Locale("ru"));
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        tvDate.setText("Сегодня, " + sdf.format(new Date()));

        checkFavoriteStatus();

        WeatherRepositoryImpl weatherRepo = new WeatherRepositoryImpl();
        new Thread(() -> {
            WeatherNow weather = weatherRepo.getForecastByCity(finalCityName);
            List<WeatherNow> allForecasts = weatherRepo.get5DayForecast(finalCityName);

            runOnUiThread(() -> {
                if (weather == null) {
                    Toast.makeText(this, "Город не найден", Toast.LENGTH_SHORT).show();
                    finish(); return;
                }

                tvTemp.setText(String.format("%.1f°", weather.getTempC()));

                // ПЕРЕДАЕМ isDay В МЕТОД ВЫБОРА ИКОНКИ
                ivBigSun.setImageResource(getIconByCondition(weather.getCondition(), weather.isDay()));

                // ... заполнение деталей (влага/ветер) ...

                // 2. ПОЧАСОВОЙ ПРОГНОЗ (Берем первые 8 элементов из прогноза - это ближайшие 24 часа)
                hourlyContainer.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(this);
                SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
                hourFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow")); // Или UTC города

                int hourlyCount = 0;
                for(WeatherNow item : allForecasts) {
                    if (hourlyCount >= 6) break; // Берем только 6 слотов

                    View view = inflater.inflate(R.layout.item_hourly, hourlyContainer, false);
                    TextView tvHour = view.findViewById(R.id.tvHour);
                    TextView tvHourTemp = view.findViewById(R.id.tvHourTemp);
                    ImageView ivHourIcon = view.findViewById(R.id.ivHourIcon);

                    Date d = new Date((long)item.getCityId() * 1000);
                    tvHour.setText(hourFormat.format(d));
                    tvHourTemp.setText(String.format("%.0f°", item.getTempC()));

                    // Тут тоже передаем isDay конкретного часа
                    ivHourIcon.setImageResource(getIconByCondition(item.getCondition(), item.isDay()));

                    hourlyContainer.addView(view);
                    hourlyCount++;
                }

                // 3. ПРОГНОЗ НА 5 ДНЕЙ (Фильтруем, берем один раз в сутки, например полдень)
                forecastContainer.removeAllViews();
                SimpleDateFormat dayFormat = new SimpleDateFormat("EE", new Locale("ru"));

                String lastDay = "";
                int daysCount = 0;

                for (WeatherNow item : allForecasts) {
                    Date d = new Date((long)item.getCityId() * 1000);
                    String dayStr = dayFormat.format(d).toUpperCase();

                    // Простая логика: если день сменился, добавляем в список
                    if (!dayStr.equals(lastDay)) {
                        if (daysCount >= 5) break;

                        View view = inflater.inflate(R.layout.item_forecast_row, forecastContainer, false);
                        TextView tvDay = view.findViewById(R.id.tvDay);
                        TextView tvDesc = view.findViewById(R.id.tvDesc);
                        TextView tvTempSmall = view.findViewById(R.id.tvTempSmall);
                        ImageView ivIcon = view.findViewById(R.id.ivForecastIcon);

                        tvDay.setText(dayStr);
                        String cond = item.getCondition().split("\\|")[0]; // Убираем влажность
                        tvDesc.setText(cond);
                        tvTempSmall.setText(String.format("%.0f°", item.getTempC()));

                        // В прогнозе на дни обычно показывают дневную иконку, но можно использовать item.isDay()
                        ivIcon.setImageResource(getIconByCondition(cond, true)); // Для списка дней форсируем "День"

                        forecastContainer.addView(view);
                        lastDay = dayStr;
                        daysCount++;
                    }
                }
            });
        }).start();

        btnAction.setOnClickListener(v -> {
            City cityObj = new City(finalCityName.hashCode(), finalCityName, "RU");
            if (isFavorite) {
                new RemoveCityFromFavorites(favRepo).execute(cityObj);
                Toast.makeText(this, "Удалено из избранного", Toast.LENGTH_SHORT).show();
            } else {
                new AddCityToFavorites(favRepo).execute(cityObj);
                Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();
            }
            checkFavoriteStatus();
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void checkFavoriteStatus() {
        isFavorite = favRepo.isFavorite(finalCityName);
        if (isFavorite) {
            btnAction.setText("Удалить из избранного");
            btnAction.setBackgroundColor(0xFFCC0000);
        } else {
            btnAction.setText("В избранное");
            btnAction.setBackgroundColor(0xFF2C2C2C);
        }
    }

    // ЛОГИКА ВЫБОРА ИКОНКИ
    private int getIconByCondition(String condition, boolean isDay) {
        String c = condition.toLowerCase();

        // 1. Гроза (Thunderstorm)
        if (c.contains("гроз") || c.contains("storm") || c.contains("thunder")) {
            return R.drawable.ic_storm;
        }

        // 2. Снег (Snow)
        // Также проверяем "sleet" (мокрый снег) и "blizzard" (метель)
        if (c.contains("снег") || c.contains("snow") || c.contains("sleet") || c.contains("blizzard") || c.contains("ice")) {
            return R.drawable.ic_cloud_snow;
        }

        // 3. Дождь (Rain, Drizzle)
        if (c.contains("дождь") || c.contains("rain") || c.contains("drizzle") || c.contains("shower")) {
            return R.drawable.ic_rain;
        }

        // 4. Туман / Мгла (Fog, Mist, Haze)
        if (c.contains("туман") || c.contains("fog") || c.contains("mist") || c.contains("haze") || c.contains("мгла")) {
            return R.drawable.ic_fog;
        }

        // 5. Пасмурно (Overcast) - Глухие облака
        if (c.contains("пасмурно") || c.contains("overcast")) {
            return R.drawable.ic_cloud; // Просто серая туча
        }

        // 6. Облачно с прояснениями / Переменная облачность (Clouds)
        if (c.contains("обла") || c.contains("cloud")) {
            return isDay ? R.drawable.ic_cloud_sun : R.drawable.ic_cloud_moon;
        }

        // 7. Ясно (Clear)
        if (c.contains("ясно") || c.contains("clear")) {
            return isDay ? R.drawable.ic_sun_yellow : R.drawable.ic_moon;
        }

        // Если пришло что-то непонятное, ориентируемся на время суток
        return isDay ? R.drawable.ic_sun_yellow : R.drawable.ic_moon;
    }
}