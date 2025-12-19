package ru.mirea.musin.data.network.model;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    @SerializedName("main") public Main main;
    @SerializedName("weather") public List<Weather> weather;
    @SerializedName("wind") public Wind wind;
    @SerializedName("dt") public long dt;
    @SerializedName("name") public String name;
    @SerializedName("sys") public Sys sys; // <--- ДОБАВИЛИ

    public static class Main {
        public double temp;
        public double humidity;
    }
    public static class Weather {
        public String description;
        public String main;
    }
    public static class Wind {
        public double speed;
    }

    // <--- НОВЫЙ КЛАСС
    public static class Sys {
        public long sunrise;
        public long sunset;
        public String pod; // "d" - day, "n" - night (есть в прогнозе)
    }
}