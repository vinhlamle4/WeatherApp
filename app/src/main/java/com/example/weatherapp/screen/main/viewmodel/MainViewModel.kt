package com.example.weatherapp.screen.main.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.model.condition.Condition
import com.example.weatherapp.model.daily_forecast.DailyForecasts
import com.example.weatherapp.model.hour_forecast.HourForecast
import com.example.weatherapp.model.location.Location
import com.example.weatherapp.repo.WeatherRepository
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> get() = _bitmap

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
            val dailyForecasts = weatherRepository.getDailyForecastLocal() as ArrayList<DailyForecasts>
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
                val bitmap: Bitmap = Glide.with(appContext).asBitmap().load(R.mipmap.app_bg)
                    .placeholder(ColorDrawable(Color.TRANSPARENT))
                    .transform(BlurTransformation(4, 2))
                    .submit().get()
                _bitmap.postValue(bitmap)
            }
        }
    }
}