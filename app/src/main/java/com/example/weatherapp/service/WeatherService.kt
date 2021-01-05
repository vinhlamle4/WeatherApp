package com.example.weatherapp.service

import com.example.weatherapp.model.condition.Condition
import com.example.weatherapp.model.daily_forecast.Forecast
import com.example.weatherapp.model.hour_forecast.HourForecast
import com.example.weatherapp.model.location.Location
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("locations/v1/cities/search")
    fun getLocation(
        @Query("apikey") apiKey: String,
        @Query("q") location: String
    ): Call<ArrayList<Location>>

    @GET("forecasts/v1/daily/5day/{locationKey}")
    fun getForecast(
        @Path("locationKey") locationKey: String, @Query("apikey") apiKey: String,
        @Query("metric") metric: Boolean
    ): Call<Forecast>

    @GET("currentconditions/v1/{locationKey}")
    fun getCondition(@Path("locationKey") locationKey: String, @Query("apikey") apiKey: String, @Query("details") details: Boolean): Call<ArrayList<Condition>>

    @GET("forecasts/v1/hourly/12hour/{locationKey}")
    fun getHourForecast(@Path("locationKey") locationKey: String, @Query("apikey") apiKey: String, @Query("metric") metric: Boolean): Call<ArrayList<HourForecast>>
}