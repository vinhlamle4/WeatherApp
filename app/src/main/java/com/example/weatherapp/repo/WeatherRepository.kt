package com.example.weatherapp.repo

import android.app.Application
import android.util.Log
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.common.SUCCESS_CODE
import com.example.weatherapp.database.WeatherDatabase
import com.example.weatherapp.model.condition.Condition
import com.example.weatherapp.model.daily_forecast.DailyForecasts
import com.example.weatherapp.model.daily_forecast.Forecast
import com.example.weatherapp.model.hour_forecast.HourForecast
import com.example.weatherapp.model.location.Location
import com.example.weatherapp.service.WeatherAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WeatherRepository(val context: Application) : IWeatherRepository {
    private val weatherApi = WeatherAPI.getInstance.newInstance()

    private val weatherDb = WeatherDatabase.getDatabase(context)

    override suspend fun getLocationLocal(): Location = weatherDb.weatherDAO().getLocation()

    override suspend fun getConditionLocal(): Condition = weatherDb.weatherDAO().getCondition()

    override suspend fun getHourForecastLocal(): List<HourForecast> =
        weatherDb.weatherDAO().getHourForecast()

    override suspend fun getDailyForecastLocal(): List<DailyForecasts> =
        weatherDb.weatherDAO().getDailyForecast()

    override suspend fun insertLocation(location: Location) {
        weatherDb.weatherDAO().apply {
            deleteAllLocation()
            insertLocation(location)
        }
    }

    override suspend fun insertCondition(condition: Condition) {
                weatherDb.weatherDAO().apply {
            deleteAllCondition()
            insertCondition(condition)
        }
    }

    override suspend fun insertHourForecast(hourForecast: HourForecast) {
        weatherDb.weatherDAO().insertHourForecast(hourForecast)
    }

    override suspend fun insertDailyForecast(dailyForecasts: DailyForecasts) {
        weatherDb.weatherDAO().insertDailyForecast(dailyForecasts)
    }

    override suspend fun deleteAllHourForecast() {
        weatherDb.weatherDAO().deleteAllHourForecast()
    }

    override suspend fun deleteAllDailyForecast() {
        weatherDb.weatherDAO().deleteAllDailyForecast()
    }

    override suspend fun fetchLocation(
        location: String,
        onSuccess: (location: Location) -> Unit,
        onFailed: (message: String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val call = weatherApi.getLocation(BuildConfig.API_KEY, location)
            val response = enqueueAPI(call)
            try {
                @Suppress("UNCHECKED_CAST")
                val locations = (response as Response<ArrayList<Location>>).body()
                if (locations != null && locations.isNotEmpty()) {
                    insertLocation(locations.first())
                    onSuccess(locations.first())
                    return@withContext
                }
                onFailed(context.getString(R.string.request_location_fail))
            } catch (ex: Exception) {
                onFailed(response as String)
            }
        }
    }

    override suspend fun fetchCondition(
        locationKey: String,
        onSuccess: (condition: Condition) -> Unit,
        onFailed: (message: String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val call = weatherApi.getCondition(locationKey, BuildConfig.API_KEY, details = true)
            val response = enqueueAPI(call)

            try {
                @Suppress("UNCHECKED_CAST")
                val conditions = (response as Response<ArrayList<Condition>>).body()
                if (conditions != null && conditions.isNotEmpty()) {
                    insertCondition(conditions.first())
                    onSuccess(conditions.first())
                    return@withContext
                }
                onFailed(context.getString(R.string.request_condition_fail))
            } catch (ex: Exception) {
                onFailed(response as String)
            }
        }
    }

    override suspend fun fetchHourForecast(
        locationKey: String,
        onSuccess: (hourForecasts: ArrayList<HourForecast>) -> Unit,
        onFailed: (message: String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val call = weatherApi.getHourForecast(locationKey, BuildConfig.API_KEY, metric = true)
            val response = enqueueAPI(call)

            try {
                @Suppress("UNCHECKED_CAST")
                val forecasts = (response as Response<ArrayList<HourForecast>>).body()
                if (forecasts != null && forecasts.isNotEmpty()) {
                    deleteAllHourForecast()
                    forecasts.forEach {
                        insertHourForecast(it)
                    }
                    onSuccess(forecasts)
                    return@withContext
                }
                onFailed(context.getString(R.string.request_forecast_fail))
            } catch (ex: Exception) {
                onFailed(response as String)
            }
        }
    }

    override suspend fun fetchDailyForecast(
        locationKey: String,
        onSuccess: (dailyForecasts: ArrayList<DailyForecasts>) -> Unit,
        onFailed: (message: String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val call = weatherApi.getForecast(locationKey, BuildConfig.API_KEY, metric = true)
            val response = enqueueAPI(call)
            try {
                @Suppress("UNCHECKED_CAST")
                val forecasts = (response as Response<Forecast>).body()
                if (forecasts != null && forecasts.dailyForecasts.isNotEmpty()) {
                    deleteAllDailyForecast()
                    forecasts.dailyForecasts.forEach {
                        insertDailyForecast(it)
                    }
                    onSuccess(forecasts.dailyForecasts)
                    return@withContext
                }
                onFailed(context.getString(R.string.request_forecast_fail))
            } catch (ex: Exception) {
                Log.e("DailyForecast Error", "${ex.message}")
                onFailed(response as String)
            }
        }
    }

    //https://stackoverflow.com/a/48562175 change callback to coroutine
    private suspend fun <T> enqueueAPI(call: Call<T>) =
        suspendCoroutine<Any> {
            call.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.code() == SUCCESS_CODE) {
                        it.resume(response)
                        return
                    }
                    // failed
                    val result = response.errorBody()?.string() ?: ""
                    val json = JSONObject(result)
                    val message: String = json.getString("Message")
                        ?: context.getString(R.string.request_api_fail)
                    it.resume(message)
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    if (t is UnknownHostException) {
                        // no internet
                        it.resume(context.getString(R.string.un_known_host_exception))
                        return
                    }
                    //another error
                    it.resume(t.message ?: "")
                }
            })
        }
}