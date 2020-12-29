package com.example.weatherapp.screen.main.viewmodel

import android.app.Application
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.model.condition.Condition
import com.example.weatherapp.model.daily_forecast.DailyForecasts
import com.example.weatherapp.model.hour_forecast.HourForecast
import com.example.weatherapp.model.location.Location
import com.example.weatherapp.repo.WeatherRepository
import jp.wasabeef.blurry.Blurry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

//https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#9

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val weatherRepository: WeatherRepository = WeatherRepository(application)

    private val _requestFail = MutableLiveData<String>()
    val requestFail: LiveData<String> get() = _requestFail

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> get() = _location

    private val _condition = MutableLiveData<Condition>()
    val condition: LiveData<Condition> get() = _condition

    private val _hourForecast = MutableLiveData<ArrayList<HourForecast>>()
    val hourForecast: LiveData<ArrayList<HourForecast>> get() = _hourForecast

    private val _dailyForecast = MutableLiveData<ArrayList<DailyForecasts>>()
    val dailyForecast: LiveData<ArrayList<DailyForecasts>> get() = _dailyForecast

    private val _bitmapComposer = MutableLiveData<Blurry.BitmapComposer>()
    val bitmapComposer: LiveData<Blurry.BitmapComposer> get() = _bitmapComposer

    private var appContext: Application = getApplication<Application>()

    fun getLocationAPI(location: String) {
        viewModelScope.launch {
            weatherRepository.fetchLocation(location,
                onSuccess = {
                    _location.postValue(it)
                }, onFailed = {
                    _requestFail.postValue(it)
                })
        }
    }

    fun getConditionAPI(locationKey: String) {
        viewModelScope.launch {
            weatherRepository.fetchCondition(locationKey, onSuccess = {
                _condition.postValue(it)
            }, onFailed = {
                _requestFail.postValue(it)
            })
        }
    }

    fun getHourForecastAPI(locationKey: String) {
        viewModelScope.launch {
            weatherRepository.fetchHourForecast(locationKey, onSuccess = {
                _hourForecast.postValue(it)
            }, onFailed = {
                _requestFail.postValue(it)
            })
        }
    }

    fun getForecastAPI(locationKey: String) {
        viewModelScope.launch {
            weatherRepository.fetchDailyForecast(locationKey, onSuccess = {
                _dailyForecast.postValue(it)
            }, onFailed = {
                _requestFail.postValue(it)
            })
        }
    }

    fun getWeatherInformationFromLocal() {
        viewModelScope.launch {
            val location = weatherRepository.getLocationLocal()
            val condition = weatherRepository.getConditionLocal()
            val hourForecasts = weatherRepository.getHourForecastLocal() as ArrayList<HourForecast>
            val dailyForecasts =
                weatherRepository.getDailyForecastLocal() as ArrayList<DailyForecasts>
            if (hourForecasts.size > 0 && dailyForecasts.size > 0) {
                _location.postValue(location)
                _condition.postValue(condition)
                _hourForecast.postValue(hourForecasts)
                _dailyForecast.postValue(dailyForecasts)
            }
        }
    }

    fun setAppBackGround() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val appBgs =
                    arrayOf(R.mipmap.app_bg, R.mipmap.app_bg1, R.mipmap.app_bg2, R.mipmap.app_bg3)

                val random = Random.nextInt(appBgs.size)

                val bitmap = BitmapFactory.decodeResource(appContext.resources, appBgs[random])

                val bitmapComposer = Blurry.with(appContext).radius(4).sampling(2).from(bitmap)
                _bitmapComposer.postValue(bitmapComposer)
            }
        }
    }
}