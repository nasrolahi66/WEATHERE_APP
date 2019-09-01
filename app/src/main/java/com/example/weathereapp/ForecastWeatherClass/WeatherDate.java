package com.example.weathereapp.ForecastWeatherClass;

import java.util.Date;

public class WeatherDate {
    public Date date;
    public double temperture;
    public String icon;
    public WeatherDate(double _temperture,String _icon,Date _date){
        temperture = _temperture;
        icon = _icon;
        date = _date;
    }
}
