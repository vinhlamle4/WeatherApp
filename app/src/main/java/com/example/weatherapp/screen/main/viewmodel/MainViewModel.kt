package com.example.weatherapp.screen.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.common.SUCCESS_CODE
import com.example.weatherapp.model.condition.Condition
import com.example.weatherapp.model.daily_forecast.Forecast
import com.example.weatherapp.model.hour_forecast.HourForecast
import com.example.weatherapp.model.location.Location
import com.example.weatherapp.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

//https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#9

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherRepository: WeatherRepository

    init {
        weatherRepository =
            WeatherRepository(application)
    }

    private val _requestFail = MutableLiveData<String>()
    val requestFail: LiveData<String> get() = _requestFail

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> get() = _location

    private val _condition = MutableLiveData<Condition>()
    val condition: LiveData<Condition> get() = _condition

    private val _hourForecast = MutableLiveData<ArrayList<HourForecast>>()
    val hourForecast: LiveData<ArrayList<HourForecast>> get() = _hourForecast

    private val _forecast = MutableLiveData<Forecast>()
    val forecast: LiveData<Forecast> get() = _forecast

    fun getLocation(location: String) {
        viewModelScope.launch {
            callLocationAPI(location)
        }
    }

    private suspend fun callLocationAPI(location: String) {
        withContext(Dispatchers.IO) {
            val call = weatherRepository.getLocation(location)

            val locations = enqueueAPI(call).body()
            if (locations != null && locations.size > 0) {
                _location.postValue(locations[0])
                weatherRepository.insertLocation(locations[0])
                return@withContext
            }
            _requestFail.postValue(getApplication<Application>().getString(R.string.request_location_fail))
        }
    }

    fun getCondition(locationKey: String) {
        viewModelScope.launch {
            callConditionApI(locationKey)
        }
    }

    private suspend fun callConditionApI(locationKey: String) {
        withContext(Dispatchers.IO) {
            val call = weatherRepository.getCondition(locationKey)

            val conditions = enqueueAPI(call).body()
            if (conditions != null && conditions.size > 0) {
                _condition.postValue(conditions[0])
                weatherRepository.insertCondition(conditions[0])
                return@withContext
            }
            _requestFail.postValue(
                getApplication<Application>().getString(R.string.request_forecast_fail)
            )
        }
    }

    fun getHourForecast(locationKey: String) {
        viewModelScope.launch {
            callHourForecastAPI(locationKey)
        }
    }

    private suspend fun callHourForecastAPI(locationKey: String) {
        withContext(Dispatchers.IO) {
            val call = weatherRepository.getHourForecast(locationKey)

            val hourForecasts = enqueueAPI(call).body()
            if (hourForecasts != null && hourForecasts.size > 0) {
                _hourForecast.postValue(hourForecasts)
                hourForecasts.forEach {
                    weatherRepository.insertHourForecast(it)
                }
                return@withContext
            }
            _requestFail.postValue(
                getApplication<Application>().getString(R.string.request_forecast_fail)
            )

        }
    }

    fun getForecast(locationKey: String) {
        viewModelScope.launch {
            callForecastApi(locationKey)
        }
    }

    private suspend fun callForecastApi(locationKey: String) {
        withContext(Dispatchers.IO) {
            val call = weatherRepository.getForecast(locationKey, metric = true)

            val body = enqueueAPI(call).body()
            if (body != null) {
                val forecastInfo = Forecast(body.headline, body.dailyForecasts)
                _forecast.postValue(forecastInfo)
                body.dailyForecasts.forEach {
                    weatherRepository.insertDailyForecast(it)
                }
                return@withContext
            }
            _requestFail.postValue(
                getApplication<Application>().getString(R.string.request_forecast_fail)
            )
        }
    }

    //https://stackoverflow.com/a/48562175 change callback to coroutine
    private suspend fun <T> enqueueAPI(call: Call<T>) = suspendCoroutine<Response<T>> {
        call.enqueue(object : Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                if (t is UnknownHostException) {
                    _requestFail.postValue(getApplication<Application>().getString(R.string.un_known_host_exception))
                    return
                }
                _requestFail.postValue(t.message)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.code() == SUCCESS_CODE) {
                    //callback(response)
                    it.resume(response)
                    return
                }
                val result = response.errorBody()?.string() ?: ""
                val json = JSONObject(result)
                val message: String? = json.getString("Message")
                    ?: getApplication<Application>().getString(R.string.request_api_fail)
                _requestFail.postValue(message)
            }
        })
    }
}