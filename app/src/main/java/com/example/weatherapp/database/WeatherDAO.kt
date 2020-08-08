package com.example.weatherapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.model.condition.Condition
import com.example.weatherapp.model.daily_forecast.DailyForecasts
import com.example.weatherapp.model.hour_forecast.HourForecast
import com.example.weatherapp.model.location.Location

@Dao
interface WeatherDAO {

    @Query("SELECT * FROM location_table")
    suspend fun getLocation(): List<Location>

    @Query("SELECT * FROM location_table WHERE LocationKey LIKE :key")
    suspend fun getLocationWithKey(key: String): Location

    @Insert
    suspend fun insertLocation(location: Location)

    @Delete
    suspend fun deleteLocation(location: Location)

    @Insert
    suspend fun insertCondition(condition: Condition)

    @Delete
    suspend fun deleteCondition(condition: Condition)

    @Insert
    suspend fun insertHourForecast(hourForecast: HourForecast)

    @Delete
    suspend fun deleteHourForecast(hourForecast: HourForecast)

    @Insert
    suspend fun insertDailyForecast(dailyForecasts: DailyForecasts)

    @Delete
    suspend fun deleteDailyForecast(dailyForecasts: DailyForecasts)
}