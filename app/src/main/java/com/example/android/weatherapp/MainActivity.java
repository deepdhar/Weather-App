package com.example.android.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private TextView t1_temp, t2_city, t3_weather_type, description_textView;
    private TextView wind_speed_textView, humidity_textView, visibility_textView, search_city_bn;
    private Button citySearchButton;
    private EditText cityEditText;
    private ImageView weatherIcon;
    private LinearLayout homeLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1_temp = (TextView) findViewById(R.id.temp_textView);
        t2_city = (TextView) findViewById(R.id.city_textView);
        t3_weather_type = (TextView) findViewById(R.id.weather_type_textView);
        search_city_bn = (TextView) findViewById(R.id.citySearch_button);
        cityEditText = (EditText) findViewById(R.id.city_editText);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        wind_speed_textView = (TextView) findViewById(R.id.wind_speed_tv);
        humidity_textView = (TextView) findViewById(R.id.humidity_tv);
        visibility_textView = (TextView) findViewById(R.id.visibility_tv);
        description_textView = (TextView) findViewById(R.id.desc_tv);

        homeLinearLayout = (LinearLayout) findViewById(R.id.home_linearLayout);

        search_city_bn.setOnClickListener(new View.OnClickListener() {
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
                    String desc = object.getString("description");
                    String cityName = response.getString("name");
                    String day = main_object.getString("temp_max");
                    String night = main_object.getString("temp_min");

                    if(weatherType.equals("Thunderstorm")) {
                        weatherIcon.setImageResource(R.drawable.thunderstorm);
                        homeLinearLayout.setBackgroundResource(R.drawable.rainy_bg);
                    }
                    else if(weatherType.equals("Drizzle") || weatherType.equals("Rain")) {
                        weatherIcon.setImageResource(R.drawable.showers);
                        homeLinearLayout.setBackgroundResource(R.drawable.rainy_bg);
                    }
                    else if(weatherType.equals("Snow")) {
                        weatherIcon.setImageResource(R.drawable.snow);
                        homeLinearLayout.setBackgroundResource(R.drawable.cloudy_bg);
                    }
                    else if(weatherType.equals("Mist") || weatherType.equals("Smoke") || weatherType.equals("Haze") || weatherType.equals("Dust")
                    || weatherType.equals("Fog") || weatherType.equals("Sand") || weatherType.equals("Ash") || weatherType.equals("Squall")
                    || weatherType.equals("Tornado")) {
                        weatherIcon.setImageResource(R.drawable.mist);
                        homeLinearLayout.setBackgroundResource(R.drawable.cloudy_bg);
                    }
                    else if(weatherType.equals("Clear")) {
                        weatherIcon.setImageResource(R.drawable.sunny);
                        homeLinearLayout.setBackgroundResource(R.drawable.sunny_bg);
                    }
                    else if(weatherType.equals("Clouds")) {
                        weatherIcon.setImageResource(R.drawable.broken_clouds);
                        homeLinearLayout.setBackgroundResource(R.drawable.cloudy_bg);
                    }



                    t2_city.setText(cityName);
                    t3_weather_type.setText(weatherType);

                    description_textView.setText(desc);


                    JSONObject wind_object = response.getJSONObject("wind");
                    String wind_speed = wind_object.getString("speed");
                    wind_speed_textView.setText(wind_speed + " km/h");

                    String humidity = main_object.getString("humidity");
                    humidity_textView.setText(humidity + "%");


//                    Calendar calendar = Calendar.getInstance();
//                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
//                    String formatted_date = sdf.format(calendar.getTime());
//
//                    t4_date.setText(formatted_date);

                    double temp_int = Double.parseDouble(temp) - 273.15;
                    int i = (int) Math.round(temp_int);
                    t1_temp.setText(String.valueOf(i));


                    String visibility = response.getString("visibility");
                    double visb_int = Double.parseDouble(visibility) / 1000;
                    visibility_textView.setText(String.valueOf(visb_int) + " km");


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