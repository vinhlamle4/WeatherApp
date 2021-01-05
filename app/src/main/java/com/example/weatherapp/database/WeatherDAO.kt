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

    //region GET
    @Query("SELECT * FROM location_table")
    suspend fun getLocation(): Location

    @Query("SELECT * FROM condition_table")
    suspend fun getCondition(): Condition

    @Query("SELECT * FROM hour_forecast_table")
    suspend fun getHourForecast(): List<HourForecast>

    @Query("SELECT * FROM daily_forecast_table")
    suspend fun getDailyForecast(): List<DailyForecasts>
    //endregion

    //region Delete All
    @Query("DELETE FROM location_table")
    suspend fun deleteAllLocation()

    @Query("DELETE FROM condition_table")
    suspend fun deleteAllCondition()

    @Query("DELETE FROM hour_forecast_table")
    suspend fun deleteAllHourForecast()

    @Query("DELETE FROM daily_forecast_table")
    suspend fun deleteAllDailyForecast()
    //endregion

    //region INSERT
    @Insert
    suspend fun insertLocation(location: Location)

    @Insert
    suspend fun insertCondition(condition: Condition)

    @Insert
    suspend fun insertHourForecast(hourForecast: HourForecast)

    @Insert
    suspend fun insertDailyForecast(dailyForecasts: DailyForecasts)
    //endregion

}