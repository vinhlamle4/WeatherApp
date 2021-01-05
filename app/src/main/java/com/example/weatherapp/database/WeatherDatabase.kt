package com.example.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.model.condition.Condition
import com.example.weatherapp.model.daily_forecast.DailyForecasts
import com.example.weatherapp.model.hour_forecast.HourForecast
import com.example.weatherapp.model.location.Location

@Database(
    entities = [
        Location::class, Condition::class, DailyForecasts::class, HourForecast::class], version = 1
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDAO(): WeatherDAO

    companion object {
        private var INSTANCE: WeatherDatabase? = null
        private const val DB_NAME = "weather_db"

        //https://yellowcodebooks.com/2019/05/24/android-architecture-component-phan-4-tim-hieu-ve-room/
        fun getDatabase(context: Context): WeatherDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }

        fun create(context: Context) : WeatherDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                DB_NAME
            ).build()
        }
    }
}