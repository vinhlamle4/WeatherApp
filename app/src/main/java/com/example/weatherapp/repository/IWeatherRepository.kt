package com.example.weatherapp.repository

import com.example.weatherapp.model.condition.Condition
import com.example.weatherapp.model.daily_forecast.DailyForecasts
import com.example.weatherapp.model.hour_forecast.HourForecast
import com.example.weatherapp.model.location.Location

interface IWeatherRepository {
    //region Room
    suspend fun getLocationLocal(): Location
    suspend fun getConditionLocal(): Condition
    suspend fun getHourForecastLocal(): List<HourForecast>
    suspend fun getDailyForecastLocal(): List<DailyForecasts>

    suspend fun insertLocation(location: Location)
    suspend fun insertCondition(condition: Condition)
    suspend fun insertHourForecast(hourForecast: HourForecast)
    suspend fun insertDailyForecast(dailyForecasts: DailyForecasts)

    suspend fun deleteAllHourForecast()
    suspend fun deleteAllDailyForecast()
    //endregion

    //region Retrofit
    suspend fun fetchLocation(
        location: String,
        onSuccess: (location: Location) -> Unit,
        onFailed: (message: String) -> Unit
    )

    suspend fun fetchCondition(
        locationKey: String,
        onSuccess: (condition: Condition) -> Unit,
        onFailed: (message: String) -> Unit
    )

    suspend fun fetchHourForecast(
        locationKey: String,
        onSuccess: (hourForecasts: ArrayList<HourForecast>) -> Unit,
        onFailed: (message: String) -> Unit
    )

    suspend fun fetchDailyForecast(
        locationKey: String,
        onSuccess: (dailyForecasts: ArrayList<DailyForecasts>) -> Unit,
        onFailed: (message: String) -> Unit
    )
    //endregion
}