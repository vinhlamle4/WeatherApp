package com.example.weatherapp.repo

import android.content.Context
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.database.WeatherDatabase
import com.example.weatherapp.model.condition.Condition
import com.example.weatherapp.model.daily_forecast.DailyForecasts
import com.example.weatherapp.model.daily_forecast.Forecast
import com.example.weatherapp.model.hour_forecast.HourForecast
import com.example.weatherapp.model.location.Location
import com.example.weatherapp.service.WeatherAPI
import retrofit2.Call

class WeatherRepository(val context: Context) {
    private val weatherApi = WeatherAPI.getInstance.newInstance()

    private val weatherDb = WeatherDatabase.getDatabase(context)

    fun getLocationAPI(location: String): Call<ArrayList<Location>> {
        return weatherApi.getLocation(BuildConfig.API_KEY, location)
    }

    fun getForecastAPI(
        locationKey: String,
        metric: Boolean = false
    ): Call<Forecast> {
        return weatherApi.getForecast(locationKey, BuildConfig.API_KEY, metric)
    }

    fun getConditionAPI(locationKey: String): Call<ArrayList<Condition>> {
        return weatherApi.getCondition(locationKey, BuildConfig.API_KEY, details = true)
    }

    fun getHourForecastAPI(locationKey: String): Call<ArrayList<HourForecast>> {
        return weatherApi.getHourForecast(locationKey, BuildConfig.API_KEY, metric = true)
    }

    suspend fun getLocation(): Location? = weatherDb.weatherDAO().getLocation()


    suspend fun getCondition(): Condition? = weatherDb.weatherDAO().getCondition()


    suspend fun getHourForecast(): List<HourForecast> = weatherDb.weatherDAO().getHourForecast()


    suspend fun getDailyForecast(): List<DailyForecasts> = weatherDb.weatherDAO().getDailyForecast()

    suspend fun insertLocation(location: Location) {
        weatherDb.weatherDAO().apply {
            deleteAllLocation()
            insertLocation(location)
        }
    }

    suspend fun insertCondition(condition: Condition) {
        weatherDb.weatherDAO().apply {
            deleteAllCondition()
            insertCondition(condition)
        }
    }

    suspend fun deleteAllHourForecast() {
        weatherDb.weatherDAO().deleteAllHourForecast()
    }

    suspend fun insertHourForecast(hourForecast: HourForecast) {
        weatherDb.weatherDAO().insertHourForecast(hourForecast)
    }

    suspend fun deleteAllDailyForecast() {
        weatherDb.weatherDAO().deleteAllDailyForecast()
    }

    suspend fun insertDailyForecast(dailyForecasts: DailyForecasts) {
        weatherDb.weatherDAO().insertDailyForecast(dailyForecasts)
    }
}