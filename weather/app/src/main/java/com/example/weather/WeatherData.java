package com.example.weather;

import java.io.Serializable;

public class WeatherData implements Serializable {
    private final String cityName;
    private final double temperature;
    private final double feelsLike;
    private final int humidity;
    private final double windSpeed;
    private final int pressure;
    private final String description;
    private final String icon;

    public WeatherData(String cityName, double temperature, double feelsLike,
                      int humidity, double windSpeed, int pressure,
                      String description, String icon) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.description = description;
        this.icon = icon;
    }

    public String getCityName() { return cityName; }
    public double getTemperature() { return temperature; }
    public double getFeelsLike() { return feelsLike; }
    public int getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    public int getPressure() { return pressure; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
}

