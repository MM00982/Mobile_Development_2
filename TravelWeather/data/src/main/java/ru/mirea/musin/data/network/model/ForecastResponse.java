package ru.mirea.musin.data.network.model;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ForecastResponse {
    @SerializedName("list") public List<WeatherResponse> list;
}