package ru.mirea.musin.domain.models;

public class WeatherNow {
    private final int cityId;
    private final double tempC;
    private final String condition;
    private final boolean isDay; // <--- НОВОЕ ПОЛЕ

    // Старый конструктор (для совместимости)
    public WeatherNow(int cityId, double tempC, String condition) {
        this(cityId, tempC, condition, true); // По умолчанию день
    }

    // Новый конструктор
    public WeatherNow(int cityId, double tempC, String condition, boolean isDay) {
        this.cityId = cityId;
        this.tempC = tempC;
        this.condition = condition;
        this.isDay = isDay;
    }

    public int getCityId() { return cityId; }
    public double getTempC() { return tempC; }
    public String getCondition() { return condition; }
    public boolean isDay() { return isDay; } // Геттер
}