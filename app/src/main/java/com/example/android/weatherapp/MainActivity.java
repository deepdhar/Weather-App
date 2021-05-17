package com.example.android.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView t1_temp, t2_city, t3_weather_type, t4_date, t5_feels_like, day_temp_textView, night_temp_textView;
    private Button citySearchButton;
    private EditText cityEditText;
    private ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1_temp = (TextView) findViewById(R.id.temp_textView);
        t2_city = (TextView) findViewById(R.id.city_textView);
        t3_weather_type = (TextView) findViewById(R.id.weather_type_textView);
        t4_date = (TextView) findViewById(R.id.date_textView);
        t5_feels_like = (TextView) findViewById(R.id.feelsLike_textView);
        day_temp_textView = (TextView) findViewById(R.id.day_temp_textView);
        night_temp_textView = (TextView) findViewById(R.id.night_temp_textView);
        citySearchButton = (Button) findViewById(R.id.citySearch_button);
        cityEditText = (EditText) findViewById(R.id.city_editText);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);


        citySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findWeather();
            }
        });
    }

    public void findWeather() {
        String apiKey = "4ebddb9e5d7ab62c4abd38ed54c920bc";
        String city = cityEditText.getText().toString();
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temp = main_object.getString("temp");
                    String feelsLike = main_object.getString("feels_like");
                    String weatherType = object.getString("main");
                    String cityName = response.getString("name");
                    String day = main_object.getString("temp_max");
                    String night = main_object.getString("temp_min");

                    double max = Double.parseDouble(day) - 273.15;
                    int maxC = (int) Math.round(max);
                    day_temp_textView.setText(maxC + "°↑");

                    double min = Double.parseDouble(night) - 273.15;
                    int minC = (int) Math.round(min);
                    night_temp_textView.setText(minC + "°↓");

                    if(weatherType.equals("Thunderstorm"))
                        weatherIcon.setImageResource(R.drawable.thunderstorm);
                    else if(weatherType.equals("Drizzle"))
                        weatherIcon.setImageResource(R.drawable.showers);
                    else if(weatherType.equals("Snow"))
                        weatherIcon.setImageResource(R.drawable.snow);
                    else if(weatherType.equals("Mist") || weatherType.equals("Smoke") || weatherType.equals("Haze") || weatherType.equals("Dust")
                    || weatherType.equals("Fog") || weatherType.equals("Sand") || weatherType.equals("Ash") || weatherType.equals("Squall")
                    || weatherType.equals("Tornado"))
                        weatherIcon.setImageResource(R.drawable.mist);
                    else if(weatherType.equals("Clear"))
                        weatherIcon.setImageResource(R.drawable.sunny);
                    else if(weatherType.equals("Clouds"))
                        weatherIcon.setImageResource(R.drawable.broken_clouds);


                    t2_city.setText(cityName);
                    t3_weather_type.setText(weatherType);

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                    String formatted_date = sdf.format(calendar.getTime());

                    t4_date.setText(formatted_date);

                    double temp_int = Double.parseDouble(temp) - 273.15;
                    int i = (int) Math.round(temp_int);
                    t1_temp.setText(String.valueOf(i));

                    double temp_int_feels = Double.parseDouble(feelsLike) - 273.15;
                    int j = (int) Math.round(temp_int_feels);
                    t5_feels_like.setText(String.valueOf(j) + "°");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Enter valid city name", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }
}