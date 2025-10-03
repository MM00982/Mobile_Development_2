package ru.mirea.musin.travelweather.domain.models;

public class WeatherNow {
    private final int cityId;
    private final double tempC;
    private final String condition;

    public WeatherNow(int cityId, double tempC, String condition) {
        this.cityId = cityId;
        this.tempC = tempC;
        this.condition = condition;
    }
    public int getCityId() { return cityId; }
    public double getTempC() { return tempC; }
    public String getCondition() { return condition; }
}
