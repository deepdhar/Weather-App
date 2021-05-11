package com.example.android.weatherappyt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView1, textView2, feelsLikeTextview, cityTextView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        editText = findViewById(R.id.editText);
        textView1 = findViewById(R.id.weatherTextView1);
        textView2 = findViewById(R.id.weatherTextView2);
        feelsLikeTextview = findViewById(R.id.feelsLikeTextview);
        cityTextView = findViewById(R.id.cityTextview);
        button = findViewById(R.id.getWeatherButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // api from https://openweathermap.org/
                String apiKey = "YOUR_API_KEY";
                String city = editText.getText().toString();
                String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid=" + apiKey;
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response.getJSONObject("main");

                            String cityName = response.getString("name");
                            cityTextView.setText(cityName);

                            String temp = object.getString("temp");
                            Double tempC = Double.parseDouble(temp)-273.15;
                            textView1.setText(tempC.toString().substring(0,5));

                            JSONArray array = response.getJSONArray("weather");
                            JSONObject object2 = array.getJSONObject(0);
                            String weatherType = object2.getString("main");
                            textView2.setText(weatherType);

                            String feelsLike = object.getString("feels_like");
                            Double feelsLikeC = Double.parseDouble(feelsLike)-273.15;
                            feelsLikeTextview.setText(feelsLikeC.toString().substring(0,5));

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Enter valid city name", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(request);
            }
        });
    }
}
