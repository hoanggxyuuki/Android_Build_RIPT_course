package com.example.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Spinner citySpinner;
    private EditText cityEditText;
    private Button getWeatherButton;

    private static final String API_KEY = "77aab1dff9400d5af90507248cb55901";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupCitySpinner();
        setupGetWeatherButton();
    }

    private void initViews() {
        citySpinner = findViewById(R.id.citySpinner);
        cityEditText = findViewById(R.id.cityEditText);
        getWeatherButton = findViewById(R.id.getWeatherButton);
    }

    private void setupCitySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this,
            R.array.cities,
            android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
    }

    private void setupGetWeatherButton() {
        getWeatherButton.setOnClickListener(v -> {
            String city = cityEditText.getText().toString().trim();

            if (city.isEmpty()) {
                city = citySpinner.getSelectedItem().toString();
            }

            if (city.isEmpty()) {
                Toast.makeText(this, R.string.error_city, Toast.LENGTH_SHORT).show();
                return;
            }

            fetchWeatherData(city);
        });
    }

    private void fetchWeatherData(String cityName) {
        String url = BASE_URL + "?q=" + cityName + "&appid=" + API_KEY + "&units=metric&lang=vi";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url(url)
            .build();

        Toast.makeText(this, R.string.loading, Toast.LENGTH_SHORT).show();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                    Toast.makeText(MainActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);

                        String city = json.getString("name");
                        JSONObject main = json.getJSONObject("main");
                        double temp = main.getDouble("temp");
                        double feelsLike = main.getDouble("feels_like");
                        int humidity = main.getInt("humidity");
                        int pressure = main.getInt("pressure");

                        JSONObject wind = json.getJSONObject("wind");
                        double windSpeed = wind.getDouble("speed");

                        JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
                        String description = weather.getString("description");
                        String icon = weather.getString("icon");

                        WeatherData weatherData = new WeatherData(
                            city, temp, feelsLike, humidity, windSpeed,
                            pressure, description, icon
                        );

                        runOnUiThread(() -> {
                            Intent intent = new Intent(MainActivity.this, WeatherDetailActivity.class);
                            intent.putExtra("weatherData", weatherData);
                            startActivity(intent);
                        });

                    } catch (Exception e) {
                        runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
                    }
                } else {
                    runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Không tìm thấy thành phố", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            showAboutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
            .setTitle(R.string.about_title)
            .setMessage(R.string.about_message)
            .setPositiveButton("OK", null)
            .show();
    }
}