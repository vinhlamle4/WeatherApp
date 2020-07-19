package com.example.weatherapp.service

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.model.condition.Condition
import com.example.weatherapp.model.forecast.Forecast
import com.example.weatherapp.model.location.Location
import retrofit2.Call

class WeatherRepository {
    private val weatherApi = WeatherAPI.getInstance.newInstance()

    fun getLocation(location: String): Call<List<Location>> {
        return weatherApi.getLocation(BuildConfig.API_KEY, location)
    }

    fun getForecast(
        locationKey: String,
        metric: Boolean = false
    ): Call<Forecast> {
        return weatherApi.getForecast(locationKey, BuildConfig.API_KEY, metric)
    }

    fun getCondition(locationKey: String): Call<List<Condition>>  {
        return weatherApi.getCondition(locationKey, BuildConfig.API_KEY, true)
    }
}