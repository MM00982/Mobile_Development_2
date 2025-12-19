package ru.mirea.musin.travelweather.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ru.mirea.musin.travelweather.R;
import ru.mirea.musin.domain.models.WeatherNow;

public class WeatherFragment extends Fragment {

    private WeatherViewModel viewModel;
    private String cityName = "Москва";

    // UI элементы
    private TextView tvTemp;
    private ImageView ivBigSun;
    private LinearLayout forecastContainer, hourlyContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Получаем аргументы (название города)
        if (getArguments() != null) {
            cityName = getArguments().getString("CITY_NAME", "Москва");
        }

        viewModel = new ViewModelProvider(this, new ViewModelFactory(requireContext())).get(WeatherViewModel.class);

        initViews(view);

        TextView tvTitle = view.findViewById(R.id.tvCityTitle);
        tvTitle.setText(cityName);

        TextView tvDate = view.findViewById(R.id.tvDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM • HH:mm", new Locale("ru"));
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        tvDate.setText("Сегодня, " + sdf.format(new Date()));

        // Кнопка Назад - просто удаляем фрагмент из стека
        view.findViewById(R.id.btnBack).setOnClickListener(v -> getParentFragmentManager().popBackStack());

        Button btnAction = view.findViewById(R.id.btnMap);
        btnAction.setOnClickListener(v -> viewModel.toggleFavorite(cityName));

        // --- ПОДПИСКИ ---
        viewModel.getCurrentWeather().observe(getViewLifecycleOwner(), weather -> {
            if (weather == null) return;
            tvTemp.setText(String.format("%.1f°", weather.getTempC()));

            // Picasso
            String iconUrl = "https://openweathermap.org/img/wn/" + weather.getIconId() + "@4x.png";
            Picasso.get().load(iconUrl).placeholder(R.drawable.ic_sun_login).into(ivBigSun);
        });

        viewModel.getForecast().observe(getViewLifecycleOwner(), this::fillForecasts);

        viewModel.getIsFavorite().observe(getViewLifecycleOwner(), isFav -> {
            if (isFav) {
                btnAction.setText("Удалить из избранного");
                btnAction.setBackgroundColor(0xFFCC0000);
            } else {
                btnAction.setText("В избранное");
                btnAction.setBackgroundColor(0xFF2C2C2C);
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), errorMsg ->
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show()
        );

        viewModel.loadWeather(cityName);
    }

    private void initViews(View view) {
        tvTemp = view.findViewById(R.id.tvTempBig);
        ivBigSun = view.findViewById(R.id.ivBigSun);
        forecastContainer = view.findViewById(R.id.forecastContainer);
        hourlyContainer = view.findViewById(R.id.hourlyContainer);
    }

    private void fillForecasts(List<WeatherNow> allForecasts) {
        if (getContext() == null) return;

        // Почасовой
        hourlyContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        hourFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        int hourlyCount = 0;
        for(WeatherNow item : allForecasts) {
            if (hourlyCount >= 6) break;
            View v = inflater.inflate(R.layout.item_hourly, hourlyContainer, false);

            ((TextView)v.findViewById(R.id.tvHour)).setText(hourFormat.format(new Date((long)item.getCityId()*1000)));
            ((TextView)v.findViewById(R.id.tvHourTemp)).setText(String.format("%.0f°", item.getTempC()));

            ImageView iv = v.findViewById(R.id.ivHourIcon);
            Picasso.get().load("https://openweathermap.org/img/wn/" + item.getIconId() + "@2x.png").into(iv);

            hourlyContainer.addView(v);
            hourlyCount++;
        }

        // По дням
        forecastContainer.removeAllViews();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EE", new Locale("ru"));
        String lastDay = "";
        int daysCount = 0;

        for (WeatherNow item : allForecasts) {
            Date d = new Date((long)item.getCityId() * 1000);
            String dayStr = dayFormat.format(d).toUpperCase();

            if (!dayStr.equals(lastDay)) {
                if (daysCount >= 5) break;
                View v = inflater.inflate(R.layout.item_forecast_row, forecastContainer, false);

                ((TextView)v.findViewById(R.id.tvDay)).setText(dayStr);
                ((TextView)v.findViewById(R.id.tvDesc)).setText(item.getCondition());
                ((TextView)v.findViewById(R.id.tvTempSmall)).setText(String.format("%.0f°", item.getTempC()));

                ImageView iv = v.findViewById(R.id.ivForecastIcon);
                Picasso.get().load("https://openweathermap.org/img/wn/" + item.getIconId() + "@2x.png").into(iv);

                forecastContainer.addView(v);
                lastDay = dayStr;
                daysCount++;
            }
        }
    }
}