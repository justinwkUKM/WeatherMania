package com.waqas.myxlab.weathermania;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity {


    TextView tvDay, tvCondition, tvTemp, tvCity;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvDay= (TextView) findViewById(R.id.textViewDay);
        tvCondition= (TextView) findViewById(R.id.textViewCondition);
        tvTemp= (TextView) findViewById(R.id.textViewTemp);
        tvCity= (TextView) findViewById(R.id.textViewCity);

        imageView= (ImageView) findViewById(R.id.imageViewIcon);

        // http://api.openweathermap.org/data/2.5/weather?q=Khartoum&appid=51cff1995c328960306865dd5b38039f
        String url= getResources().getString(R.string.api_url)
                +"Khartoum&units=metric&appid="+
                getResources().getString(R.string.api_key);

        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        imageView.setAnimation(animationFadeIn);
        getWeatherData(url);

    }

    private void getWeatherData(String url) {
        // Initialize a new RequestQueue instance
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Do something with response
                        Log.e("Res",response.toString());
                        setDay();
                        try {
                            JSONArray jsonArray = response.getJSONArray("weather");
                            for (int i =0; i< jsonArray.length(); i++){
                                JSONObject weatherdata = jsonArray.getJSONObject(i);
                                String condition = weatherdata.getString("main");
                                tvCondition.setText(condition);
                            }

                            JSONObject main = response.getJSONObject("main");
                            double celsius = Double.parseDouble(main.getString("temp"));
                            int a = (int) Math.round(celsius);
                            //double  celsius =(( 5 *(fahrenheit - 32.0)) / 9.0);
                            tvTemp.setText(a+" C"+"\u00b0");


                            String name = response.getString("name");
                            tvCity.setText(name);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Log.e("Error",error.getMessage());
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }

    private void setDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        tvDay.setText(dayOfTheWeek);
    }

}
