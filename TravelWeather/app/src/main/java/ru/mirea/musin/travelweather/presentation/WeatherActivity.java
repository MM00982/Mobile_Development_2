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

import com.squareup.picasso.Picasso;

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
    private WeatherViewModel viewModel;

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

        viewModel = new ViewModelProvider(this, new ViewModelFactory(this)).get(WeatherViewModel.class);

        initViews();

        TextView tvTitle = findViewById(R.id.tvCityTitle);
        tvTitle.setText(finalCityName);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM • HH:mm", new Locale("ru"));
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        tvDate.setText("Сегодня, " + sdf.format(new Date()));

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnAction.setOnClickListener(v -> viewModel.toggleFavorite(finalCityName));

        // --- LIVEDATA ---

        viewModel.getCurrentWeather().observe(this, weather -> {
            if (weather == null) return;
            tvTemp.setText(String.format("%.1f°", weather.getTempC()));

            // --- ЗАДАНИЕ PICASSO ---
            // Загружаем иконку с официального сайта OpenWeather
            String iconUrl = "https://openweathermap.org/img/wn/" + weather.getIconId() + "@4x.png";

            Picasso.get()
                    .load(iconUrl)
                    .placeholder(R.drawable.ic_sun_login) // Пока грузится
                    .error(R.drawable.ic_cloud)           // Если ошибка
                    .into(ivBigSun);
        });

        viewModel.getForecast().observe(this, allForecasts -> {
            fillHourlyForecast(allForecasts);
            fillDailyForecast(allForecasts);
        });

        viewModel.getIsFavorite().observe(this, isFav -> {
            if (isFav) {
                btnAction.setText("Удалить из избранного");
                btnAction.setBackgroundColor(0xFFCC0000);
            } else {
                btnAction.setText("В избранное");
                btnAction.setBackgroundColor(0xFF2C2C2C);
            }
        });

        viewModel.getError().observe(this, errorMsg -> {
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        });

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

            // Picasso для часового прогноза
            String iconUrl = "https://openweathermap.org/img/wn/" + item.getIconId() + "@2x.png";
            Picasso.get().load(iconUrl).into(ivHourIcon);

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

                // Picasso для дневного прогноза
                String iconUrl = "https://openweathermap.org/img/wn/" + item.getIconId() + "@2x.png";
                Picasso.get().load(iconUrl).into(ivIcon);

                forecastContainer.addView(view);
                lastDay = dayStr;
                daysCount++;
            }
        }
    }
}