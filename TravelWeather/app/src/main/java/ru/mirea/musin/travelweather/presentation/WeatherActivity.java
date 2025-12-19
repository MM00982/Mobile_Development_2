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
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ru.mirea.musin.travelweather.R;
import ru.mirea.musin.domain.models.WeatherNow;

public class WeatherActivity extends AppCompatActivity {

    private Button btnAction;
    private String finalCityName;
    private WeatherViewModel viewModel; // ViewModel

    // UI Элементы (поля класса, чтобы видеть их в Observer)
    private TextView tvTemp, tvDate;
    private ImageView ivBigSun;
    private LinearLayout forecastContainer, hourlyContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        String cityName = getIntent().getStringExtra("CITY_NAME");
        if (cityName == null) cityName = "Москва";
        finalCityName = cityName;

        // 1. Инициализация ViewModel
        viewModel = new ViewModelProvider(this, new ViewModelFactory(this)).get(WeatherViewModel.class);

        initViews();

        TextView tvTitle = findViewById(R.id.tvCityTitle);
        tvTitle.setText(finalCityName);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM • HH:mm", new Locale("ru"));
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        tvDate.setText("Сегодня, " + sdf.format(new Date()));

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Кнопка Избранного вызывает метод ViewModel
        btnAction.setOnClickListener(v -> {
            viewModel.toggleFavorite(finalCityName);
        });

        // 2. ПОДПИСКА НА ДАННЫЕ (LIVEDATA)

        // Текущая погода
        viewModel.getCurrentWeather().observe(this, weather -> {
            if (weather == null) return;
            tvTemp.setText(String.format("%.1f°", weather.getTempC()));
            ivBigSun.setImageResource(getIconByCondition(weather.getCondition(), weather.isDay()));
        });

        // Прогноз (почасовой + 5 дней)
        viewModel.getForecast().observe(this, allForecasts -> {
            fillHourlyForecast(allForecasts);
            fillDailyForecast(allForecasts);
        });

        // Статус избранного (меняет цвет кнопки)
        viewModel.getIsFavorite().observe(this, isFav -> {
            if (isFav) {
                btnAction.setText("Удалить из избранного");
                btnAction.setBackgroundColor(0xFFCC0000);
            } else {
                btnAction.setText("В избранное");
                btnAction.setBackgroundColor(0xFF2C2C2C);
            }
        });

        // Ошибки
        viewModel.getError().observe(this, errorMsg -> {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        });

        // 3. Запуск загрузки
        viewModel.loadWeather(finalCityName);
    }

    private void initViews() {
        tvTemp = findViewById(R.id.tvTempBig);
        tvDate = findViewById(R.id.tvDate);
        ivBigSun = findViewById(R.id.ivBigSun);
        btnAction = findViewById(R.id.btnMap);
        forecastContainer = findViewById(R.id.forecastContainer);
        hourlyContainer = findViewById(R.id.hourlyContainer);
    }

    private void fillHourlyForecast(List<WeatherNow> allForecasts) {
        hourlyContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        hourFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        int hourlyCount = 0;
        for(WeatherNow item : allForecasts) {
            if (hourlyCount >= 6) break;

            View view = inflater.inflate(R.layout.item_hourly, hourlyContainer, false);
            TextView tvHour = view.findViewById(R.id.tvHour);
            TextView tvHourTemp = view.findViewById(R.id.tvHourTemp);
            ImageView ivHourIcon = view.findViewById(R.id.ivHourIcon);

            Date d = new Date((long)item.getCityId() * 1000);
            tvHour.setText(hourFormat.format(d));
            tvHourTemp.setText(String.format("%.0f°", item.getTempC()));
            ivHourIcon.setImageResource(getIconByCondition(item.getCondition(), item.isDay()));

            hourlyContainer.addView(view);
            hourlyCount++;
        }
    }

    private void fillDailyForecast(List<WeatherNow> allForecasts) {
        forecastContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EE", new Locale("ru"));

        String lastDay = "";
        int daysCount = 0;

        for (WeatherNow item : allForecasts) {
            Date d = new Date((long)item.getCityId() * 1000);
            String dayStr = dayFormat.format(d).toUpperCase();

            if (!dayStr.equals(lastDay)) {
                if (daysCount >= 5) break;

                View view = inflater.inflate(R.layout.item_forecast_row, forecastContainer, false);
                TextView tvDay = view.findViewById(R.id.tvDay);
                TextView tvDesc = view.findViewById(R.id.tvDesc);
                TextView tvTempSmall = view.findViewById(R.id.tvTempSmall);
                ImageView ivIcon = view.findViewById(R.id.ivForecastIcon);

                tvDay.setText(dayStr);
                String cond = item.getCondition().split("\\|")[0];
                tvDesc.setText(cond);
                tvTempSmall.setText(String.format("%.0f°", item.getTempC()));
                ivIcon.setImageResource(getIconByCondition(cond, true));

                forecastContainer.addView(view);
                lastDay = dayStr;
                daysCount++;
            }
        }
    }

    private int getIconByCondition(String condition, boolean isDay) {
        String c = condition.toLowerCase();
        if (c.contains("гроз") || c.contains("storm") || c.contains("thunder")) return R.drawable.ic_storm;
        if (c.contains("снег") || c.contains("snow") || c.contains("sleet") || c.contains("blizzard") || c.contains("ice")) return R.drawable.ic_cloud_snow;
        if (c.contains("дождь") || c.contains("rain") || c.contains("drizzle") || c.contains("shower")) return R.drawable.ic_rain;
        if (c.contains("туман") || c.contains("fog") || c.contains("mist") || c.contains("haze") || c.contains("мгла")) return R.drawable.ic_fog;
        if (c.contains("пасмурно") || c.contains("overcast")) return R.drawable.ic_cloud;
        if (c.contains("обла") || c.contains("cloud")) return isDay ? R.drawable.ic_cloud_sun : R.drawable.ic_cloud_moon;
        if (c.contains("ясно") || c.contains("clear")) return isDay ? R.drawable.ic_sun_yellow : R.drawable.ic_moon;
        return isDay ? R.drawable.ic_sun_yellow : R.drawable.ic_moon;
    }
}