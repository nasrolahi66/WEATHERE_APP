package com.example.weathereapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathereapp.CurrentWeatherClass.CurrentWeatherClass;
import com.example.weathereapp.ForecastWeatherClass.ForecastWeatherClass;
import com.example.weathereapp.ForecastWeatherClass.Weather;
import com.example.weathereapp.ForecastWeatherClass.WeatherDate;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    TextView txtCityName;
    TextView txtDate;
    TextView txtCityTempreture;
    ImageView imgIcon;
    TextView txtHumidity;
    TextView txtTempMin;
    TextView txtTempMax;
    TextView txtWind;
    RecyclerView nextDaysRecycler;
    ImageView imgEdit;
    String cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        txtCityName=findViewById(R.id.txtCityName);
        txtDate=findViewById(R.id.txtDate);
        txtCityTempreture=findViewById(R.id.txtCityTempreture);
        imgIcon=findViewById(R.id.imgIcon);
        txtHumidity=findViewById(R.id.txtHumidity);
        txtTempMin=findViewById(R.id.txtTempMin);
        txtTempMax=findViewById(R.id.txtTempMax);
        txtWind=findViewById(R.id.txtWind);
        nextDaysRecycler=findViewById(R.id.nextDaysRecycler);
        txtDate.setText(getDate());
        imgEdit=findViewById(R.id.imgEdit);
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             ShowSuggestions();
            }
        });
        txtCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSuggestions();
            }
        });

        FillData("");
    }

    private void ShowSuggestions() {
        Intent intent=new Intent(MainActivity.this,CityesTitleActivity.class);
        startActivityForResult(intent,123);
    }

    private void FillData(String cityName){
        AsyncHttpClient client=new AsyncHttpClient();
        if(cityName== "")
            cityName="tehran";
        final Context _context = this;
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+ cityName +"&units=metric&APPID=19dc69cdc6b7304758697de9e2fa6e25";
        try{
            client.get(url,new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Gson gson=new Gson();
                    CurrentWeatherClass currentWeatherClass = gson.fromJson(response.toString(), CurrentWeatherClass.class);

                    txtCityName.setText(currentWeatherClass.getName() + ", " + currentWeatherClass.getSys().getCountry());
                    String temp= Math.round( currentWeatherClass.getMain().getTemp().intValue()) + "";
                    txtCityTempreture.setText(temp);
                    txtHumidity.setText(currentWeatherClass.getMain().getHumidity().toString() +" %");
                    txtTempMin.setText(currentWeatherClass.getMain().getTempMin().toString() +" °C");
                    txtTempMax.setText(currentWeatherClass.getMain().getTempMax().toString() +" °C");
                    txtWind.setText(currentWeatherClass.getWind().getSpeed().toString() +" Km/h");

                    String icon=currentWeatherClass.getWeather().get(0).getIcon();
                    String url="http://openweathermap.org/img/wn/" + icon + "@2x.png";
                    Picasso.with(MainActivity.this).load(url).into(imgIcon);
                }
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);

                }
            });

            String forecastUrl = "http://api.openweathermap.org/data/2.5/forecast?q="+ cityName +"&units=metric&APPID=19dc69cdc6b7304758697de9e2fa6e25";
            AsyncHttpClient foreCastClient=new AsyncHttpClient();
            foreCastClient.get(forecastUrl,new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Gson gson=new Gson();
                    ForecastWeatherClass forecastWeatherClass = gson.fromJson(response.toString(), ForecastWeatherClass.class);

                    List<WeatherDate> adapterList = new ArrayList<>();
                    String lastDate = "";
                    for (int i =0;i< forecastWeatherClass.getList().size();i+=8) {
                        com.example.weathereapp.ForecastWeatherClass.List forecastList = forecastWeatherClass.getList().get(i);
                        lastDate = forecastList.getDtTxt().substring(0,10);
                        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = null;
                        try {
                            date1 = parser.parse(lastDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Weather weather = forecastList.getWeather().get(0);
                        adapterList.add(new WeatherDate(forecastList.getMain().getTemp(),  weather.getIcon(), date1));
                        //}
                    }
                    WeatherForecastAdapter adapter = new WeatherForecastAdapter(adapterList, MainActivity.this );
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    nextDaysRecycler.setLayoutManager(layoutManager);
                    nextDaysRecycler.setAdapter(adapter);
                }
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }catch (Exception exception){
            Toast.makeText(this,exception.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    public String getDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM");
        String formattedDate = df.format(c);
        return  formattedDate;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode==123 && resultCode==RESULT_OK)
        {
            cityName=data.getStringExtra("CityName");
           FillData(cityName);
        }
    }

}

