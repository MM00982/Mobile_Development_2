package ru.mirea.musin.domain.models;

public class WeatherNow {
    private final int cityId;
    private final double tempC;
    private final String condition;
    private final boolean isDay;
    private final String iconId; // НОВОЕ ПОЛЕ

    // Основной конструктор
    public WeatherNow(int cityId, double tempC, String condition, boolean isDay, String iconId) {
        this.cityId = cityId;
        this.tempC = tempC;
        this.condition = condition;
        this.isDay = isDay;
        this.iconId = iconId;
    }

    // Для совместимости (если где-то используется старый)
    public WeatherNow(int cityId, double tempC, String condition, boolean isDay) {
        this(cityId, tempC, condition, isDay, "01d");
    }

    public int getCityId() { return cityId; }
    public double getTempC() { return tempC; }
    public String getCondition() { return condition; }
    public boolean isDay() { return isDay; }
    public String getIconId() { return iconId; } // Геттер
}