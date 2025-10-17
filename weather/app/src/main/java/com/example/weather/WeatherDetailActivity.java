package com.example.weather;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class WeatherDetailActivity extends AppCompatActivity {

    private TextView cityNameText;
    private TextView temperatureText;
    private TextView descriptionText;
    private TextView feelsLikeText;
    private TextView humidityText;
    private TextView windSpeedText;
    private TextView pressureText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        displayWeatherData();
    }

    private void initViews() {
        cityNameText = findViewById(R.id.cityNameText);
        temperatureText = findViewById(R.id.temperatureText);
        descriptionText = findViewById(R.id.descriptionText);
        feelsLikeText = findViewById(R.id.feelsLikeText);
        humidityText = findViewById(R.id.humidityText);
        windSpeedText = findViewById(R.id.windSpeedText);
        pressureText = findViewById(R.id.pressureText);
    }

    @SuppressWarnings("deprecation")
    private void displayWeatherData() {
        WeatherData weatherData;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            weatherData = getIntent().getSerializableExtra("weatherData", WeatherData.class);
        } else {
            weatherData = (WeatherData) getIntent().getSerializableExtra("weatherData");
        }

        if (weatherData != null) {
            cityNameText.setText(weatherData.getCityName());
            temperatureText.setText(String.format(Locale.getDefault(), "%.1f°C", weatherData.getTemperature()));
            descriptionText.setText(capitalizeFirstLetter(weatherData.getDescription()));
            feelsLikeText.setText(String.format(Locale.getDefault(), "%.1f°C", weatherData.getFeelsLike()));
            humidityText.setText(String.format(Locale.getDefault(), "%d%%", weatherData.getHumidity()));
            windSpeedText.setText(String.format(Locale.getDefault(), "%.1f m/s", weatherData.getWindSpeed()));
            pressureText.setText(String.format(Locale.getDefault(), "%d hPa", weatherData.getPressure()));
        }
    }

    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

